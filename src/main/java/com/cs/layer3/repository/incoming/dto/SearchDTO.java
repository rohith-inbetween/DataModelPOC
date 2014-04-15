package com.cs.layer3.repository.incoming.dto;

import java.util.Map;

import com.cs.layer2.interactor.request.RequestModel;

public class SearchDTO implements RequestModel {

	private Map<String, ConditionParam> conditions;

	private String attributeName;

	private String value;

	public Map<String, ConditionParam> getConditions() {
		return conditions;
	}

	public void setConditions(Map<String, ConditionParam> conditions) {
		this.conditions = conditions;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
