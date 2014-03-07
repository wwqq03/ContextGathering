package com.thesis.contextGathering;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.thesis.contextGathering.DAO.ContextDAO;
import com.thesis.contextGathering.common.ContextItem;

public class ContextCollector implements Runnable{
	
	private ContextDAO contextDAO = new ContextDAO();
	
	public void run() {
		MqttClient client;
		try {
		      client = new MqttClient("tcp://localhost:1883", "server");
		      client.setCallback(new MqttCallback() {
		    	  
	                @Override
	                public void connectionLost(Throwable cause) {
	                    System.out.println("Connection lost, Trying to reconnect");
	                    
	                }
	 
	                @Override
	                public void deliveryComplete(IMqttDeliveryToken token) {
	                	
	                }
	 
	                @Override
	                public void messageArrived(String topicName, MqttMessage message)
	                        throws Exception {
	                	String payload = new String(message.getPayload());
	                    System.out.println("[Received publish] " + payload);
	                    ContextItem newItem = generateContextItem(payload);
	                    
	                    if(newItem != null) {
	                    	contextDAO.store(newItem);
	                    }
	                }
	                
	          });
		      client.connect();
		      client.subscribe("location");
		      /*ArrayList<String> list = getSubscribeList();
		      if(list == null) {
		    	  System.out.println("Failed in getting subscribe list");
		    	  client.disconnect();
		    	  return;
		      }
		      Iterator<String> i = list.iterator();
		      while(i.hasNext()) {
		    	  client.subscribe(i.next());
		      }*/
		      
		      System.out.println("Context collector started");
		      
		} catch (Exception e) {
		      e.printStackTrace();
		}
	}
	
	private ArrayList<String> getSubscribeList() {
		return null;
	}
	
	private ContextItem generateContextItem(String message) {
		if(message == null || message.isEmpty()) {
			return null;
		}
		
		String[] parts = message.split(" ");
		if(parts.length != 3) {
			return null;
		}
		
		return new ContextItem(parts[0], parts[1], parts[2]);
	}
}
