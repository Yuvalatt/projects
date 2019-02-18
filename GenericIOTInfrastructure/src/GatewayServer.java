import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.json.JSONObject;
import com.sun.net.httpserver.*;

public class GatewayServer {
	private static final int SERVER_PORT = 5000;
	private static final int HTTP_SERVER_PORT = 8000;
	private static int serialNumber = 0;
	private TCPUDPServer tcpUdpServer;
	private HttpServer httpServer;
	private ExecutorService threadPool;
	private CommandFactory<String, JSONObject, CRUD<String, Integer>> factory = CommandFactory.getFactory(); //singleton command factory

	public GatewayServer(int threadsNum){
		initHTTPServer();
		initTCPUDPServer();
		initCommandFactory();
		threadPool = Executors.newFixedThreadPool(threadsNum);
	}

	private void initTCPUDPServer(){
		try {
			tcpUdpServer = new TCPUDPServer(SERVER_PORT, new HTTPCrud("http://localhost:8000/request"));
			tcpUdpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}

	private void initHTTPServer(){
		try {
			httpServer = HttpServer.create(new InetSocketAddress(HTTP_SERVER_PORT), 0);
			httpServer.createContext("/request", new RequestHandler());
			httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private void initCommandFactory() {
		factory.add("REGISTER_PRODUCT", registerProduct);
		factory.add("UPDATE_IOT", updateDevice);
	} 

	Function<JSONObject, CRUD<String, Integer>> registerProduct = new Function<JSONObject, CRUD<String, Integer>>() {
		@Override
		public CRUD<String, Integer> apply(JSONObject jsonObj) {
			new HTTPCrud("http://localhost:8080/GenericIOTInfrastructure/ProductServlet").create(jsonObj.toString());
			return null;
		}
	};

	Function<JSONObject, CRUD<String, Integer>> updateDevice = new Function<JSONObject, CRUD<String, Integer>>() {
		@Override
		public CRUD<String, Integer> apply(JSONObject jsonObj) {
			new HTTPCrud("http://localhost:8080/GenericIOTInfrastructure/IOTServlet").create(jsonObj.toString());
			return null;
		}
	};

	//command's key is given by the JSON object
	class RequestHandler implements HttpHandler {	
		@Override
		public void handle(HttpExchange t) throws IOException { 
			InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
			BufferedReader br = new BufferedReader(isr);
			JSONObject jsonObj = new JSONObject(br.readLine());
			jsonObj.put("id", (new Integer(++serialNumber)).toString());
			String key = jsonObj.getString("key");
			//System.out.println("JSON:" + jsonObj.toString());
			
			Callable<CRUD<String, Integer>> task = new Callable<CRUD<String, Integer>>() {			
				@Override
				public CRUD<String, Integer> call() throws Exception {				
					factory.create(key, jsonObj);
					return null;
				}
			};
			threadPool.submit(task);
		}
	}
}