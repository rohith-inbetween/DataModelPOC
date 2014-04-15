package com.cs.layer3.repository.business.extension.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.neo4j.support.index.IndexType;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;

@Entity
@Indexed
public class Value extends BusinessObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.springframework.data.neo4j.annotation.Indexed
	Long id;
	
	@Column
	@Field
	@org.springframework.data.neo4j.annotation.Indexed(indexType=IndexType.FULLTEXT,indexName="attribute_value_search")
	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "{value : \"" + value + "\"}";
	}

}
