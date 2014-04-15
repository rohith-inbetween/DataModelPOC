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
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@Entity
@Indexed
public class Clazz extends BusinessObject implements Serializable {

	@Id
	@GraphId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.springframework.data.neo4j.annotation.Indexed
	private Long id;

	@Column
	@org.springframework.data.neo4j.annotation.Indexed(indexType=IndexType.FULLTEXT,indexName="class_name_search")
	@Field
	private String classname;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{id:" + id + " name: " + classname + "  }";
	}
}
