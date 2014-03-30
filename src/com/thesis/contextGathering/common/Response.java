package com.thesis.contextGathering.common;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Response {

	private String status;
	private ArrayList<ContextItem> items;
	private String message;
	
	public Response(String status) {
		this.status = status;
		if(status.equals("200")) {
			items = new ArrayList<ContextItem>();
		}
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addItem(ContextItem contextItem) {
		if(status != "200") {
			return;
		}
		
		items.add(contextItem);
	}
	
	public void addItems(ArrayList<ContextItem> contextItems) {
		if(status != "200") {
			return;
		}
		
		items.addAll(contextItems);
	}
	
	public String toXML() {
		String xml = null;
		
		try{
			Document document = DocumentHelper.createDocument();
            Element responseElement = document.addElement("response");
            responseElement.addAttribute("status", status);
            
            if(status.equals("200")){
            	if(items != null && !items.isEmpty()) {
            		Iterator<ContextItem> i = items.iterator();
            		while(i.hasNext()) {
            			ContextItem context = i.next();
            			if(context == null) {
            				continue;
            			}
            			Element itemElement = responseElement.addElement("item");
            			itemElement.addAttribute("subject", context.getSubject());
            			Element predicateElement = itemElement.addElement("predicate");
            			predicateElement.setText(context.getPredicate());
            			Element objectElement = itemElement.addElement("object");
            			objectElement.setText(context.getObject());
            		}
            	}
            }
            else if(message != null) {
            	Element messageElement = responseElement.addElement("message");
            	messageElement.setText(message);
            }
            
            xml = responseElement.asXML();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return xml;
	}
}
