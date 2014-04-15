package com.cs.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.api.EntityApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/resources/ExtendedStrategyContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class EntityTestWithExtendedStrategy {

	@Autowired
	ClazzApi clazzRepository;

	@Autowired
	AttributeApi attrRep;

	@Autowired
	EntityApi entityRepository;

	@Before
	public void beforeStart() {

	}

	private EntityDTO createProduct(String name, List<ClassDTO> classBOs) {
		List<String> classIds = new ArrayList<String>();
		for (ClassDTO classBusinessObject : classBOs) {
			classIds.add(classBusinessObject.getId() + "");
		}
		EntityDTO product = new EntityDTO();
		product.setName(name);
		for (String id : classIds) {
			product.addClass(new ClassDTO(Long.valueOf(id)));
		}
		EntityDTO savedEntity = (EntityDTO)entityRepository.save(product);
		System.out.println("Saved Entity Id  : " + savedEntity.getId());
		return savedEntity;
	}

	private ClassDTO createClass(String name, List<AttributeDTO> attributes) {
		List<String> attributeIds = new ArrayList<String>();
		for (AttributeDTO attributeBusinessObject : attributes) {
			attributeIds.add(attributeBusinessObject.getId() + "");
		}
		ClassDTO myClass = new ClassDTO();
		myClass.setName(name);
		for (String id : attributeIds) {
			myClass.addAttribute(new AttributeDTO(Long.valueOf(id)));
		}
		ClassDTO savedClass = clazzRepository.save(myClass);
		System.out.println("Saved Class Id  : " + savedClass.getId());
		return savedClass;
	}

	private AttributeDTO createAttribute(String name) {
		AttributeDTO attr = new AttributeDTO();
		attr.setName(name);
		AttributeDTO savedAttr = (AttributeDTO)attrRep.save(attr);
		System.out.println("Saved Attr Id  : " + savedAttr.getId());
		return savedAttr;
	}

	@Test
	public void getEntitiesWithMappping() {
		AttributeDTO sizeAttribute = createAttribute("Size");
		AttributeDTO weightAttribute = createAttribute("Weight");
		AttributeDTO lengthAttribute = createAttribute("Length");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1",attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2",attributes);
		
		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct",classes);
		
		System.out.println(new Gson().toJson(entityRepository.getMapppingForGivenEntity(product.getId())));
	}
}
