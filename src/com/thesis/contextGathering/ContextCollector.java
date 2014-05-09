package com.thesis.contextGathering;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.thesis.contextGathering.DAO.ContextDAO;
import com.thesis.contextGathering.common.ContextItem;

public class ContextCollector implements Runnable{
	
	public void run() {
		MqttClient client;
		try {
		      client = new MqttClient(Server.MQTT_ADDRESS, Server.MQTT_NAME);
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
	                    	ContextDAO.store(newItem);
	                    }
	                }
	                
	          });
		      client.connect();
		      /*client.subscribe("location");
		      client.subscribe("patientSensor");
		      client.subscribe("epr");*/
		      ArrayList<String> list = getSubscribeList();
		      if(list == null) {
		    	  System.out.println("Failed in getting subscribe list");
		    	  client.disconnect();
		    	  return;
		      }
		      Iterator<String> i = list.iterator();
		      while(i.hasNext()) {
		    	  client.subscribe(i.next());
		      }
		      
		      System.out.println("Context collector started");
		      
		} catch (Exception e) {
		      e.printStackTrace();
		}
	}
	
	private ArrayList<String> getSubscribeList() {
		String subscribe = Server.MQTT_SUBSCRIBE;
		String[] subscribeList = subscribe.split(";");
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < subscribeList.length; i++) {
			result.add(subscribeList[i]);
		}
		return result;
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
