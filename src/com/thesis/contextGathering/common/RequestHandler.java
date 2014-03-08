package com.thesis.contextGathering.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import com.thesis.contextGathering.DAO.ContextDAO;

public class RequestHandler implements Runnable{
	private Socket clientSocket;
	
	public RequestHandler(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.clientSocket = clientSocket;
	}
	
	public void run() {
		try{
			InputStreamReader streamReader = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);			
			String message = reader.readLine();
			
			Response response = processMessage(message);
			if(response == null){
				response = new Response("400");
				response.setMessage("Bad request!");
			}
			
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			writer.println(response.toXML());
			writer.close();
			clientSocket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private Response processMessage(String message) {
		// TODO Auto-generated method stub
		Response response = null;
		Request request = new Request(message);
		
		ArrayList<ContextItem> contextItems = null;
		ArrayList<String> subjects = request.getSubjects();
		Iterator<String> i = subjects.iterator();
		while(i.hasNext()) {
			String subject = i.next();
			ArrayList<String> predicates = request.getPredicates(subject);
			if(predicates == null || predicates.isEmpty()) {
				contextItems = ContextDAO.read(subject);
			} else {
				contextItems = ContextDAO.read(subject, predicates);
			}
		}
		
		response = new Response("200");
		response.addItems(contextItems);
		
		return response;
	}
	
	
}
