package myTomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Webserver{

	private ServerSocket server;
	private ExecutorService threadPool;

	public Webserver(){
		try{
			server = new ServerSocket(3000);
			this.threadPool = Executors.newFixedThreadPool(20);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void start(){
		try{
			while(true){
				Socket socket = server.accept();
				ClientHandler handler = new ClientHandler(socket);
				this.threadPool.execute(handler);
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