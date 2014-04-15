package com.cs.layer3.repository.incoming.dto;

import com.cs.layer2.interactor.request.RequestModel;


public class AttributeDTO extends DataTransferObject implements RequestModel {

	private String name;

	private String value;
	
	private String type;

	public AttributeDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AttributeDTO(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
