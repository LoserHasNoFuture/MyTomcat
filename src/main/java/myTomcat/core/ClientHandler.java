package myTomcat.core;


import java.net.Socket;
import java.io.File;

import myTomcat.http.EmptyRequestException;
import myTomcat.http.HttpRequest;
import myTomcat.http.HttpResponse;
import myTomcat.servlet.HttpServlet;
import myTomcat.servlet.RegServlet;
import myTomcat.servlet.SigninServlet;


public class ClientHandler implements Runnable{
	private Socket socket;

	public ClientHandler(){}

	public ClientHandler(Socket socket){
		this.socket = socket;
	}

	public void run(){
		try{
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			String path = request.get_request_uri();
			HttpServlet servlet = ServletContext.getServlet(path);
			
			if(servlet != null) {
				servlet.service(request,response);
			}else{
				File file = new File("./webapps"+path);
					
				if(file.exists()){
					response.setFile(file);
				}else{
					path = "/common/404.html";
					file = new File("./webapps"+path);
					response.setFile(file);
					// set response line
					response.setProtocol("HTTP/1.1");
					response.setResponseCode(404);
					response.setStatus("NOT FOUND");
				}
			}
			response.flush();
			
		}catch(EmptyRequestException e) {
//			do nothing;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

}