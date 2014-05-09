package com.thesis.contextGathering;

import java.net.ServerSocket;
import java.net.Socket;
import com.thesis.contextGathering.common.RequestHandler;

public class ContextSender implements Runnable {
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(Integer.valueOf(Server.PORT));
			System.out.println("Context sender started on port " + Server.PORT);
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				Thread requestHandler = new Thread(new RequestHandler(clientSocket));
				requestHandler.start();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
