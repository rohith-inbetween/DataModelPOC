package com.cs.layer3.repository.business.defalt.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@javax.persistence.Entity
@Indexed
public class Entity extends BusinessObject implements Serializable {

	@Id
	@GraphId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.springframework.data.neo4j.annotation.Indexed
	private Long id;

	@Column
	@Field
	@org.springframework.data.neo4j.annotation.Indexed(indexType=IndexType.FULLTEXT,indexName="entity_name_search")
	private String EntityName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityName() {
		return EntityName;
	}

	public void setEntityName(String entityName) {
		EntityName = entityName;
	}
	
	@Override
	public String toString() {
		return "{id:" + id + " name: " + EntityName + "  }";
	}

}
