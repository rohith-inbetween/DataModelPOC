package com.cs.layer3.repository.business.defalt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.bo.Attribute;
import com.cs.layer3.repository.business.defalt.bo.Clazz;
import com.cs.layer3.repository.business.defalt.interfaces.IClazzStrategy;
import com.cs.layer3.repository.business.extension.bo.ClazzAttributeDecorator;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer4.persistence.incoming.IConnector;

@Component
public class DefaultClazzStrategy implements IClazzStrategy {

	@Autowired
	protected IConnector connector;

	@Autowired
	protected AttributeApi attributeRepository;

	public ClassDTO save(ClassDTO bObject) {
		Clazz clazz = (Clazz) ObjectConverter.convertToModel(bObject);
		clazz = connector.save(clazz);
		ClassDTO classBO = (ClassDTO) ObjectConverter
				.convertToBO(clazz);
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
		return classBO;
	}

	public void createClazzAttributeRelationship(Clazz clazz,
			List<AttributeDTO> attributeBOs) {
		List<ClazzAttributeDecorator> clazzAttributeDecorators = new ArrayList<ClazzAttributeDecorator>();
		for (AttributeDTO attributeBusinessObject : attributeBOs) {
			ClazzAttributeDecorator clazzAttributeDecorator = new ClazzAttributeDecorator();
			clazzAttributeDecorator.setId(clazz.getId());
			clazzAttributeDecorator.setClazz(clazz);
			clazzAttributeDecorator.setAttribute((Attribute) ObjectConverter
					.convertToModel(attributeBusinessObject));
			clazzAttributeDecorators.add(clazzAttributeDecorator);
		}
		connector.save(clazzAttributeDecorators);
	}

	public ClassDTO getClazzAttributeMappingByClazzId(Long id) {
		Iterable<ClazzAttributeDecorator> classAttributeMappings = (Iterable<ClazzAttributeDecorator>) connector
				.getById(id, ClazzAttributeDecorator.class);
		ClassDTO classBusinessObject = null;
		if (classAttributeMappings != null) {
			for (ClazzAttributeDecorator clazzAttributeDecorator : classAttributeMappings) {
				if (classBusinessObject == null) {
					classBusinessObject = (ClassDTO) ObjectConverter
							.convertToBO(clazzAttributeDecorator.getClazz());
					classBusinessObject
							.setAttributes(new HashMap<String, AttributeDTO>());
				}
				classBusinessObject
						.addAttribute((AttributeDTO) ObjectConverter
								.convertToBO(clazzAttributeDecorator
										.getAttribute()));
			}
		}
		return classBusinessObject;
	}

	public void delete(ClassDTO bObject) {
		// TODO Auto-generated method stub

	}

	public ClassDTO getById(Long id) {
		Clazz clazz = (Clazz) connector.getById(id, Clazz.class);
		return (ClassDTO) ObjectConverter.convertToBO(clazz);
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	public ClassDTO update(ClassDTO updatedClass) {
		// TODO Auto-generated method stub
		return null;
	}
}
