package io.helidon.service.impl;

import java.util.UUID;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;



public final class Ticket {
	private final String id;
	private final String subject;
	private final String summary;
	private final String customer;
	private final String customerId;
	private final String partner;
	private final String partnerId;	
	private final String product;
	private final String status;
	private final String creationDate;
	
	private Ticket(String id, String subject, String summary, String customer, String customerId,
			String partner, String partnerId, String product, String status, String creationDate) {
		this.id = id;
		this.subject = subject;
		this.summary = summary;
		this.customer = customer;
		this.customerId = customerId;
		this.partner = partner;
		this.partnerId = partnerId;
		this.product = product;
		this.status = status;
		this.creationDate = creationDate;
	}
	
	@JsonbCreator
	public static Ticket of(@JsonbProperty("id") String id, @JsonbProperty("subject") String subject,
	        @JsonbProperty("summary") String summary, @JsonbProperty("customer") String customer,
	        @JsonbProperty("customerId") String customerId, @JsonbProperty("partner") String partner,
	        @JsonbProperty("partnerId") String partnerId, @JsonbProperty("product") String product, 
	        @JsonbProperty("status") String status, @JsonbProperty("creationDate") String creationDate) {
	        if (id == null || id.trim().equals("")) {
	            id = UUID.randomUUID().toString();	            
	        }
	        Ticket t = new Ticket(id, subject, summary, customer, customerId, partner, partnerId, product, status, creationDate);
	        return t;
	}	
	
	public String getId() {
		return id;
	}
	public String getSubject() {
		return subject;
	}
	public String getSummary() {
		return summary;
	}
	public String getCustomer() {
		return customer;
	}
	public String getCustomerId() {
		return customerId;
	}
	public String getPartner() {
		return partner;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public String getProduct() {
		return product;
	}
	public String getStatus() {
		return status;
	}
	public String getCreationDate() {
		return creationDate;
	}
	
}
