package com.cs.layer3.repository.business.defalt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.interfaces.IAttributeStrategy;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer4.persistence.incoming.IConnector;

@Component
public class DefaultAttributeStrategy  implements IAttributeStrategy {

	@Autowired
	protected IConnector connector;
	
	public AttributeDTO save(AttributeDTO bObject) {
		Attribute attribute = (Attribute)ObjectConverter.convertToModel(bObject);
		attribute = connector.save(attribute);
		return (AttributeDTO)ObjectConverter.convertToBO(attribute);
	}

	public void delete(AttributeDTO bObject) {
		// TODO Auto-generated method stub
		
	}

	public AttributeDTO getById(Long id) {
		Attribute attribute = (Attribute)connector.getById(id, Attribute.class);
		return (AttributeDTO)ObjectConverter.convertToBO(attribute);
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	public AttributeDTO update(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public AttributeDTO update(AttributeDTO attribute) {
		// TODO Auto-generated method stub
		return null;
	}
}
