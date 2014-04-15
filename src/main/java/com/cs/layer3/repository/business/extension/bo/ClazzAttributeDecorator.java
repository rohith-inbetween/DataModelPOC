package com.cs.layer3.repository.business.extension.bo;

import java.io.Serializable;

import javax.persistence.Entity;
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

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Clazz;

@Entity
@RelationshipEntity(type="ClazzAttributeDecorator")
public class ClazzAttributeDecorator extends BusinessObject implements
		Serializable {

	@GraphId
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	Long id;
	
	@StartNode
	@Fetch
	@OneToOne(fetch=FetchType.LAZY)
	Clazz clazz;

	@EndNode
	@Fetch
	@OneToOne(fetch=FetchType.LAZY)
	Attribute attribute;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	/*public Set<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}*/
	
	/*public void addAttributes(Set<Attribute> attributes){
	this.attributes.addAll(attributes);
}*/

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Clazz getClazz() {
		return this.clazz;
	}
}
