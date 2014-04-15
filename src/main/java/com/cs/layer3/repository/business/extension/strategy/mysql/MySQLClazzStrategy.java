package com.cs.layer3.repository.business.extension.strategy.mysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.interfaces.IClazzStrategy;
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer4.persistence.incoming.IConnector;

public class MySQLClazzStrategy implements IClazzStrategy {

	@Autowired
	IConnector connector;

	public void createClazzAttributeRelationship(Clazz clazz,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		List<ClazzAttributeDecorator> clazzAttributeMappings = new ArrayList<ClazzAttributeDecorator>();
		for (Attribute attribute : attributes) {
			ClazzAttributeDecorator clazzAttributeMapping = new ClazzAttributeDecorator();
			clazzAttributeMapping.setAttribute(attribute);
			clazzAttributeMapping.setClazz(clazz);
			connector.save(clazzAttributeMapping);
		}

	}

	@SuppressWarnings("unchecked")
	public ClassDTO getClazzAttributeMappingByClazzId(Long id) {
		List ret = (List) connector.getById(id, ClazzAttributeDecorator.class);
		return null;
		// return (List) connector.getBySelectID(id,
		// ClazzAttributeDecorator.class,"classId");
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ClassDTO save(ClassDTO bObject) {

		Clazz c = (Clazz) ObjectConverter.convertToModel(bObject);
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		c = (Clazz) connector.save(c);
		List<String> attributeIds = bObject.getAddedAttributes();
		if (attributeIds != null && attributeIds.size() > 0) {
			for (String string : attributeIds) {
				attributes.add((Attribute) connector.getById(
						Long.valueOf(string), Attribute.class));
			}
			createClazzAttributeRelationship(c, attributes);
		}
		return (ClassDTO) ObjectConverter.convertToBO(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long classId) {

		Clazz deleteClass = (Clazz) connector.getById(classId, Clazz.class);
		Iterable<ClazzAttributeDecorator> clazzAttIterable = connector
				.getBySelectID(deleteClass.getId() + "",
						ClazzAttributeDecorator.class, "clazz_id");
		if (clazzAttIterable.iterator().hasNext()) {
			for (ClazzAttributeDecorator clazzAttributeDecorator : clazzAttIterable) {
				connector.delete(clazzAttributeDecorator);
			}
		}
		Iterable<ItemClazzAttributeValueDecorator> itemIterable = connector
				.getBySelectID(deleteClass.getId() + "",
						ItemClazzAttributeValueDecorator.class, "clazz_id");
		if (itemIterable.iterator().hasNext()) {
			for (ItemClazzAttributeValueDecorator itemClazzAttributeDecorator : itemIterable) {
				connector.delete(itemClazzAttributeDecorator);
			}
		}
		connector.delete(deleteClass);

	}

	public ClassDTO getById(Long id) {
		return (ClassDTO) ObjectConverter.convertToBO((Clazz) connector
				.getById(id, Clazz.class));

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ClassDTO update(ClassDTO updatedClass) {
		Clazz classToUpdate = (Clazz) connector.getById(updatedClass.getId(),
				Clazz.class);
		if (updatedClass.getUpdatedClassPropertyValues() != null
				&& updatedClass.getUpdatedClassPropertyValues().get("name") != null) {
			classToUpdate.setClassname(updatedClass
					.getUpdatedClassPropertyValues().get("name"));
			classToUpdate = connector.save(classToUpdate);
		}

		if (updatedClass.getDeletedAttributes() != null) {
			for (String deletedId : updatedClass.getDeletedAttributes()) {
				Map<String, String> myMap = new HashMap<String, String>();
				myMap.put("clazz_id", classToUpdate.getId() + "");
				myMap.put("attribute_id", deletedId);
				connector.deleteByCondition(ClazzAttributeDecorator.class,
						myMap);
				connector.deleteByCondition(
						ItemClazzAttributeValueDecorator.class, myMap);
			}
		}

		if (updatedClass.getAddedAttributes() != null) {
			for (String addedIds : updatedClass.getAddedAttributes()) {
				ClazzAttributeDecorator clazzAttributeDecorator = new ClazzAttributeDecorator();
				Attribute attributeToAdd = (Attribute) connector.getById(
						Long.valueOf(addedIds), Attribute.class);
				clazzAttributeDecorator.setAttribute(attributeToAdd);
				clazzAttributeDecorator.setClazz(classToUpdate);
				connector.save(clazzAttributeDecorator);
				Iterable<ItemClazzAttributeValueDecorator> itemIterable = connector
						.getBySelectID(updatedClass.getId() + "",
								ItemClazzAttributeValueDecorator.class,
								"clazz_id");
				for (ItemClazzAttributeValueDecorator itemClazzAttributeValueDecorator : itemIterable) {
					if (itemClazzAttributeValueDecorator.getAttribute() == null) {
						itemClazzAttributeValueDecorator
								.setAttribute(attributeToAdd);
						connector.save(itemClazzAttributeValueDecorator);
					} else {
						ItemClazzAttributeValueDecorator clonedDecorator = itemClazzAttributeValueDecorator
								.cloneDecorator();
						clonedDecorator.setAttribute(attributeToAdd);
						connector.save(clonedDecorator);
					}
				}
			}
		}

		return (ClassDTO) ObjectConverter.convertToBO(classToUpdate);
	}
}
