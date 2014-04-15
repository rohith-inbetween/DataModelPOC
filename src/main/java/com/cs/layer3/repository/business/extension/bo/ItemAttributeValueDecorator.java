package com.cs.layer3.repository.business.extension.bo;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import org.springframework.data.neo4j.support.index.IndexType;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Entity;

@RelationshipEntity(type = "ItemAttributeValueDecorator")
public class ItemAttributeValueDecorator extends BusinessObject {

	@GraphId
	Long id;

	@StartNode
	@Fetch
	Entity entity;

	@EndNode
	@Fetch
	Attribute attribute;

	@Indexed(indexName="item_attribute_value",indexType=IndexType.FULLTEXT)
	String value;

	String classId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
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
