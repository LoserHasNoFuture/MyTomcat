package myTomcat.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import myTomcat.http.HttpRequest;
import myTomcat.http.HttpResponse;

public class SigninServlet extends HttpServlet {

	public SigninServlet() {}
	
	public void service(HttpRequest request, HttpResponse response) {
		String username = request.get_parameter("username");
		String password = request.get_parameter("password");
		try(
				RandomAccessFile raf
				= new RandomAccessFile(
						"user.dat","rw");
			){
			for(int i = 0; i < raf.length()/100; i++) {
				raf.seek(i*100);
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data,"UTF-8").trim();
				if(name.equals(username)) {
					raf.read(data);
					String pwd = new String(data,"UTF-8").trim();
					if(pwd.equals(password)) {
						forward(request,response,"/bobo_page/signin_sucess.html");
						return;
					}
					break;
				}
			}
			forward(request,response,"/bobo_page/signin_fail.html");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
