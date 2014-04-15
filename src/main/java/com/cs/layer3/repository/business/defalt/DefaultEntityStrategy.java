package com.cs.layer3.repository.business.defalt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.defalt.interfaces.IEntityStrategy;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeDecorator;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.api.EntityApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;
import com.cs.layer4.persistence.incoming.IConnector;
import com.google.gson.Gson;

@Component
public class DefaultEntityStrategy implements IEntityStrategy {

	@Autowired
	protected IConnector connector;

	@Autowired
	ClazzApi clazzRepository;

	@Autowired
	AttributeApi attributeRepository;

	@Autowired
	EntityApi entityRepository;

	public EntityDTO save(EntityDTO bObject) {
		Entity entity = (Entity) ObjectConverter.convertToModel(bObject);
		entity = connector.save(entity);
		Set<String> classIds = bObject.getClasses().keySet();
		if (classIds != null && classIds.size() > 0) {
			List<ClassDTO> classBOs = new ArrayList<ClassDTO>();
			for (String clazzId : classIds) {
				classBOs.add(clazzRepository.findOne(Long.valueOf(clazzId)));
			}
			createRelationship(entity, classBOs);
		}
		return (EntityDTO) ObjectConverter.convertToBO(entity);
	}

	public void createRelationship(Entity entity,
			List<ClassDTO> classBOs) {
		for (ClassDTO classBusinessObject : classBOs) {
			ClassDTO classBOWithMapping = clazzRepository
					.getClazzAttributeMapping(classBusinessObject.getId());
			Map<String, AttributeDTO> attributeBOs = classBOWithMapping
					.getAttributes();
			if (attributeBOs != null) {
				Clazz clazz = (Clazz) ObjectConverter
						.convertToModel(classBusinessObject);
				ArrayList<ItemClazzAttributeDecorator> allFacts = new ArrayList<ItemClazzAttributeDecorator>();
				Set<String> attributesMapKeySet = attributeBOs.keySet();
				for (String attributeId : attributesMapKeySet) {
					ItemClazzAttributeDecorator newFact = new ItemClazzAttributeDecorator();
					Attribute attribute = (Attribute)ObjectConverter.convertToModel(attributeBOs.get(attributeId));
					newFact.setId(entity.getId());
					newFact.setEntity(entity);
					newFact.setClazz(clazz);
					newFact.setAttribute(attribute);
					allFacts.add(newFact);
				}
				connector.save(allFacts);
			}
		}
	}

	public void delete(Long entityId) {
		// TODO Auto-generated method stub
	}

	public EntityDTO getById(Long id) {
		Entity entity = (Entity) connector.getById(id, Entity.class);
		return (EntityDTO) ObjectConverter.convertToBO(entity);
	}

	@SuppressWarnings("unchecked")
	public DataTransferObject getEntityMapping(Long id) {
		List<ItemClazzAttributeDecorator> itemClazzAttributeMapping = (List<ItemClazzAttributeDecorator>) connector
				.getById(id, ItemClazzAttributeDecorator.class);
		System.out.println("Fact with objects : "
				+ new Gson().toJson(itemClazzAttributeMapping));
		if (itemClazzAttributeMapping != null && itemClazzAttributeMapping.size() > 0) {
			EntityDTO entityBO = new EntityDTO();
			Map<String, ClassDTO> classes = entityBO.getClasses();
			for (ItemClazzAttributeDecorator itemClassAttribute : itemClazzAttributeMapping) {
				if (entityBO.getId() == null || entityBO.getName() == null) {
					entityBO.setValuesFromEntityModel(itemClassAttribute
							.getEntity());
				}
				Clazz clazz = itemClassAttribute.getClazz();
				ClassDTO classFromEntityMap = classes.get(clazz
						.getId() + "");
				if (classFromEntityMap != null) {
					Map<String, AttributeDTO> attributes = classFromEntityMap
							.getAttributes();
					AttributeDTO attributeBO = (AttributeDTO) ObjectConverter
							.convertToBO(itemClassAttribute.getAttribute());
					attributeBO.setValue(itemClassAttribute.getValue());
					attributes.put(attributeBO.getId() + "", attributeBO);
				} else {
					ClassDTO classBO = new ClassDTO();
					classBO.setValuesFromClazzModel(clazz);
					Map<String, AttributeDTO> attributes = classBO
							.getAttributes();
					AttributeDTO attributeBO = (AttributeDTO) ObjectConverter
							.convertToBO(itemClassAttribute.getAttribute());
					attributeBO.setValue(itemClassAttribute.getValue());
					attributes.put(attributeBO.getId() + "", attributeBO);
					classBO.setAttributes(attributes);
					classes.put(classBO.getId() + "", classBO);
				}
			}
			return entityBO;
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public void setEntityAttributeValues(Long id,
			Map<String, String> attributeValues) {
		List<ItemClazzAttributeDecorator> clazzAttributeMapping = (List<ItemClazzAttributeDecorator>) connector
				.getById(id, ItemClazzAttributeDecorator.class);
		for (ItemClazzAttributeDecorator itemClazzAttributeDecorator : clazzAttributeMapping) {
			Attribute attribute = itemClazzAttributeDecorator.getAttribute();
			String attributeValue = attributeValues.get(attribute.getId() + "");
			
			if(attributeValue != null){
				itemClazzAttributeDecorator.setValue(attributeValue);
			}
		}
	}

	public Set<EntityDTO> search(SearchDTO searchCriteria) {
		return null;
		
	}
	public EntityDTO update(EntityDTO entityToUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Long parentId, Long childId) {
		// TODO Auto-generated method stub
		
	}
}
