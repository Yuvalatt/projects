//package il.co.ilrd.chatClientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements Runnable {
	private ServerSocket serverSocket;
	private Set<ClientHandler> clients = new HashSet<>();
	private ExecutorService pool;
	
	public ChatServer(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(10);
		System.out.println("Server Connected");
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ClientHandler c = new ClientHandler(socket);
				System.out.println("New Client accepted");
				clients.add(c);
				pool.execute(c);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ClientHandler implements Runnable {
		private Socket s;
		private DataInputStream in;
		private DataOutputStream out;

		public ClientHandler(Socket socket) throws IOException{
			s = socket;
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());		
		}
		
		@Override
		public void run() {
			String currMessage = "";
			while (true) {
				try {
					currMessage = in.readUTF();
					for (ClientHandler h : clients) {
						if (!h.equals(this)) {
							h.out.writeUTF(currMessage);
						}
					}					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			ChatServer server = new ChatServer(20000);
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}