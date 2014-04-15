package com.cs.layer3.repository.business.extension.bo;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Entity;

@RelationshipEntity(type = "ItemAttributeInstanceDecorator")
@javax.persistence.Entity
public class ItemAttributeInstanceDecorator extends BusinessObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GraphId
	Long id;

	@OneToOne(fetch=FetchType.LAZY)
	@StartNode
	@Fetch
	Entity entity;

	@OneToOne(fetch=FetchType.LAZY)
	@EndNode
	@Fetch
	AttributeInstance attributeInstance;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public AttributeInstance getAttributeInstance() {
		return attributeInstance;
	}

	public void setAttributeInstance(AttributeInstance attribute) {
		this.attributeInstance = attribute;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}

}
