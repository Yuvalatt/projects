import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class HTTPCrud implements CRUD<String, Integer>{
	private static final long serialVersionUID = 1L;	
	private HttpClient client;
	private String url;
	
	public HTTPCrud(String url) {
		client = HttpClients.createDefault();
		this.url = url;
	}
		
	@Override
	public Integer create(String data) {
		HttpPost httpPost = new HttpPost(url);  
		httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
		try {
			StringEntity se = new StringEntity(data);
			httpPost.setEntity(se);
			HttpResponse res = client.execute(httpPost);
			System.out.println("response status: " + res.getStatusLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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