
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IOTDevice {
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		new GatewayServer(10);
				
		/*TcpCRUD tcpCRUD = new TcpCRUD(InetAddress.getLocalHost().getHostAddress(), SERVER_PORT);
		tcpCRUD.connect();
		tcpCRUD.create("{\"key\":\"UPDATE_IOT\",\"company\":\"tadiran\",\"product\":\"alpha\", \"info\":\"23\"}");
			*/	
		HTTPCrud httpcrud = new HTTPCrud("http://localhost:8000/request");
		httpcrud.create("{\"key\":\"REGISTER_PRODUCT\",\"company\":\"tadiran\",\"product\":\"alpha\", \"info\":\"44\"}");
		 
		//HTTPCrud httpcrud2 = new HTTPCrud("http://localhost:8000/request");
		//httpcrud2.create("{\"key\":\"UPDATE_IOT\",\"company\":\"tadiran\",\"product\":\"alpha\", \"info\":\"23\"}");
	}
}