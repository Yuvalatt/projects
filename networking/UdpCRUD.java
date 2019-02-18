package il.co.ilrd.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UdpCRUD implements CRUD<String, Integer> {
	private static final long serialVersionUID = 1L;
	private static final int BUFF_SIZE = 1024;
	private DatagramChannel udpChannel;
	private InetSocketAddress socketAddress;
	
	public UdpCRUD(String hostName, int port) throws UnknownHostException, IOException {
		socketAddress = new InetSocketAddress(hostName, port);
		udpChannel = DatagramChannel.open();
	}
	
	public void connect() throws IOException {
		udpChannel.socket().connect(socketAddress);
	}
	
	public void disconnect() {
		try {
			udpChannel.disconnect();
			udpChannel.close();
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
			udpChannel.send(buf, socketAddress);
		} catch (IOException e) {
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