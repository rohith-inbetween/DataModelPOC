package com.cs.layer3.repository.business.extension.strategy.neo4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.interfaces.IAttributeStrategy;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer4.persistence.incoming.IConnector;


public class Neo4jAttributeStrategy implements IAttributeStrategy {

	@Autowired
	IConnector connector;

	public AttributeDTO save(AttributeDTO bObject) {
		Attribute attribute = (Attribute)ObjectConverter.convertToModel(bObject);
		attribute = connector.save(attribute);
		return (AttributeDTO)ObjectConverter.convertToBO(attribute);
	}

	public void delete(Long id) {
		
		Attribute attribute = (Attribute)connector.getById(id, Attribute.class);
		connector.delete(attribute);
		
	}

	public AttributeDTO getById(Long id) {
		Attribute attribute = (Attribute)connector.getById(id, Attribute.class);
		return (AttributeDTO)ObjectConverter.convertToBO(attribute);
	}


	public AttributeDTO update(AttributeDTO attribute) {
		
		Attribute attributeToUpdate = (Attribute)connector.getById(attribute.getId(), Attribute.class);
		attributeToUpdate.setAttributeName(attribute.getName());
		connector.save(attributeToUpdate);
		
		
		return null;
	}
	

}
