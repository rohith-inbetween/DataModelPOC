package com.cs.layer3.repository.business.extension.bo;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;

@javax.persistence.Entity
public class ItemClazzAttributeValueDecorator extends BusinessObject implements
		Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch=FetchType.LAZY)
	private Entity entity;

	@OneToOne(fetch=FetchType.LAZY)
	private Clazz clazz;

	@OneToOne(fetch=FetchType.LAZY)
	private Attribute attribute;

	@OneToOne(fetch=FetchType.LAZY)
	private Value value;

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}
	
	public ItemClazzAttributeValueDecorator cloneDecorator(){
		ItemClazzAttributeValueDecorator clonedDecorator = new ItemClazzAttributeValueDecorator();
		clonedDecorator.setId(id);
		clonedDecorator.setEntity(entity);
		clonedDecorator.setClazz(clazz);
		clonedDecorator.setAttribute(attribute);
		return clonedDecorator;
	}
}
