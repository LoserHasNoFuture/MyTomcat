package myTomcat.servlet;

import java.io.File;

import myTomcat.http.HttpRequest;
import myTomcat.http.HttpResponse;

public abstract class HttpServlet {
	public abstract void service(HttpRequest request, HttpResponse response);
	
	public void forward(HttpRequest request, HttpResponse response, String path) {
		File file = new File("./webapps" + path);
		response.setFile(file);
	}
}
