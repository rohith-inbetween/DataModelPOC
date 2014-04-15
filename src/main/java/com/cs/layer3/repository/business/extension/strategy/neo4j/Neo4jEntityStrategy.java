package com.cs.layer3.repository.business.extension.strategy.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.defalt.interfaces.IEntityStrategy;
import com.cs.layer3.repository.business.extension.bo.ItemAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzDecorator;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.ConditionParam;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;
import com.cs.layer4.persistence.incoming.IConnector;
import com.cs.layer4.persistence.query.NestedQuery;

public class Neo4jEntityStrategy implements IEntityStrategy {

	@Autowired
	IConnector connector;

	@Autowired
	ClazzApi clazzRepository;

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

	public void createRelationship(Entity entity, List<ClassDTO> classBOs) {
		for (ClassDTO classBusinessObject : classBOs) {
			ClassDTO classBOMapping = clazzRepository
					.getClazzAttributeMapping(classBusinessObject.getId());
			Iterable<String> iter = classBOMapping.getAttributes().keySet();
			for (String attributeId : iter) {
				Attribute attr = (Attribute) ObjectConverter
						.convertToModel(classBOMapping.getAttributes().get(
								attributeId));
				ItemAttributeValueDecorator itemAttributeDecorator = new ItemAttributeValueDecorator();
				itemAttributeDecorator.setEntity(entity);
				itemAttributeDecorator.setAttribute(attr);
				itemAttributeDecorator.setClassId(classBusinessObject.getId()
						+ "");
				itemAttributeDecorator = connector.save(itemAttributeDecorator);
			}
			ItemClazzDecorator itemClazzDecorator = new ItemClazzDecorator();
			itemClazzDecorator.setEntity(entity);
			itemClazzDecorator.setClazz((Clazz) ObjectConverter
					.convertToModel(classBusinessObject));
			itemClazzDecorator = connector.save(itemClazzDecorator);
		}
	}

	public void delete(Long entityId) {
		Entity entity = (Entity) connector.getById(entityId, Entity.class);
		connector.delete(entity);
	}

	public EntityDTO getById(Long id) {
		Entity entity = (Entity) connector.getById(id, Entity.class);
		return (EntityDTO) ObjectConverter.convertToBO(entity);
	}

	@SuppressWarnings("unchecked")
	public DataTransferObject getEntityMapping(Long id) {

		EntityDTO entityBO = null;
		Iterable<ItemAttributeValueDecorator> itemAttributeMappings = connector
				.getBySelectID(id + "", ItemAttributeValueDecorator.class,
						"ItemAttributeDecorator");
		Map<String, Map<String, AttributeDTO>> clazzAttributeMap = new HashMap<String, Map<String, AttributeDTO>>();
		for (ItemAttributeValueDecorator itemAttributeDecorator : itemAttributeMappings) {
			if (entityBO == null) {
				entityBO = (EntityDTO) ObjectConverter
						.convertToBO(itemAttributeDecorator.getEntity());
			}
			Attribute attribute = itemAttributeDecorator.getAttribute();
			Map<String, AttributeDTO> attributeBOsListMap = clazzAttributeMap
					.get(itemAttributeDecorator.getClassId());
			if (attributeBOsListMap == null) {
				attributeBOsListMap = new HashMap<String, AttributeDTO>();
			}
			AttributeDTO attributeBO = (AttributeDTO) ObjectConverter
					.convertToBO(attribute);
			attributeBO.setValue(itemAttributeDecorator.getValue());
			attributeBOsListMap.put(attribute.getId() + "", attributeBO);
			clazzAttributeMap.put(itemAttributeDecorator.getClassId(),
					attributeBOsListMap);
		}
		Iterable<ItemClazzDecorator> itemClazzMappings = connector
				.getBySelectID(id + "", ItemClazzDecorator.class,
						"ItemClazzDecorator");
		entityBO.setClasses(new HashMap<String, ClassDTO>());
		Map<String, ClassDTO> classBOsMap = entityBO.getClasses();
		for (ItemClazzDecorator itemClazzDecorator : itemClazzMappings) {
			Clazz clazz = itemClazzDecorator.getClazz();
			ClassDTO classBO = (ClassDTO) ObjectConverter.convertToBO(clazz);
			classBO.setAttributes(clazzAttributeMap.get(classBO.getId() + ""));
			classBOsMap.put(clazz.getId() + "", classBO);
		}
		return entityBO;
	}

	@SuppressWarnings("unchecked")
	public void setEntityAttributeValues(Long id,
			Map<String, String> attributeValues) {
		EntityDTO entityBO = null;
		Iterable<ItemAttributeValueDecorator> itemAttributeMappings = connector
				.getBySelectID(id + "", ItemAttributeValueDecorator.class,
						"ItemAttributeDecorator");
		for (ItemAttributeValueDecorator itemAttributeDecorator : itemAttributeMappings) {
			Attribute attribute = itemAttributeDecorator.getAttribute();
			String attributeValue = attributeValues.get(attribute.getId() + "");
			if (attributeValue != null) {
				itemAttributeDecorator.setValue(attributeValue);
				connector.save(itemAttributeDecorator);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Set<EntityDTO> search(SearchDTO searchCriteria) {/*
		
		NestedQuery query = new NestedQuery();
		query.setTargetTableName(ItemAttributeDecorator.class
				.getSimpleName());
		Map<String, List<ConditionParam>> conditions = searchCriteria
				.getConditions();
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
		for (String tableName : conditions.keySet()) {
				List<ConditionParam> conditionParams = conditions
						.get(tableName);
				Map<String, String> tableConditions = new HashMap<String, String>();
				for (ConditionParam conditionParam : conditionParams) {
					tableConditions.put(conditionParam.getLhs(),
							conditionParam.getRhs());
				}
				resultMap.put(tableName, tableConditions);
		}
		query.setSubTableNames(resultMap);
		Iterable<ItemAttributeDecorator> result = connector
				.searchNested(query, ItemAttributeDecorator.class);
		Set<EntityDTO> eligibleEntities = new HashSet<EntityDTO>();
		for (ItemAttributeDecorator itemAttributeDecorator : result) {
			eligibleEntities.add((EntityDTO) ObjectConverter
					.convertToBO(itemAttributeDecorator.getEntity()));
		}
		return eligibleEntities;		
	*/
		return null;
	}

	@SuppressWarnings("unchecked")
	public EntityDTO update(EntityDTO entityToUpdate) {

		Entity updatedEntity = (Entity) connector.getById(
				entityToUpdate.getId(), Entity.class);
		if (entityToUpdate.getUpdatedEntityPropertyValues() != null) {
			for (String element : entityToUpdate
					.getUpdatedEntityPropertyValues().keySet()) {
				if (entityToUpdate.getUpdatedEntityPropertyValues()
						.get(element) != null && element.equals("name")) {
					updatedEntity.setEntityName(entityToUpdate
							.getUpdatedEntityPropertyValues().get(element));
					updatedEntity = connector.save(updatedEntity);
				}
			}
		}

		Iterable<ItemAttributeValueDecorator> itemAttIterable = connector
				.getBySelectID(updatedEntity.getId() + "",
						ItemAttributeValueDecorator.class, "ItemAttributeDecorator");
		if (entityToUpdate.getDeletedClasses().iterator().hasNext()) {
			for (String classID : entityToUpdate.getDeletedClasses()) {
				Iterable<ItemClazzDecorator> itemClIterable = connector
						.getBySelectID(classID, ItemClazzDecorator.class,
								"ItemClazzDecorator");
				for (ItemClazzDecorator itemClazzDecorator : itemClIterable) {
					if (itemClazzDecorator.getEntity().getId()
							.equals(updatedEntity.getId())) {
						connector.delete(itemClazzDecorator);
					}
				}

				for (ItemAttributeValueDecorator itemAttributeDecorator : itemAttIterable) {

					if (String.valueOf(itemAttributeDecorator.getClassId())
							.equals(classID)) {
						connector.delete(itemAttributeDecorator);
					}
				}

			}
		}

		if (entityToUpdate.getAddedClasses().iterator().hasNext()) {
			List<ClassDTO> classDTOs = new ArrayList<ClassDTO>();
			for (String classID : entityToUpdate.getAddedClasses()) {

				classDTOs.add((ClassDTO) ObjectConverter
						.convertToBO((Clazz) connector.getById(
								Long.valueOf(classID), Clazz.class)));

			}
			createRelationship(updatedEntity, classDTOs);

		}

		return (EntityDTO) ObjectConverter.convertToBO(updatedEntity);

	}

	@Override
	public void setParent(Long parentId, Long childId) {
		// TODO Auto-generated method stub
		
	}
}
