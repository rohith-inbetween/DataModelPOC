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
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;

@RelationshipEntity(type="ItemClazzDecorator")
@javax.persistence.Entity
public class ItemClazzDecorator extends BusinessObject {

	@GraphId
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch=FetchType.LAZY)
	@StartNode
	private Entity entity;

	@OneToOne(fetch=FetchType.LAZY)
	@EndNode
	@Fetch
	private Clazz clazz;

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

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub

	}

}
