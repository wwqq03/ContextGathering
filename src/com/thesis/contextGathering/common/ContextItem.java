package com.thesis.contextGathering.common;

public class ContextItem {
	
	private int id;
	private String subject;
	private String predicate;
	private String object;
	
	public ContextItem(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "ContextItem [id=" + id + ", subject=" + subject
				+ ", predicate=" + predicate + ", object=" + object + "]";
	}
}
