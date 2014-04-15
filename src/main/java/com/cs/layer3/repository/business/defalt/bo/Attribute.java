package com.cs.layer3.repository.business.defalt.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed.Level;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@Entity
@Indexed
public class Attribute extends BusinessObject implements Serializable {

	@Id
	@GraphId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.springframework.data.neo4j.annotation.Indexed
	private Long id;

	@Column
	@Field
	@org.springframework.data.neo4j.annotation.Indexed(indexType = IndexType.FULLTEXT, indexName = "attribute_name_search")
	private String attributeName;

	@Column
	private String type;

	@Column
	private String defaultValue ="";

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{id:" + id + " name: " + attributeName + "  }";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
