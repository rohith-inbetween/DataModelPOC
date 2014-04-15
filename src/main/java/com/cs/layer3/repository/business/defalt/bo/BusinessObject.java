package com.cs.layer3.repository.business.defalt.bo;

public abstract class BusinessObject {

	public abstract Long getId();
	
	public abstract void setId(Long id);
	
	public Iterable getReferencedList(){
		return null;
	}
}
