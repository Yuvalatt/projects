//package il.co.ilrd.chatClientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient{
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private Scanner scanner = new Scanner(System.in);
	
	public ChatClient(String address, int port) {
		try {
			socket = new Socket(address, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());			
			System.out.println("Client Connected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Runnable sendMessage = new Runnable() {
		@Override
		public void run() {		
			String line = "";
			while (true) {
				try {
					line = scanner.nextLine();
					out.writeUTF(line);
					out.flush();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	Runnable getMessage = new Runnable() {
		@Override
		public void run() {
			String line = "";
			while (true) {
				try {
					line = in.readUTF();
					System.out.println(line);
				}
				catch(IOException e) {
					e.printStackTrace();
				}		
			}
		}
	};
	
	public void start() {
		new Thread(getMessage).start();
		new Thread(sendMessage).start();
	}
	public static void main(String[] args) {
		ChatClient client = new ChatClient("10.1.0.94", 20000);
		client.start();
	}
}
