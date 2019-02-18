package il.co.ilrd.networking;

import java.io.IOException;
import java.net.Inet6Address;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientFileMonitor   {

	private FileMonitor<String> fileMonitor;
	private FileObserver<String> fileObserver;
	private static int id = 1000;

	public ClientFileMonitor(Path monitorPath, CRUD<String, Integer> crud) throws IOException {
		fileObserver = new FileObserver<>(crud, ++id);
		fileMonitor = new FileMonitor<>(monitorPath);
		fileObserver.register(fileMonitor);
	}

	public void start() {		
		fileMonitor.startMonitor();
	}

	public void stop() {
		fileMonitor.stopMonitor();
	}

	public static void main(String[] args) throws InterruptedException, IOException {	
		String LOG_PATH = "/home/rdz/git/yuval-attar/logFile.txt";
		Server server = new Server(LOG_PATH, 20000);
		server.start();
		//CRUD<String, Integer> c = new FileCRUD<>("/home/rdz/git/yuval-attar/logFile.txt");
		TcpCRUD c1 = new TcpCRUD(Inet6Address.getLocalHost().getHostAddress(), 20000);
		UdpCRUD c2 = new UdpCRUD(Inet6Address.getLocalHost().getHostAddress(), 20000);
		TcpCRUD c3 = new TcpCRUD(Inet6Address.getLocalHost().getHostAddress(), 20000);

		ClientFileMonitor client1 = new ClientFileMonitor(Paths.get("/home/rdz/git/yuval-attar/input1"), c1);
		ClientFileMonitor client2 = new ClientFileMonitor(Paths.get("/home/rdz/git/yuval-attar/input2"), c2);
		ClientFileMonitor client3 = new ClientFileMonitor(Paths.get("/home/rdz/git/yuval-attar/input3"), c3);

		c1.connect();
		c2.connect();
		c3.connect();
		client1.start();
		client2.start();
		client3.start();

		/*Thread.sleep(4000);			
			System.out.println("SHUTDOWN...");
			server.shutdown();
			client1.stop();
			client2.stop();
			client3.stop();*/
	}	
}