package myTomcat.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.lang.StringBuilder;
import java.util.HashMap;

public class HttpRequest{
	private Socket socket;
	
	private String method;
	private String url;
	private String protocol;
	private InputStream in;
	private String requestURI;
	private String queryString;
	private HashMap<String,String> headers = new HashMap<String,String>();
	private HashMap<String,String> parameters = new HashMap<String,String>();
	
	
	
	public HttpRequest(){}

	public HttpRequest(Socket socket) throws EmptyRequestException{
		try{
			this.socket = socket;
			this.in = socket.getInputStream();

			parse_request_line();
			parse_request_header();
			parse_content();
		}catch(EmptyRequestException e) {
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getMethod(){
		return this.method;
	}

	public String getUrl(){
		return this.url;
	}

	public String getProtocol(){
		return this.protocol;
	}

	public String getHeader(String name){
		return this.headers.getOrDefault(name,"");
	}

	public String read_line(){
		try{
			int d = -1;
			char c1='a',c2='a';
			StringBuilder builder = new StringBuilder();
			while((d = in.read())!=-1) {
				c2 = (char)d;
				if(c1==13&&c2==10) { //CRLF
					break;
				}
				builder.append(c2);
				c1 =c2;
			}
			String line = builder.toString().trim();
			return line;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
	}

	public void parse_request_line() throws EmptyRequestException{
		String line = read_line();
		String[] request_line = line.split(" ");
		if(request_line.length < 3) {
			throw new EmptyRequestException();
		}
		this.method = request_line[0];
		this.url = request_line[1];
		this.protocol = request_line[2];
		parse_url();
	}
	
	public void parse_url() {
		String line = this.url;
		System.out.println(line);
		if(line.indexOf("?") != -1) {
			String[] data = line.split("\\?");
			this.requestURI = data[0];
			while(data.length > 1) {
				this.queryString = data[1];
				try {
					queryString	= URLDecoder.decode(
						queryString,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				data = this.queryString.split("&");
				for(String para: data) {
					String[] arr = para.split("=");
					if(arr.length > 1) {
						this.parameters.put(arr[0], arr[1]);
					}else {
						this.parameters.put(arr[0], null);
					}
				}
			}
		}else {
			this.requestURI = line;
		}
	}

	public void parse_request_header(){
		String line = read_line();
		while(!line.equals("")){
			String[] info= line.split(": ");
			this.headers.put(info[0],info[1]);
			line = read_line();
		}
	}

	public void parse_content(){}
	
	public String get_request_uri(){
		return this.requestURI;
	}
	
	public String get_parameter(String name) {
		return this.parameters.get(name);
	}
	
	
}