package com.cs.layer3.repository.business.defalt.interfaces;

import com.cs.layer3.repository.incoming.dto.AttributeDTO;

public interface IAttributeStrategy {

	public AttributeDTO save(AttributeDTO bObject);
	
	public void delete(Long id);

	public AttributeDTO getById(Long id);
	
	public AttributeDTO update(AttributeDTO attribute);
}
