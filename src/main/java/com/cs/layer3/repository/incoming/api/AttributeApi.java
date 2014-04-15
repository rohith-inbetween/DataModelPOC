package com.cs.layer3.repository.incoming.api;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.interfaces.IAttributeStrategy;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;

@Component
public class AttributeApi {

	@Autowired
	IAttributeStrategy attributeStrategy;
	
	@Transactional
	public DataTransferObject save(DataTransferObject attributeBO){
		return attributeStrategy.save((AttributeDTO)attributeBO);
	}
	
	public DataTransferObject findOne(Long id){
		return attributeStrategy.getById(id);
	}
	
	public DataTransferObject update(DataTransferObject attributeBO)
	{
		return attributeStrategy.update((AttributeDTO)attributeBO);
	}
	
	public void delete(Long id)
	{
		attributeStrategy.delete(id);
	}
}
