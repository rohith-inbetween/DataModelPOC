package com.cs.layer3.repository.business.defalt.bo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
@Entity
@Indexed
public class ClazzMapped extends BusinessObject implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Field
	private String classname;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<Attribute> attributes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void addAttribute(Attribute attribute){
		if(attributes == null){
			attributes = new HashSet<Attribute>();
		}
		attributes.add(attribute);
	}

	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
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
