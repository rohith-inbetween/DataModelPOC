package com.cs.layer3.repository.business.extension.bo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Entity;

@javax.persistence.Entity
public class InheritanceDecorator extends BusinessObject{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Entity parent;
	
	@OneToOne
	private Entity child;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public Entity getChild() {
		return child;
	}

	public void setChild(Entity child) {
		this.child = child;
	}
	
	
	
}
