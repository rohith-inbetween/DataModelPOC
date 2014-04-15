package com.cs.layer3.repository.business.extension.strategy.mysqlnormalized;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.search.FullTextSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cs.layer3.repository.business.defalt.ObjectConverter;
import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.defalt.bo.QAttribute;
import com.cs.layer3.repository.business.defalt.bo.QEntity;
import com.cs.layer3.repository.business.defalt.interfaces.IEntityStrategy;
import com.cs.layer3.repository.business.extension.bo.AttributeInstance;
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.InheritanceDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemAttributeInstanceDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzDecorator;
import com.cs.layer3.repository.business.extension.bo.QItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.QValue;
import com.cs.layer3.repository.business.extension.bo.Value;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.ConditionParam;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;
import com.cs.layer4.persistence.incoming.SqlConnector;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;

public class MySQLNormalizedEntityStrategy implements IEntityStrategy {

	@Autowired
	SqlConnector connector;

	@Autowired
	ClazzApi clazzRepository;

	@Autowired
	AttributeApi attributeRepository;

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public EntityDTO save(EntityDTO bObject) {
		Entity enityToSave = (Entity) ObjectConverter.convertToModel(bObject);
		enityToSave = connector.save(enityToSave);
		List<String> addedClasses = bObject.getAddedClasses();
		if (addedClasses != null && addedClasses.size() > 0) {
			for (String classId : addedClasses) {
				Iterable<ClazzAttributeDecorator> mapping = connector
						.getBySelectID(classId, ClazzAttributeDecorator.class,
								"clazz_Id");
				if (mapping != null && mapping.iterator().hasNext()) {
					for (ClazzAttributeDecorator clazzAttributeMapping : mapping) {
						ItemClazzDecorator itemClassMapping = new ItemClazzDecorator();
						itemClassMapping.setEntity(enityToSave);
						itemClassMapping.setClazz(clazzAttributeMapping
								.getClazz());
						itemClassMapping = connector.save(itemClassMapping);
						AttributeInstance attributeInstance = new AttributeInstance();
						attributeInstance.setAttribute(clazzAttributeMapping
								.getAttribute());
						attributeInstance = connector.save(attributeInstance);
						ItemAttributeInstanceDecorator itemAttributeInstanceDecorator = new ItemAttributeInstanceDecorator();
						itemAttributeInstanceDecorator.setEntity(enityToSave);
						itemAttributeInstanceDecorator
								.setAttributeInstance(attributeInstance);
						itemAttributeInstanceDecorator = connector
								.save(itemAttributeInstanceDecorator);
					}
				} else {
					Clazz clazzAdded = (Clazz) connector.getById(
							Long.valueOf(classId), Clazz.class);
					ItemClazzDecorator itemClassMapping = new ItemClazzDecorator();
					itemClassMapping.setEntity(enityToSave);
					itemClassMapping.setClazz(clazzAdded);
					itemClassMapping = connector.save(itemClassMapping);
				}
			}
		}

		if (bObject.getParentEntity() != null) {
			setParent(Long.valueOf(bObject.getParentEntity()),
					enityToSave.getId());
		}
		return (EntityDTO) ObjectConverter.convertToBO(enityToSave);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long entityId) {
		Entity entity = (Entity) connector.getById(entityId, Entity.class);
		Iterable<ItemClazzAttributeValueDecorator> itemIterable = connector
				.getBySelectID(entityId + "",
						ItemClazzAttributeValueDecorator.class, "entity_id");
		if (itemIterable.iterator().hasNext()) {
			for (ItemClazzAttributeValueDecorator itemClazzAttributeDecorator : itemIterable) {
				connector.delete(itemClazzAttributeDecorator);
			}
		}
		connector.delete(entity);

	}

	public EntityDTO getById(Long id) {

		QEntity qEntity = QEntity.entity;
		JPAQuery query = connector.getJPAQuery();
		Entity entity = query.from(qEntity).where(qEntity.id.eq(id))
				.uniqueResult(qEntity);
		return (EntityDTO) ObjectConverter.convertToBO(entity);

	}

	@SuppressWarnings("unchecked")
	public DataTransferObject getEntityMapping(Long id) {

		EntityDTO entityBO = null;
		List<ItemClazzDecorator> itemClazzDecorators = (List<ItemClazzDecorator>) connector
				.getBySelectID(id + "", ItemClazzDecorator.class, "entity_id");
		for (ItemClazzDecorator itemClazzDecorator : itemClazzDecorators) {
			if (entityBO == null) {
				entityBO = (EntityDTO) ObjectConverter
						.convertToBO(itemClazzDecorator.getEntity());
			}
			ClassDTO classDTO = (ClassDTO) ObjectConverter
					.convertToBO(itemClazzDecorator.getClazz());
			List<ClazzAttributeDecorator> clazzAttributeDecorators = (List<ClazzAttributeDecorator>) connector
					.getBySelectID(classDTO.getId() + "",
							ClazzAttributeDecorator.class, "clazz_id");
			for (ClazzAttributeDecorator clazzAttributeDecorator : clazzAttributeDecorators) {
				AttributeDTO attributeDTO = (AttributeDTO) ObjectConverter
						.convertToBO(clazzAttributeDecorator.getAttribute());
				classDTO.addAttribute(attributeDTO);
			}
			entityBO.addClass(classDTO);
		}
		return entityBO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public void setEntityAttributeValues(Long id,
			Map<String, String> attributeValues) {
		
		
		
		List<ItemClazzAttributeValueDecorator> mapping = (List<ItemClazzAttributeValueDecorator>) connector
				.getBySelectID(id + "", ItemClazzAttributeValueDecorator.class,
						"entity_id");
		for (ItemClazzAttributeValueDecorator itemClazzAttributeValueDecorator : mapping) {
			Attribute attribute = itemClazzAttributeValueDecorator
					.getAttribute();
			String attributeValue = attributeValues.get(attribute.getId() + "");
			if (attributeValue != null) {
				Value value = itemClazzAttributeValueDecorator.getValue();
				Iterable<Value> valueIterable = connector.getBySelectID(
						attributeValue, Value.class, "value");
				Value existingValue = null;
				if (valueIterable.iterator().hasNext()) {
					itemClazzAttributeValueDecorator.setValue(valueIterable
							.iterator().next());
				} else {
					value = new Value();
					value.setValue(attributeValue);
					connector.save(value);
					itemClazzAttributeValueDecorator.setValue(value);
				}

				connector.save(itemClazzAttributeValueDecorator);
			}
		}
	}

	public Set<EntityDTO> search(SearchDTO searchCriteria) {

		try {
			FullTextSession fts = connector.getFullTextSession();
			fts.createIndexer().startAndWait();
			JPAQuery query = connector.getJPAQuery();
			QItemClazzAttributeValueDecorator qItemDecorator = QItemClazzAttributeValueDecorator.itemClazzAttributeValueDecorator;
			query.from(qItemDecorator);
			Map<String, ConditionParam> conditonMap = searchCriteria
					.getConditions();
			boolean first = true;
			BooleanBuilder finalBoolean = null;
			for (String attributeName : conditonMap.keySet()) {
				ConditionParam conditionParam = conditonMap.get(attributeName);
				QValue qValue = QValue.value1;
				HibernateSubQuery subValuequery = new HibernateSubQuery();
				subValuequery.from(qValue);
				subValuequery.where(qValue.value.eq(conditionParam.getLhs()));

				QAttribute qAttribute = QAttribute.attribute;
				HibernateSubQuery subAttributeuery = new HibernateSubQuery();
				subAttributeuery.from(qAttribute);
				subAttributeuery.where(qAttribute.attributeName
						.eq(attributeName));
				if (conditionParam.getOperator().equalsIgnoreCase("OR")) {
					Predicate predicate = qItemDecorator.attribute.in(
							subAttributeuery.list(qAttribute))
							.and(qItemDecorator.value.in(subValuequery
									.list(qValue)));
					if (first) {
						finalBoolean = new BooleanBuilder(predicate);
						first = false;
					} else {
						finalBoolean.or(predicate);
					}
				} else {
					Predicate predicate = qItemDecorator.attribute.in(
							subAttributeuery.list(qAttribute))
							.and(qItemDecorator.value.in(subValuequery
									.list(qValue)));
					if (first) {
						finalBoolean = new BooleanBuilder(predicate);
						first = false;
					} else {
						finalBoolean.and(predicate);
					}
				}
			}
			query.where(finalBoolean);
			Iterable<ItemClazzAttributeValueDecorator> result = query
					.list(qItemDecorator);
			Set<EntityDTO> eligibleEntities = new HashSet<EntityDTO>();
			for (ItemClazzAttributeValueDecorator itemClazzAttributeValueDecorator : result) {
				eligibleEntities.add((EntityDTO) ObjectConverter
						.convertToBO(itemClazzAttributeValueDecorator
								.getEntity()));
			}
			return eligibleEntities;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public EntityDTO update(EntityDTO updatedEntity) {
		Entity entityToUpdate = (Entity) connector.getById(
				updatedEntity.getId(), Entity.class);
		if (updatedEntity.getUpdatedEntityPropertyValues() != null) {
			for (String element : updatedEntity
					.getUpdatedEntityPropertyValues().keySet()) {
				if (updatedEntity.getUpdatedEntityPropertyValues().get(element) != null
						&& element.equals("name")) {
					entityToUpdate.setEntityName(updatedEntity
							.getUpdatedEntityPropertyValues().get(element));
					entityToUpdate = connector.save(entityToUpdate);
				}
			}
		}
		if (updatedEntity.getDeletedClasses() != null) {
			for (String elementID : updatedEntity.getDeletedClasses()) {
				HashMap<String, String> myHashMap = new HashMap<String, String>();
				myHashMap.put("entity_id", entityToUpdate.getId() + "");
				myHashMap.put("clazz_id", elementID);
				connector.deleteByCondition(ItemClazzAttributeDecorator.class,
						myHashMap);
			}
		}

		if (updatedEntity.getAddedClasses() != null) {

			for (String addedClassId : updatedEntity.getAddedClasses()) {
				Iterable<ClazzAttributeDecorator> classMapping = connector
						.getBySelectID(addedClassId,
								ClazzAttributeDecorator.class, "clazz_id");
				if (classMapping.iterator().hasNext()) {
					for (ClazzAttributeDecorator clazzAttributeDecorator : classMapping) {
						ItemClazzAttributeValueDecorator itemDecorator = new ItemClazzAttributeValueDecorator();
						itemDecorator.setEntity(entityToUpdate);
						itemDecorator.setAttribute(clazzAttributeDecorator
								.getAttribute());
						itemDecorator.setClazz(clazzAttributeDecorator
								.getClazz());
						connector.save(itemDecorator);
					}
				} else {
					Clazz defaultClass = (Clazz) connector.getById(
							Long.valueOf(addedClassId), Clazz.class);
					ItemClazzAttributeValueDecorator itemMapping = new ItemClazzAttributeValueDecorator();
					itemMapping.setEntity(entityToUpdate);
					itemMapping.setClazz(defaultClass);
					connector.save(itemMapping);
				}
			}
		}

		if (updatedEntity.getUpdatedClassAttributeValues() != null) {
			setEntityAttributeValues(entityToUpdate.getId(),
					updatedEntity.getUpdatedClassAttributeValues());
		}

		return (EntityDTO) ObjectConverter.convertToBO(entityToUpdate);

	}

	@Override
	public void setParent(Long parentId, Long childId) {

		InheritanceDecorator parentChildRelation = new InheritanceDecorator();
		parentChildRelation.setChild((Entity) connector.getById(childId,
				Entity.class));
		parentChildRelation.setParent((Entity) connector.getById(parentId,
				Entity.class));
		connector.save(parentChildRelation);

	}

}
