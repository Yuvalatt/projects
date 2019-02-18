package il.co.ilrd.networking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Server implements Runnable {
	private final int BUFF_SIZE = 1024;
	private final int TIMEOUT = 1000;
	private File logFile;
	private Selector selector;
	private ServerSocketChannel serverSocket;
	private DatagramChannel udpChannel;
	private boolean enabled = true;
	private List<Channel> channels = new ArrayList<>();

	public Server(String path, int port) throws IOException {
		logFile = new File(path);
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		InitSelector(port);
		System.out.println("Server Connected");
	}

	public void start() {
		new Thread(this).start();	
	}
	
	private void InitSelector(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);

		udpChannel = DatagramChannel.open();
		udpChannel.configureBlocking(false);
		udpChannel.socket().bind(new InetSocketAddress(port));
		udpChannel.register(selector, SelectionKey.OP_READ);
		channels.add(udpChannel);
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
		SocketChannel channel = serverSocket.accept();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
		channels.add(channel);
	}

	private void writeToLog(ByteBuffer buffer, SelectionKey key) throws IOException {
		buffer.clear();
		if (key.channel().equals(channels.get(0))) { // in case channel is UDP Channel
			DatagramChannel channel = (DatagramChannel) key.channel();
			channel.receive(buffer);
		} else {
			SocketChannel channel = (SocketChannel)key.channel();
			channel.read(buffer);
		}
		try (FileWriter out = new FileWriter(logFile, true)) {
			buffer.flip();
			String message = StandardCharsets.UTF_8.decode(buffer).toString();
			out.append(message);
		}
	}

	public void shutdown() {
		enabled = false;
		try {
			for (Channel c : channels) {
				c.close();
			}
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}