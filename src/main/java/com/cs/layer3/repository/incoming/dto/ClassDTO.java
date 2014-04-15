package com.cs.layer3.repository.incoming.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer3.repository.business.defalt.bo.Clazz;

public class ClassDTO extends DataTransferObject implements RequestModel {

	private String name;

	private Map<String, AttributeDTO> attributes;

	private List<String> addedAttributes;

	private List<String> deletedAttributes;

	private Map<String, String> updatedClassPropertyValues;

	public ClassDTO() {
		// TODO Auto-generated constructor stub
	}

	public ClassDTO(Long id) {
		this.id = id;
	}

	public List<String> getAddedAttributes() {
		return addedAttributes;
	}

	public void setAddedAttributes(List<String> addedAttributes) {
		this.addedAttributes = addedAttributes;
	}

	public List<String> getDeletedAttributes() {
		return deletedAttributes;
	}

	public void setDeletedAttributes(List<String> deletedAttributes) {
		this.deletedAttributes = deletedAttributes;
	}

	public Map<String, String> getUpdatedClassPropertyValues() {
		return updatedClassPropertyValues;
	}

	public void setUpdatedClassPropertyValues(
			Map<String, String> updatedClassPropertyValues) {
		this.updatedClassPropertyValues = updatedClassPropertyValues;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, AttributeDTO> getAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, AttributeDTO>();
		}
		return attributes;
	}

	public void setAttributes(Map<String, AttributeDTO> attributes) {
		this.attributes = attributes;
	}

	public void setValuesFromClazzModel(Clazz clazz) {
		this.id = clazz.getId();
		this.name = clazz.getClassname();
	}

	public void addAttribute(AttributeDTO attributeBO) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, AttributeDTO>();
		}
		this.attributes.put(attributeBO.getId() + "", attributeBO);
	}

}
