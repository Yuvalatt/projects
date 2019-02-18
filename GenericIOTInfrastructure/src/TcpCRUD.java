import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpCRUD implements CRUD<String, Integer> {
	private static final long serialVersionUID = 1L;
	private static final int BUFF_SIZE = 1024;
	private SocketChannel channel;
	private InetSocketAddress address;
	
	public TcpCRUD(String hostName, int port) throws UnknownHostException, IOException {
		address = new InetSocketAddress(hostName, port);
		channel = SocketChannel.open();	
	}

	public void connect() throws IOException {
		channel.connect(address);
	}
	
	public void disconnect() {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Integer create(String data) {
		ByteBuffer buf = ByteBuffer.allocate(BUFF_SIZE);
		buf.clear();
		try {		
			buf.put(data.getBytes());
			buf.flip();
			while(buf.hasRemaining()) {
				channel.write(buf);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String read(Integer id) {
		return null;
	}

	@Override
	public void update(Integer id, String data) {
	}

	@Override
	public void delete(Integer id) {
	}
}