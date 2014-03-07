package com.thesis.contextGathering;

public class Server{
	public static void main(String[] args){
		Server server = new Server();
		server.go();
	}
	
	public void go() {
		Thread collectorThread = new Thread(new ContextCollector());
		collectorThread.start();
		
		Thread senderThread = new Thread(new ContextSender());
		senderThread.start();
	}
}
