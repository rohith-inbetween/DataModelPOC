package com.cs.layer3.repository.business.defalt;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.bo.ClazzMapped;
import com.cs.layer3.repository.business.defalt.bo.Entity;
import com.cs.layer3.repository.business.defalt.bo.EntityMapped;
import com.cs.layer3.repository.business.extension.bo.Value;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;

public class ObjectConverter {

	public static BusinessObject convertToModel(DataTransferObject objectToConvert){
		if(objectToConvert instanceof EntityDTO){
			EntityDTO entityBO = (EntityDTO)objectToConvert;
			Entity entity = new Entity();
			entity.setId(entityBO.getId());
			entity.setEntityName(entityBO.getName());
			return entity;
		}
		else if(objectToConvert instanceof ClassDTO){
			ClassDTO classBO = (ClassDTO)objectToConvert;
			Clazz clazz = new Clazz();
			clazz.setId(classBO.getId());
			clazz.setClassname(classBO.getName());
			return clazz;
		}
		else if(objectToConvert instanceof AttributeDTO){
			AttributeDTO attributeBO = (AttributeDTO)objectToConvert;
			Attribute attribute = new Attribute();
			attribute.setId(attributeBO.getId());
			attribute.setAttributeName(attributeBO.getName());
			attribute.setType(attributeBO.getType());
			return attribute;
		}
		return null;
	}
	
	public static BusinessObject convertToMappedModel(DataTransferObject objectToConvert){
		if(objectToConvert instanceof EntityDTO){
			EntityDTO entityBO = (EntityDTO)objectToConvert;
			EntityMapped entity = new EntityMapped();
			entity.setId(entityBO.getId());
			entity.setEntityName(entityBO.getName());
			return entity;
		}
		else if(objectToConvert instanceof ClassDTO){
			ClassDTO classBO = (ClassDTO)objectToConvert;
			ClazzMapped clazz = new ClazzMapped();
			clazz.setId(classBO.getId());
			clazz.setClassname(classBO.getName());
			return clazz;
		}
		return null;
	}
	
	public static DataTransferObject convertToBO(BusinessObject objectToConvert){
		if(objectToConvert instanceof Entity){
			Entity entity = (Entity)objectToConvert;
			EntityDTO entityBO = new EntityDTO();
			entityBO.setId(entity.getId());
			entityBO.setName(entity.getEntityName());
			return entityBO;
		}
		else if(objectToConvert instanceof EntityMapped){
			EntityMapped entity = (EntityMapped)objectToConvert;
			EntityDTO entityBO = new EntityDTO();
			entityBO.setId(entity.getId());
			entityBO.setName(entity.getEntityName());
			if(entity.getClasses() != null && entity.getClasses().iterator().hasNext()){
				for (ClazzMapped clazz : entity.getClasses()) {
					entityBO.addClass((ClassDTO)convertToBO(clazz));
				}
			}
			if(entity.getAttributeValues() != null){
				for (Attribute attribute : entity.getAttributeValues().keySet()) {
					Value value = entity.getAttributeValues().get(attribute);
					entityBO.addAttributeValue(attribute.getAttributeName(), value.getValue());
				}
			}
			return entityBO;
		}
		else if(objectToConvert instanceof Clazz){
			Clazz clazz = (Clazz)objectToConvert;
			ClassDTO classBO = new ClassDTO();
			classBO.setId(clazz.getId());
			classBO.setName(clazz.getClassname());
			return classBO;
		}
		else if(objectToConvert instanceof ClazzMapped){
			ClazzMapped clazz = (ClazzMapped)objectToConvert;
			ClassDTO classBO = new ClassDTO();
			classBO.setId(clazz.getId());
			classBO.setName(clazz.getClassname());
			if(clazz.getAttributes() != null && clazz.getAttributes().iterator().hasNext()){
				for (Attribute attribute : clazz.getAttributes()) {
					classBO.addAttribute((AttributeDTO)convertToBO(attribute));
				}
			}
			return classBO;
		}
		else if(objectToConvert instanceof Attribute){
			Attribute attribute = (Attribute)objectToConvert;
			AttributeDTO attributeBO = new AttributeDTO();
			attributeBO.setId(attribute.getId());
			attributeBO.setName(attribute.getAttributeName());
			attributeBO.setType(attribute.getType());
			return attributeBO;
		}
		return null;
	}
}
