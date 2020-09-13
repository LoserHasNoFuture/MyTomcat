package myTomcat.servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import myTomcat.http.HttpRequest;
import myTomcat.http.HttpResponse;

public class RegServlet {
	
	
	public RegServlet() {}
	
	
	public void service(HttpRequest request, HttpResponse response) {
		String username = request.get_parameter("username");
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
					File file = new File("./webapps/bobo_page/user_exists.html");
					response.setFile(file);
					return;
				}
			}
			
			add_a_new_user(raf,request);
			
			File file = new File("./webapps/bobo_page/reg_success.html");
			response.setFile(file);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void add_a_new_user(RandomAccessFile raf, HttpRequest request) throws IOException {
		String username = request.get_parameter("username");
		String password = request.get_parameter("password");
		String nickname = request.get_parameter("nickname");
		int age = Integer.parseInt(
				request.get_parameter("age")
			);
		

		raf.seek(raf.length());
		byte[] data = username.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		
		data = password.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		
		data = nickname.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		
		raf.writeInt(age);
		
	
	}
	
}
