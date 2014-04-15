package com.cs.layer2.interactor.response;

public class ObjectResponse implements ResponseModel{

	private Object response;

	public ObjectResponse(Object response) {
		this.response = response;
	}
	
	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
	
	
}
