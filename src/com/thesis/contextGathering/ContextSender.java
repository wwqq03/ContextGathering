package com.thesis.contextGathering;

import java.net.ServerSocket;
import java.net.Socket;
import com.thesis.contextGathering.common.RequestHandler;

public class ContextSender implements Runnable {
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(7979);
			System.out.println("Context sender started on port 7979");
			
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
