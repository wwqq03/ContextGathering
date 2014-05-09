package com.thesis.contextGathering;

import java.io.FileInputStream;
import java.util.Properties;

public class Server{
	public static void main(String[] args){
		Server server = new Server();
		server.go();
	}
	
	public static String MQTT_ADDRESS;
	public static String MQTT_NAME;
	public static String MQTT_SUBSCRIBE;
	
	public static String PORT;
	
	public static String DB_URL;
	public static String DB_USER;
	public static String DB_PASSWORD;
	public static String DB_CONTEXT_ITEM_TABLE;
	
	public void go() {
		
		Properties p = new Properties();
		try{
			FileInputStream inputFile = new FileInputStream("config.txt"); 
			p.load(inputFile);
			MQTT_ADDRESS = p.getProperty("mqtt_address");
			MQTT_NAME = p.getProperty("mqtt_name");
			MQTT_SUBSCRIBE = p.getProperty("mqtt_subscribe");
			
			PORT = p.getProperty("port");
			
			DB_URL = p.getProperty("db_url");
			DB_USER = p.getProperty("db_user");
			DB_PASSWORD = p.getProperty("db_password");
			DB_CONTEXT_ITEM_TABLE = p.getProperty("db_table_context_item");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Thread collectorThread = new Thread(new ContextCollector());
		collectorThread.start();
		
		Thread senderThread = new Thread(new ContextSender());
		senderThread.start();
	}
}
