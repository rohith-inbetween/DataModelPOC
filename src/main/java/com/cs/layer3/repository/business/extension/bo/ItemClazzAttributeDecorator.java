package com.cs.layer3.repository.business.extension.bo;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;

@javax.persistence.Entity
@RelationshipEntity
public class ItemClazzAttributeDecorator extends BusinessObject implements
		Serializable {

	@Id
	@GraphId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@Fetch
	private Entity entity;

	@OneToOne
	@Fetch
	private Clazz clazz;

	@OneToOne
	@Fetch
	private Attribute attribute;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
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
}
