package com.cs.layer3.repository.incoming.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer3.repository.business.defalt.bo.Entity;

public class EntityDTO extends DataTransferObject implements RequestModel {

	private String name;

	private String parentEntity;

	private Map<String, ClassDTO> classes;

	private Map<String, String> attributeValues;

	private List<String> addedClasses;

	private List<String> deletedClasses;

	private Map<String, String> updatedEntityPropertyValues;

	private Map<String, String> updatedClassAttributeValues;

	public Map<String, String> getAttributeValues() {
		return attributeValues;
	}
	
	public void addAttributeValue(String attribute, String value){
		if(attributeValues == null){
			attributeValues = new HashMap<String,String>();
		}
		attributeValues.put(attribute, value);
	}

	public void setAttributeValues(Map<String, String> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public String getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}

	public List<String> getAddedClasses() {
		return addedClasses;
	}

	public void setAddedClasses(List<String> addedClasses) {
		this.addedClasses = addedClasses;
	}

	public List<String> getDeletedClasses() {
		return deletedClasses;
	}

	public void setDeletedClasses(List<String> deletedClasses) {
		this.deletedClasses = deletedClasses;
	}

	public Map<String, String> getUpdatedEntityPropertyValues() {
		return updatedEntityPropertyValues;
	}

	public void setUpdatedEntityPropertyValues(
			Map<String, String> updatedEntityPropertyValues) {
		this.updatedEntityPropertyValues = updatedEntityPropertyValues;
	}

	public Map<String, String> getUpdatedClassAttributeValues() {
		return updatedClassAttributeValues;
	}

	public void setUpdatedClassAttributeValues(
			Map<String, String> updatedClassAttributeValues) {
		this.updatedClassAttributeValues = updatedClassAttributeValues;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, ClassDTO> getClasses() {
		if (this.classes == null) {
			this.classes = new HashMap<String, ClassDTO>();
		}
		return classes;
	}

	public void setClasses(Map<String, ClassDTO> classes) {
		this.classes = classes;
	}

	public void setValuesFromEntityModel(Entity entity) {
		this.id = entity.getId();
		this.name = entity.getEntityName();
	}

	public void addClass(ClassDTO classBO) {
		if (this.classes == null) {
			this.classes = new HashMap<String, ClassDTO>();
		}
		this.classes.put(classBO.getId() + "", classBO);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityDTO) {
			EntityDTO entityDTO = (EntityDTO) obj;
			return entityDTO.getId().equals(this.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
