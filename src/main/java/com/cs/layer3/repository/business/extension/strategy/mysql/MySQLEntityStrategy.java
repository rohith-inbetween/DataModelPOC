package com.cs.layer3.repository.business.extension.strategy.mysql;

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
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeDecorator;
import com.cs.layer3.repository.business.extension.bo.ItemClazzAttributeValueDecorator;
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

public class MySQLEntityStrategy implements IEntityStrategy {

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
				if (mapping.iterator().hasNext()) {
					for (ClazzAttributeDecorator clazzAttributeMapping : mapping) {
						ItemClazzAttributeValueDecorator itemMapping = new ItemClazzAttributeValueDecorator();
						itemMapping.setEntity(enityToSave);
						itemMapping.setClazz(clazzAttributeMapping.getClazz());
						itemMapping.setAttribute(clazzAttributeMapping
								.getAttribute());
						connector.save(itemMapping);
					}
				} else {
					Clazz defaultClass = (Clazz) connector.getById(
							Long.valueOf(classId), Clazz.class);
					ItemClazzAttributeValueDecorator itemMapping = new ItemClazzAttributeValueDecorator();
					itemMapping.setEntity(enityToSave);
					itemMapping.setClazz(defaultClass);
					connector.save(itemMapping);
				}
			}
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

		/*
		 * return (EntityDTO) ObjectConverter.convertToBO((Entity) connector
		 * .getById(id, Entity.class));
		 */
	}

	@SuppressWarnings("unchecked")
	public DataTransferObject getEntityMapping(Long id) {

		List<ItemClazzAttributeValueDecorator> itemClazzAttributeValueMapping = (List<ItemClazzAttributeValueDecorator>) connector
				.getBySelectID(id + "", ItemClazzAttributeValueDecorator.class,
						"entity_id");

		if (itemClazzAttributeValueMapping != null
				&& itemClazzAttributeValueMapping.size() > 0) {
			EntityDTO entityBO = new EntityDTO();
			Map<String, ClassDTO> classes = entityBO.getClasses();
			for (ItemClazzAttributeValueDecorator itemClassAttributeValue : itemClazzAttributeValueMapping) {
				if (entityBO.getId() == null || entityBO.getName() == null) {
					entityBO.setValuesFromEntityModel(itemClassAttributeValue
							.getEntity());
				}
				Clazz clazz = itemClassAttributeValue.getClazz();
				ClassDTO classFromEntityMap = classes.get(clazz.getId() + "");
				if (classFromEntityMap != null) {
					Map<String, AttributeDTO> attributes = classFromEntityMap
							.getAttributes();
					AttributeDTO attributeBO = (AttributeDTO) ObjectConverter
							.convertToBO(itemClassAttributeValue.getAttribute());
					Value value = itemClassAttributeValue.getValue();
					if (value != null) {
						attributeBO.setValue(value.getValue());
					}
					attributes.put(attributeBO.getId() + "", attributeBO);
				} else {
					ClassDTO classBO = new ClassDTO();
					classBO.setValuesFromClazzModel(clazz);
					Map<String, AttributeDTO> attributes = classBO
							.getAttributes();
					AttributeDTO attributeBO = (AttributeDTO) ObjectConverter
							.convertToBO(itemClassAttributeValue.getAttribute());
					Value value = itemClassAttributeValue.getValue();
					if (value != null) {
						attributeBO.setValue(value.getValue());
					}
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
				if(valueIterable.iterator().hasNext()){
					itemClazzAttributeValueDecorator.setValue(valueIterable.iterator().next());
				}
				else{
					value = new Value();
					value.setValue(attributeValue);
					connector.save(value);
					itemClazzAttributeValueDecorator.setValue(value);
				}
				/*if (value == null) {
					Iterable<Value> valueIterable = connector.getBySelectID(
							attributeValue, Value.class, "value");
					Value existingValue = null;
					if (valueIterable.iterator().hasNext()) {
						existingValue = valueIterable.iterator().next();
					}
					if (existingValue != null) {
						value = existingValue;
					} else {
						value = new Value();
						value.setValue(attributeValue);
						connector.save(value);
					}
					itemClazzAttributeValueDecorator.setValue(value);
				} else {
					value.setValue(attributeValue);
				}*/
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
			for (String attributeId : conditonMap.keySet()) {
				ConditionParam conditionParam = conditonMap.get(attributeId);
				QValue qValue = QValue.value1;
				/*SearchQuery<Value> subValuequery = new SearchQuery<Value>(fts,
						qValue);*/
				HibernateSubQuery subValuequery = new HibernateSubQuery();
				subValuequery.from(qValue);
				subValuequery.where(qValue.value.eq(conditionParam.getLhs()));
				// query.where(qItemDecorator.value.in(subValuequery.list()));

				QAttribute qAttribute = QAttribute.attribute;
				/*SearchQuery<Attribute> subAttributeuery = new SearchQuery<Attribute>(
						fts, qAttribute);*/
				HibernateSubQuery subAttributeuery = new HibernateSubQuery();
				subAttributeuery.from(qAttribute);
				subAttributeuery.where(qAttribute.id
						.eq(Long.valueOf(attributeId)));
				if (conditionParam.getOperator().equalsIgnoreCase("OR")) {
					Predicate predicate = qItemDecorator.attribute.in(
							subAttributeuery.list(qAttribute)).and(
									qItemDecorator.value.in(subValuequery.list(qValue)));
					if(first){
						finalBoolean = new BooleanBuilder(predicate);
						first = false;
					}
					else{
						finalBoolean.or(predicate);
					}
				} else {
					Predicate predicate = qItemDecorator.attribute.in(
							subAttributeuery.list(qAttribute)).and(
							qItemDecorator.value.in(subValuequery.list(qValue)));
					if(first){
						finalBoolean = new BooleanBuilder(predicate);
						first = false;
					}
					else{
						finalBoolean.and(predicate);
					}
				}
			}
			query.where(finalBoolean);
			Iterable<ItemClazzAttributeValueDecorator> result = query.list(qItemDecorator);
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

		/*
		 * try { FullTextSession fts = connector.getFullTextSession();
		 * fts.createIndexer().startAndWait(); JPAQuery query =
		 * connector.getJPAQuery(); QItemClazzAttributeValueDecorator
		 * qItemDecorator =
		 * QItemClazzAttributeValueDecorator.itemClazzAttributeValueDecorator;
		 * query.from(qItemDecorator); Map<String, List<ConditionParam>>
		 * conditions = searchCriteria .getConditions(); for (String tableName :
		 * conditions.keySet()) { List<ConditionParam> conditionParams =
		 * conditions .get(tableName); if (tableName.equalsIgnoreCase("value"))
		 * { QValue qValue = QValue.value1; SearchQuery<Value> subValueQuery =
		 * new SearchQuery<Value>( fts, qValue); for (ConditionParam
		 * conditionParam : conditionParams) { String propertyName =
		 * conditionParam.getLhs(); String propertyValue =
		 * conditionParam.getRhs();
		 * subValueQuery.where(qValue.value.eq(propertyValue).and(right)); } if
		 * (subValueQuery.exists()) {
		 * query.where(qItemDecorator.value.in(subValueQuery .list())); } else {
		 * BooleanBuilder builder = new BooleanBuilder(); query.where(builder);
		 * } } else if (tableName.equalsIgnoreCase("attribute")) { QAttribute
		 * qAttribute = QAttribute.attribute; SearchQuery<Attribute>
		 * subAttributeuery = new SearchQuery<Attribute>( fts, qAttribute); for
		 * (ConditionParam conditionParam : conditionParams) { String
		 * propertyName = conditionParam.getLhs(); String propertyValue =
		 * conditionParam.getRhs();
		 * subAttributeuery.where(qAttribute.attributeName .eq(propertyValue));
		 * } if (subAttributeuery.exists()) {
		 * query.where(qItemDecorator.attribute .in(subAttributeuery.list())); }
		 * else { BooleanBuilder builder = new BooleanBuilder();
		 * query.where(builder); } } }
		 * 
		 * Iterable<ItemClazzAttributeValueDecorator> result = query
		 * .list(qItemDecorator); Set<EntityDTO> eligibleEntities = new
		 * HashSet<EntityDTO>(); for (ItemClazzAttributeValueDecorator
		 * itemClazzAttributeValueDecorator : result) {
		 * eligibleEntities.add((EntityDTO) ObjectConverter
		 * .convertToBO(itemClazzAttributeValueDecorator .getEntity())); }
		 * return eligibleEntities;
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } return null;
		 */}

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

		// logic to be written for get.
		if (updatedEntity.getUpdatedClassAttributeValues() != null) {
			/*Iterable<ItemClazzAttributeValueDecorator> itemIterable = connector
					.getBySelectID(entityToUpdate.getId() + "",
							ItemClazzAttributeValueDecorator.class, "entity_id");
			Map<String, String> attributeValueMap = new HashMap<String, String>();
			for (ItemClazzAttributeValueDecorator itemClazzAttributeValueDecorator : itemIterable) {
				if (updatedEntity.getUpdatedClassAttributeValues().get(
						itemClazzAttributeValueDecorator.getAttribute()
								.getAttributeName()) != null) {
					attributeValueMap
							.put(itemClazzAttributeValueDecorator
									.getAttribute().getId() + "",
									updatedEntity
											.getUpdatedClassAttributeValues()
											.get(itemClazzAttributeValueDecorator
													.getAttribute()
													.getAttributeName()));
				}
			}*/
			setEntityAttributeValues(entityToUpdate.getId(), updatedEntity.getUpdatedClassAttributeValues());
		}

		return (EntityDTO) ObjectConverter.convertToBO(entityToUpdate);

	}

	@Override
	public void setParent(Long parentId, Long childId) {
		// TODO Auto-generated method stub
		
	}

}
