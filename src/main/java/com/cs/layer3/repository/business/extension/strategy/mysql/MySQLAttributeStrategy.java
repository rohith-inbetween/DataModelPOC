package com.cs.layer3.repository.business.extension.strategy.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.interfaces.IAttributeStrategy;
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer4.persistence.incoming.IConnector;

public class MySQLAttributeStrategy implements IAttributeStrategy {

	@Autowired
	IConnector connector;

	@Transactional(propagation = Propagation.REQUIRED)
	public AttributeDTO save(AttributeDTO bObject) {
		Attribute attribute = (Attribute) ObjectConverter
				.convertToModel(bObject);
		attribute = connector.save(attribute);

		return (AttributeDTO) ObjectConverter.convertToBO(attribute);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long attributeId) {

		Attribute deleteAttribute = (Attribute) connector.getById(attributeId,
				Attribute.class);
		System.out.println(deleteAttribute);
		Iterable<ItemClazzAttributeValueDecorator> itemIterable = connector
				.getBySelectID(deleteAttribute.getId() + "",
						ItemClazzAttributeValueDecorator.class, "attribute_id");
		if (itemIterable.iterator().hasNext()) {
			for (ItemClazzAttributeValueDecorator itemClazzAttributeDecorator : itemIterable) {
				connector.delete(itemClazzAttributeDecorator);
				
			}
		}
		Iterable<ClazzAttributeDecorator> classAttributeDecorater = connector
				.getBySelectID(deleteAttribute.getId() + "",
						ClazzAttributeDecorator.class, "attribute_id");
		if (classAttributeDecorater.iterator().hasNext()) {
			for (ClazzAttributeDecorator clazzAttributeDecorator : classAttributeDecorater) {
				connector.delete(clazzAttributeDecorator);
			}

		}
		System.out.println(deleteAttribute);
		connector.delete(deleteAttribute);
	}

	public AttributeDTO getById(Long id) {

		return (AttributeDTO) ObjectConverter.convertToBO((Attribute) connector
				.getById(id, Attribute.class));

	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public AttributeDTO update(AttributeDTO attribute) {
		Attribute updatedAttribute = (Attribute) ObjectConverter
				.convertToModel(attribute);
		
		Iterable<ClazzAttributeDecorator> classAttributeMapping = connector.getBySelectID(updatedAttribute.getId()+"", ClazzAttributeDecorator.class, "clazz_id");
		for (ClazzAttributeDecorator clazzAttributeDecorator : classAttributeMapping) {
			connector.delete(clazzAttributeDecorator);
		}
		Iterable<ItemClazzAttributeDecorator>itemIterable = connector.getBySelectID(updatedAttribute.getId()+"", ItemClazzAttributeDecorator.class, "attribute_id");
		for (ItemClazzAttributeDecorator itemClazzAttributeDecorator : itemIterable) {
			connector.delete(itemClazzAttributeDecorator);
		}
		return (AttributeDTO) ObjectConverter.convertToBO((Attribute) connector
				.save(updatedAttribute));
	}

}
