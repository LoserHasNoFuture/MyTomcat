package myTomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Webserver{

	private ServerSocket server;

	public Webserver(){
		try{
			server = new ServerSocket(3000);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void start(){
		try{
			while(true){
				Socket socket = server.accept();
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		Webserver web = new Webserver();
		web.start();
	}
}