package com.thesis.contextGathering.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Request {
	
	private HashMap<String, ArrayList<String>> items;
	
	public Request(String plainRequest) {
		if(plainRequest == null) {
			return;
		}
		
		try {
			items = new HashMap<String, ArrayList<String>>();
			
			Document document = DocumentHelper.parseText(plainRequest);
			Element requestElement = document.getRootElement();
			
			ArrayList<Element> itemElements = new ArrayList<Element>(requestElement.elements("item"));
			Iterator<Element> iitem = itemElements.iterator();
			while(iitem.hasNext()) {
				Element itemElement = iitem.next();
				String subject = itemElement.attributeValue("subject");
				if(subject == null || subject.isEmpty()) {
					continue;
				}
				
				ArrayList<String> predicates = new ArrayList<String>();
				ArrayList<Element> predicateElements = new ArrayList<Element>(itemElement.elements("predicate"));
				Iterator<Element> ipredicate = predicateElements.iterator();
				while(ipredicate.hasNext()) {
					Element predicateElement = ipredicate.next();
					String predicate = predicateElement.getText();
					if(predicate == null || predicate.isEmpty()) {
						continue;
					}
					predicates.add(predicate);
				}
				
				addItem(subject, predicates);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getSubjects() {
		if(items == null || items.isEmpty()) {
			return null;
		}
		
		ArrayList<String> subjects = new ArrayList<String>();
		Iterator<Map.Entry<String, ArrayList<String>>> i = items.entrySet().iterator();
		while(i.hasNext()) {
			Map.Entry<String, ArrayList<String>> item = i.next();
			String subject = item.getKey();
			if(subject != null && !subject.isEmpty()) {
				subjects.add(subject);
			}
		}
		return subjects;
	}
	
	public ArrayList<String> getPredicates(String subject) {
		if(subject == null || subject.isEmpty()) {
			return null;
		}
		
		return items.get(subject);
	}
	
	public void addItem(String subject, ArrayList<String> predicates) {
		if(subject != null && predicates != null) {
			items.put(subject, predicates);
		}
	}
}
