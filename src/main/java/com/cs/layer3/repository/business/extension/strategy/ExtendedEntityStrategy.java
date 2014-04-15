package com.cs.layer3.repository.business.extension.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.DefaultEntityStrategy;
import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeIdDecorator;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.api.EntityApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer4.persistence.incoming.IConnector;
import com.google.gson.Gson;

@Component
public class ExtendedEntityStrategy extends DefaultEntityStrategy {

	@Autowired
	protected IConnector connector;

	@Autowired
	ClazzApi clazzRepository;

	@Autowired
	AttributeApi attributeRepository;

	@Autowired
	EntityApi entityRepository;

	@Override
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

	@Override
	public void createRelationship(Entity entity,
			List<ClassDTO> classBOs) {
		for (ClassDTO classBusinessObject : classBOs) {
			ClassDTO classBOWithMapping = clazzRepository
					.getClazzAttributeMapping(classBusinessObject.getId());
			Map<String, AttributeDTO> attributeBOs = classBOWithMapping
					.getAttributes();
			if (attributeBOs != null) {
				ArrayList<ItemClazzAttributeIdDecorator> allFacts = new ArrayList<ItemClazzAttributeIdDecorator>();
				Set<String> attributesMapKeySet = attributeBOs.keySet();
				for (String attributeId : attributesMapKeySet) {
					ItemClazzAttributeIdDecorator newFact = new ItemClazzAttributeIdDecorator();
					newFact.setId(entity.getId());
					newFact.setEntityId(entity.getId());
					newFact.setClazzId(classBusinessObject.getId());
					newFact.setAttributeId(Long.valueOf(attributeId));
					allFacts.add(newFact);
				}
				connector.save(allFacts);
			}
		}
	}

	@Override
	public DataTransferObject getEntityMapping(Long id) {
		List<ItemClazzAttributeIdDecorator> clazzAttributeMapping = (List) connector
				.getById(id, ItemClazzAttributeIdDecorator.class);
		System.out.println("Fact with ids : "
				+ new Gson().toJson(clazzAttributeMapping));
		if (clazzAttributeMapping != null && clazzAttributeMapping.size() > 0) {
			EntityDTO entityBO = new EntityDTO();
			for (ItemClazzAttributeIdDecorator itemClassAttribute : clazzAttributeMapping) {
				if (entityBO.getId() == null || entityBO.getName() == null) {
					entityBO = (EntityDTO) entityRepository
							.findOne(itemClassAttribute.getEntityId());
				}
				Map<String, ClassDTO> classes = entityBO
						.getClasses();
				ClassDTO classFromEntityMap = classes
						.get(itemClassAttribute.getClazzId() + "");
				if (classFromEntityMap != null) {
					Map<String, AttributeDTO> attributes = classFromEntityMap
							.getAttributes();
					AttributeDTO attributeBO = (AttributeDTO) attributeRepository
							.findOne(itemClassAttribute.getAttributeId());
					attributes.put(attributeBO.getId() + "", attributeBO);
				} else {
					ClassDTO classBO = clazzRepository
							.findOne(itemClassAttribute.getClazzId());
					Map<String, AttributeDTO> attributes = classBO
							.getAttributes();
					AttributeDTO attributeBO = (AttributeDTO) attributeRepository
							.findOne(itemClassAttribute.getAttributeId());
					attributes.put(attributeBO.getId() + "", attributeBO);
					classBO.setAttributes(attributes);
					classes.put(classBO.getId() + "", classBO);
				}
			}
			return entityBO;
		}
		return null;

	}

	@Override
	public void setEntityAttributeValues(Long id,
			Map<String, String> attributeValues) {
		// TODO Auto-generated method stub
		
	}

}
