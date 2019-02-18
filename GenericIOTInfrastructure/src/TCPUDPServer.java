import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class TCPUDPServer implements Runnable {
	private final int BUFF_SIZE = 1024;
	private final int TIMEOUT = 1000;
	private boolean enabled = true;
	private CRUD<String, Integer> crud;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;  //TCP channel
	private DatagramChannel udpChannel;				  //UDP channel

	public TCPUDPServer(int port, CRUD<String, Integer> crud) throws IOException {
		InitSelector(port);
		this.crud = crud;
		System.out.println("Server Connected");
	}

	public void start() {
		new Thread(this).start();	
	}

	private void InitSelector(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), port));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		udpChannel = DatagramChannel.open();
		//udpChannel.configureBlocking(false);
		udpChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), port));
		udpChannel.register(selector, SelectionKey.OP_READ);
	}

	@Override
	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(BUFF_SIZE);
		while (enabled) {
			try {
				if (0 == selector.select(TIMEOUT)) {
					continue;
				}
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();

				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					if (key.isAcceptable()) {
						registerClient();
					}
					if (key.isReadable()) {
						writeToLog(buffer, key);
					}
					iter.remove();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void registerClient() throws IOException {
		SocketChannel channel = serverSocketChannel.accept();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
	}

	private void writeToLog(ByteBuffer buffer, SelectionKey key) throws IOException {
		buffer.clear();
		if (key.channel().getClass().isInstance(udpChannel)) {
			DatagramChannel channel = (DatagramChannel) key.channel();
			channel.receive(buffer);
		} else {
			SocketChannel channel = (SocketChannel)key.channel();
			channel.read(buffer);
		}
		buffer.flip();
		String message = StandardCharsets.UTF_8.decode(buffer).toString();
		crud.create(message);
	}

	public void shutdown() {
		enabled = false;
		try {
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}