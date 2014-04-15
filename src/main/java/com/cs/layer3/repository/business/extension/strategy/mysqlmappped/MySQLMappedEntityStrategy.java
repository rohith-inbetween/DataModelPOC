package com.cs.layer3.repository.business.extension.strategy.mysqlmappped;

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
import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.ClazzMapped;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.defalt.bo.EntityMapped;
import com.cs.layer3.repository.business.defalt.bo.QAttribute;
import com.cs.layer3.repository.business.defalt.bo.QEntity;
import com.cs.layer3.repository.business.defalt.bo.QEntityMapped;
import com.cs.layer3.repository.business.defalt.interfaces.IEntityStrategy;
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.QItemClazzAttributeValueDecorator;
import com.cs.layer3.repository.business.extension.bo.QValue;
import com.cs.layer3.repository.business.extension.bo.Value;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.dto.ConditionParam;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;
import com.cs.layer4.persistence.incoming.SqlConnector;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.ListSubQuery;

public class MySQLMappedEntityStrategy implements IEntityStrategy {

	@Autowired
	SqlConnector connector;

	@Autowired
	ClazzApi clazzRepository;

	@Autowired
	AttributeApi attributeRepository;

	@Transactional
	public EntityDTO save(EntityDTO bObject) {
		EntityMapped enityToSave = (EntityMapped) ObjectConverter
				.convertToMappedModel(bObject);
		List<String> addedClasses = bObject.getAddedClasses();
		if (addedClasses != null && addedClasses.size() > 0) {
			for (String classId : addedClasses) {
				ClazzMapped clazz = (ClazzMapped) connector.getById(
						Long.valueOf(classId), ClazzMapped.class);
				enityToSave.addClass(clazz);
				for (Attribute attribute : clazz.getAttributes()) {
					Value attributeValue = new Value();
					attributeValue.setValue(attribute.getDefaultValue());
					Map<Attribute, Value> attributeValues = enityToSave
							.getAttributeValues();
					if (attributeValues == null) {
						attributeValues = new HashMap<Attribute, Value>();
						enityToSave.setAttributeValues(attributeValues);
						attributeValues = enityToSave.getAttributeValues();
					}
					attributeValues.put(attribute, attributeValue);
				}
			}
		}
		enityToSave = connector.save(enityToSave);
		return (EntityDTO) ObjectConverter.convertToBO(enityToSave);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long entityId) {
		EntityMapped entity = (EntityMapped) connector.getById(entityId,
				EntityMapped.class);
		connector.delete(entity);
	}

	public EntityDTO getById(Long id) {

		QEntityMapped qEntity = QEntityMapped.entityMapped;
		JPAQuery query = connector.getJPAQuery();
		EntityMapped entity = query.from(qEntity).where(qEntity.id.eq(id))
				.uniqueResult(qEntity);
		return (EntityDTO) ObjectConverter.convertToBO(entity);
	}

	@SuppressWarnings("unchecked")
	public DataTransferObject getEntityMapping(Long id) {

		QEntityMapped qEntity = QEntityMapped.entityMapped;
		JPAQuery query = connector.getJPAQuery();
		EntityMapped entity = query.from(qEntity).where(qEntity.id.eq(id))
				.uniqueResult(qEntity);
		return (EntityDTO) ObjectConverter.convertToBO(entity);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	private void setEntityAttributeValues(EntityMapped entity,
			Map<String, String> attributeValues) {
		Map<Attribute, Value> attributeValueMapping = entity
				.getAttributeValues();
		for (Attribute attribute : attributeValueMapping.keySet()) {
			Value existingAttributeValue = attributeValueMapping.get(attribute);
			String attributeValue = attributeValues.get(attribute.getId() + "");
			if (attributeValue != null) {
				Iterable<Value> existingValueFromTable = connector
						.getBySelectID(attributeValue, Value.class, "value");
				if (existingValueFromTable != null
						&& existingValueFromTable.iterator().hasNext()) {
					attributeValueMapping.put(attribute, existingValueFromTable
							.iterator().next());
				} else {
					Value newValue = new Value();
					newValue.setValue(attributeValue);
					attributeValueMapping.put(attribute, newValue);
				}
			}
		}
	}

	public Set<EntityDTO> search(SearchDTO searchCriteria) {

		try {
			FullTextSession fts = connector.getFullTextSession();
			fts.createIndexer().startAndWait();
			JPAQuery query = connector.getJPAQuery();
			QEntityMapped qEntity = QEntityMapped.entityMapped;
			Map<String, ConditionParam> conditonMap = searchCriteria
					.getConditions();
			query.from(qEntity);
			BooleanBuilder finalBoolean = null;
			boolean first = true;
			for (String attributeId : conditonMap.keySet()) {
				ConditionParam conditionParam = conditonMap.get(attributeId);
				QValue qValue = QValue.value1;

				/*
				 * SearchQuery<Value> subValuequery = new
				 * SearchQuery<Value>(fts, qValue);
				 */

				HibernateSubQuery subValuequery = new HibernateSubQuery();
				subValuequery.from(qValue);
				subValuequery.where(qValue.value.eq(conditionParam.getLhs()));
				// query.where(qItemDecorator.value.in(subValuequery.list()));

				QAttribute qAttribute = QAttribute.attribute;

				/*
				 * SearchQuery<Attribute> subAttributeuery = new
				 * SearchQuery<Attribute>( fts, qAttribute);
				 */

				HibernateSubQuery subAttributeuery = new HibernateSubQuery();
				subAttributeuery.from(qAttribute);
				subAttributeuery.where(qAttribute.id.eq(Long
						.valueOf(attributeId)));
				Predicate predicate = qEntity.attributeValues.containsKey(
						subAttributeuery.unique(qAttribute)).and(
						qEntity.attributeValues.containsValue(subValuequery
								.unique(qValue)));
				if (first) {
					finalBoolean = new BooleanBuilder(predicate);
					first = false;
				} else {
					if (conditionParam.getOperator().equalsIgnoreCase("OR")) {
						finalBoolean.or(predicate);
					} else {
						finalBoolean.and(predicate);
					}
				}

			}
			query.where(finalBoolean);
			Iterable<EntityMapped> result = query.list(qEntity);
			Set<EntityDTO> eligibleEntities = new HashSet<EntityDTO>();
			for (EntityMapped mappedEntity : result) {
				eligibleEntities.add((EntityDTO) ObjectConverter
						.convertToBO(mappedEntity));
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
		EntityMapped entityToUpdate = (EntityMapped) connector.getById(
				updatedEntity.getId(), EntityMapped.class);
		if (updatedEntity.getUpdatedEntityPropertyValues() != null) {
			for (String element : updatedEntity
					.getUpdatedEntityPropertyValues().keySet()) {
				if (updatedEntity.getUpdatedEntityPropertyValues().get(element) != null
						&& element.equals("name")) {
					entityToUpdate.setEntityName(updatedEntity
							.getUpdatedEntityPropertyValues().get(element));
				}
			}
		}
		if (updatedEntity.getDeletedClasses() != null) {
			Set<ClazzMapped> classesToDelete = new HashSet<ClazzMapped>();
			for (String classId : updatedEntity.getDeletedClasses()) {
				for (ClazzMapped clazz : entityToUpdate.getClasses()) {
					if (classId.equals(clazz.getId() + "")) {
						classesToDelete.add(clazz);
						break;
					}
				}
			}
			entityToUpdate.getClasses().removeAll(classesToDelete);
		}

		if (updatedEntity.getAddedClasses() != null) {
			Set<ClazzMapped> classesToAdd = new HashSet<ClazzMapped>();
			for (String addedClassId : updatedEntity.getAddedClasses()) {
				ClazzMapped classToAdd = (ClazzMapped) connector.getById(
						Long.valueOf(addedClassId), ClazzMapped.class);
				classesToAdd.add(classToAdd);
			}
			entityToUpdate.getClasses().addAll(classesToAdd);
		}

		// logic to be written for get.
		if (updatedEntity.getUpdatedClassAttributeValues() != null) {
			setEntityAttributeValues(entityToUpdate,
					updatedEntity.getUpdatedClassAttributeValues());
		}
		entityToUpdate = connector.save(entityToUpdate);
		return (EntityDTO) ObjectConverter.convertToBO(entityToUpdate);

	}

	@Override
	public void setParent(Long parentId, Long childId) {
		// TODO Auto-generated method stub

	}

}
