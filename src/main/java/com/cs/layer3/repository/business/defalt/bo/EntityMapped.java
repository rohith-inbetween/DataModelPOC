package com.cs.layer3.repository.business.defalt.bo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.cs.layer3.repository.business.extension.bo.Value;

@NodeEntity
@javax.persistence.Entity
@Indexed
public class EntityMapped extends BusinessObject implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Field
	private String EntityName;

	@OneToOne
	private Entity parentEntity;

	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	private Set<ClazzMapped> classes = new HashSet<ClazzMapped>();

	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	private Map<Attribute, Value> attributeValues = new HashMap<Attribute, Value>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addAttributeValueMapping(Attribute attribute, Value value) {
		if (attributeValues == null) {
			attributeValues = new HashMap<Attribute, Value>();
		}
		attributeValues.put(attribute, value);
	}

	public Map<Attribute, Value> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(Map<Attribute, Value> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public void addClass(ClazzMapped clazz) {
		if (classes == null) {
			classes = new HashSet<ClazzMapped>();
		}
		classes.add(clazz);
	}

	public void addClasses(Collection<ClazzMapped> clazzes) {
		if (classes == null) {
			classes = new HashSet<ClazzMapped>();
		}
		classes.addAll(clazzes);
	}

	public Entity getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(Entity parentEntity) {
		this.parentEntity = parentEntity;
	}

	public Set<ClazzMapped> getClasses() {
		return classes;
	}

	public void setClasses(Set<ClazzMapped> classes) {
		this.classes = classes;
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
