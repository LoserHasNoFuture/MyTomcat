package myTomcat.http;

import java.net.Socket;
import java.io.File;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


public class HttpResponse{

	private Socket socket;
	private OutputStream out;
	private File file;
	private String protocol = "HTTP/1.1";
	private String response_code = "200";
	private String status = "OK";
	private HashMap<String,String> headers = new HashMap<String,String>();

	public HttpResponse(){}

	public HttpResponse(Socket socket){
		try{
			this.socket = socket;	
			this.out = socket.getOutputStream();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void setFile(File file){
		this.file = file;
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);
		String type = HttpContext.getContentType(ext);
		headers.put("Content-Type",type);
		headers.put("Content-Length",file.length()+"");
	}


	public void println(String line){
		try{
			this.out.write(line.getBytes("ISO8859-1"));
			out.write(HttpContext.getCR());
			out.write(HttpContext.getLF());			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void send_response_line(){
		String line = this.protocol + " " + this.response_code + " " + this.status;
		println(line);
	}

	public void send_response_header(){
		Set<Entry<String,String>> entrySet = headers.entrySet();
		for(Entry<String,String> entry:entrySet){
			String line = entry.getKey() + ": " + entry.getValue();
			println(line);
		}
		println("");
	}

	public void send_response_content(){
		try(
			FileInputStream fis = new FileInputStream(this.file);
			){
			int len = -1;
			byte[] data = new byte[1024*10];
			while((len = fis.read(data)) != -1){
				this.out.write(data,0,len);
			}				
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void flush(){
		send_response_line();
		send_response_header();
		send_response_content();
	}

	public void setProtocol(String protocol){
		this.protocol = protocol;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public void setResponseCode(String response_code){
		this.response_code = response_code;
	}

	public void setResponseCode(int response_code){
		this.response_code = response_code + "";
	}

	public void addHeader(String key, String value){
		this.headers.put(key,value);
	}
}