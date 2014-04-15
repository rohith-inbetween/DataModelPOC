package com.cs.layer3.repository.business.extension.strategy.neo4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.defalt.interfaces.IClazzStrategy;
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzDecorator;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer4.persistence.incoming.IConnector;

public class Neo4jClazzStrategy  implements IClazzStrategy{

	@Autowired
	IConnector connector;
	
	@Autowired
	AttributeApi attributeRepository;
	
	public ClassDTO save(ClassDTO bObject) {
		Clazz clazz = (Clazz)ObjectConverter.convertToModel(bObject);
		clazz = connector.save(clazz);
		Set<String> attributeIds = bObject.getAttributes().keySet();
		if (attributeIds != null && attributeIds.size() > 0) {
			List<AttributeDTO> attributeBOs = new ArrayList<AttributeDTO>();
			if (attributeIds != null) {
				for (String id : attributeIds) {
					attributeBOs
							.add((AttributeDTO) attributeRepository
									.findOne(Long.valueOf(id)));
				}
			}
			createClazzAttributeRelationship(clazz, attributeBOs);
		}
		ClassDTO classBO = (ClassDTO)ObjectConverter.convertToBO(clazz);
		return classBO;
	}
	
	public void createClazzAttributeRelationship(Clazz clazz,
			List<AttributeDTO> attributeBOs) {
		Set<Attribute> attributes = new HashSet<Attribute>();
		for (AttributeDTO attributeBusinessObject : attributeBOs) {
			ClazzAttributeDecorator clazzAttributeDecorator;
			clazzAttributeDecorator = new ClazzAttributeDecorator();
			clazzAttributeDecorator.setClazz(clazz);
			clazzAttributeDecorator.setAttribute((Attribute)ObjectConverter.convertToModel(attributeBusinessObject));
			connector.save(clazzAttributeDecorator);
		}
	}

	public void delete(Long id) {

		Clazz classToDelete = (Clazz)connector.getById(id, Clazz.class);
		connector.delete(classToDelete);
	}

	public ClassDTO getById(Long id) {
		Clazz clazz = (Clazz)connector.getById(id, Clazz.class);
		return (ClassDTO)ObjectConverter.convertToBO(clazz);
	}

	public ClassDTO getClazzAttributeMappingByClazzId(Long id) {
		Iterable<ClazzAttributeDecorator> decoratorIterable = connector.getBySelectID(id + "", ClazzAttributeDecorator.class, "");
		ClassDTO classBO = null;
		
		for (ClazzAttributeDecorator decorator : decoratorIterable) {
			if(classBO == null){
				classBO = (ClassDTO)ObjectConverter.convertToBO(decorator.getClazz());
			}
			classBO.addAttribute((AttributeDTO)ObjectConverter.convertToBO(decorator.getAttribute()));
		}
		return classBO;
	}

	public ClassDTO update(ClassDTO updatedClass) {
		Clazz classToUpdate = (Clazz) connector.getById(updatedClass.getId(),
				Clazz.class);
		if (updatedClass.getUpdatedClassPropertyValues() != null
				&& updatedClass.getUpdatedClassPropertyValues().get("name") != null) {
			classToUpdate.setClassname(updatedClass
					.getUpdatedClassPropertyValues().get("name"));
			classToUpdate =connector.save(classToUpdate);
		}
		
		if(updatedClass.getDeletedAttributes() != null)
		{
			Iterable<ClazzAttributeDecorator> clIterable = connector.getBySelectID(updatedClass.getId() +"", ClazzAttributeDecorator.class, "ClazzAttributeDecorator");
			if(clIterable.iterator().hasNext())
			{
				for (String attString : updatedClass.getDeletedAttributes()) {
					
					for (ClazzAttributeDecorator clazzAttributeDecorator : clIterable) {
						if(clazzAttributeDecorator.getAttribute().getId() == Long.valueOf(attString))
						{
							connector.delete(clazzAttributeDecorator);
						}
					}
					
					Iterable<ItemAttributeValueDecorator> iterableAttIterable = connector.getBySelectID(updatedClass.getId() +"", ItemAttributeValueDecorator.class, "ItemAttributeDecorator");
					for (ItemAttributeValueDecorator itemAttributeDecorator : iterableAttIterable) {
						connector.delete(itemAttributeDecorator);
					}
				}
			}		
			
		}
		
		if(updatedClass.getAddedAttributes() != null)
		{
			for (String attrID : updatedClass.getAddedAttributes()) {
				
				ClazzAttributeDecorator clazzAttributeDecorator = new ClazzAttributeDecorator();
				clazzAttributeDecorator.setAttribute((Attribute)connector.getById(Long.valueOf(attrID), Attribute.class));
				clazzAttributeDecorator.setClazz(classToUpdate);
				connector.save(clazzAttributeDecorator);
			}
		}
		
		Iterable<ItemClazzDecorator> itemCIterable = connector.getBySelectID(classToUpdate.getId()+"", ItemClazzDecorator.class, "ItemClazzDecorator");
		List<Entity> entityList = new ArrayList<Entity>();
		for (ItemClazzDecorator itemClazzDecorator : itemCIterable) {
			entityList.add(itemClazzDecorator.getEntity());
		}
		
		for (String attrID : updatedClass.getAddedAttributes()) {
			for (Entity long1 : entityList) {
				ItemAttributeValueDecorator itemAttributeDecorator = new ItemAttributeValueDecorator();
				itemAttributeDecorator.setClassId(classToUpdate.getId()+"");
				itemAttributeDecorator.setEntity(long1);
				itemAttributeDecorator.setAttribute((Attribute)connector.getById(Long.valueOf(attrID), Attribute.class));
				connector.save(itemAttributeDecorator);
				
			}
		}
		
		return (ClassDTO)ObjectConverter.convertToBO(classToUpdate);
	}

}
