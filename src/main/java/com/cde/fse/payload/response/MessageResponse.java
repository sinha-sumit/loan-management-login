package com.cde.fse.payload.response;

public class MessageResponse {

	private String message;
	private String role;

	public MessageResponse(String message) {
	    this.message = message;
	  }
	
	public MessageResponse(String message, String role) {
	    this.message = message;
	    this.role = role;
	  }
	
	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
