package il.co.ilrd.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

public class WebPage {	
	URL web;
		
	//Downloading a web page
	public WebPage() throws MalformedURLException{
		web = new URL("http://www.journaldev.com/741/java-socket-programming-server-client");
	}
	
	public void printWeb() throws IOException {
		try(BufferedReader in = new BufferedReader(new InputStreamReader(web.openStream()));
			OutputStreamWriter out = new OutputStreamWriter(System.out)) {
			String line;
			while((line = in.readLine()) != null) {
				out.write(line + "\n");
			}			
		}
	}
	
	public static void main(String[] args) {
		try {
			WebPage w = new WebPage();
			w.printWeb();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
