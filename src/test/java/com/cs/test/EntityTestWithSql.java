package com.cs.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.api.EntityApi;
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.cs.layer3.repository.incoming.dto.ConditionParam;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/resources/SqlContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class EntityTestWithSql {

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
		if (classBOs != null) {
			for (ClassDTO classBusinessObject : classBOs) {
				classIds.add(classBusinessObject.getId() + "");
			}
		}
		EntityDTO product = new EntityDTO();
		product.setName(name);
		product.setAddedClasses(classIds);
		EntityDTO savedEntity = (EntityDTO) entityRepository.save(product);
		System.out.println("Saved Entity Id  : " + savedEntity.getId());
		return savedEntity;
	}

	private ClassDTO createClass(String name, List<AttributeDTO> attributes) {
		List<String> attributeIds = new ArrayList<String>();
		for (AttributeDTO attributeBusinessObject : attributes) {
			attributeIds.add(attributeBusinessObject.getId() + "");
		}
		ClassDTO myClass = new ClassDTO();
		myClass.setAddedAttributes(attributeIds);
		myClass.setName(name);
		ClassDTO savedClass = clazzRepository.save(myClass);
		System.out.println("Saved Class Id  : " + savedClass.getId());
		return savedClass;
	}

	private AttributeDTO createAttribute(String name, String type) {
		AttributeDTO attr = new AttributeDTO();
		attr.setName(name);
		attr.setType(type);
		AttributeDTO savedAttr = (AttributeDTO) attrRep.save(attr);
		System.out.println("Saved Attr Id  : " + savedAttr.getId());
		return savedAttr;
	}

	@Test
	public void createEntity() {
		EntityDTO product = createProduct("TestQUERYDSL", null);
		System.out.println("!@#$!@$#!@$!@$" + entityRepository.findOne(product.getId()).getId());
	}

	@Test
	@Transactional
	@Rollback(value = true)
	public void createAndGetEntitiesWithMapppingAndValues() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct", classes);

		Map<String, String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		product.setAttributeValues(attributeValues);
		entityRepository.update(product);
		System.out.println("******Created And Retrieved Mapped Entity \n"
				+ new Gson().toJson(entityRepository
						.getMapppingForGivenEntity(product.getId())));
	}

	@Test
	@Transactional
	@Rollback(value = true)
	public void searchForValues() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct", classes);

		Map<String, String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		product.setAttributeValues(attributeValues);
		entityRepository.update(product);

		EntityDTO product2 = createProduct("MyProduct2", classes);

		Map<String, String> attributeValues2 = new HashMap<String, String>();
		attributeValues2.put(sizeAttribute.getId() + "", "Huge");
		attributeValues2.put(weightAttribute.getId() + "", "511kg");
		attributeValues2.put(lengthAttribute.getId() + "", "522m");
		product2.setAttributeValues(attributeValues2);
		entityRepository.update(product2);

		SearchDTO searchDTO = new SearchDTO();
		Map<String, ConditionParam> conditionsMap = new HashMap<String, ConditionParam>();
		ConditionParam condition = new ConditionParam();
		condition.setLhs("500m");
		condition.setOperator("AND");
		conditionsMap.put(lengthAttribute.getId() + "", condition);
		condition = new ConditionParam();
		condition.setLhs("500kg");
		condition.setOperator("OR");
		conditionsMap.put(weightAttribute.getId() + "", condition);
		searchDTO.setConditions(conditionsMap);
		System.out.println("AFTER CREATING CONDITIONSSS");
		System.out.println("###########Searched For length=522m \n"
				+ new Gson().toJson(entityRepository.searchValues(searchDTO)));
	}

	@Rollback(value = true)
	@Test
	public void checkUpdateOfAttribute() {
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		// Attribute updatedAttribute =
		// (Attribute)ObjectConverter.convertToModel(weightAttribute);
		weightAttribute.setName("anindyaUpdated"); // Updated Value
		System.out.println("@#%#%#%@Initial Attribute\n"
				+ new Gson().toJson(attrRep.findOne(weightAttribute.getId())));
		attrRep.update(weightAttribute);
		System.out.println("@#%#%#%@Updated Attribute\n"
				+ new Gson().toJson(attrRep.findOne(weightAttribute.getId())));
	}

	@Test(expected = NoSuchElementException.class)
	@Rollback(value = true)
	public void checkDeleteOfAttribute() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct", classes);

		Map<String, String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		product.setAttributeValues(attributeValues);
		entityRepository.update(product);
		attrRep.delete(sizeAttribute.getId());
		attrRep.findOne(sizeAttribute.getId());
	}

	@Test
	@Rollback(value = true)
	public void checkClassUpdate() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct", classes);

		Map<String, String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		product.setAttributeValues(attributeValues);
		entityRepository.update(product);

		ClassDTO classDTO = new ClassDTO();
		classDTO.setId(myClass1.getId());
		Map<String, String> updatedProperties = new HashMap<String, String>();
		updatedProperties.put("name", "AnindyaUpdatedClass");
		classDTO.setUpdatedClassPropertyValues(updatedProperties);
		List<String> deletedAttributes = new ArrayList<String>();
		deletedAttributes.add(lengthAttribute.getId() + "");
		classDTO.setDeletedAttributes(deletedAttributes);
		List<String> addedAttributes = new ArrayList<String>();
		addedAttributes.add(weightAttribute.getId() + "");
		classDTO.setAddedAttributes(addedAttributes);
		System.out.println("%#$%$%$%Initial Class\n"
				+ new Gson().toJson(clazzRepository.findOne(classDTO.getId())));
		clazzRepository.update(classDTO);
		System.out.println("%#$%$%$%Updated Class\n"
				+ new Gson().toJson(clazzRepository.findOne(classDTO.getId())));

	}

	@Test(expected = NoSuchElementException.class)
	@Rollback(value = true)
	public void checkDeleteOfClass() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct", classes);
		clazzRepository.delete(myClass1.getId());
		clazzRepository.findOne(myClass1.getId());
	}

	@Test
	@Rollback(value = false)
	public void checkDeleteOfEntity() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		classes.add(myClass);

		EntityDTO product = createProduct("MyProduct", classes);
		entityRepository.deleteEntity(product.getId());
		System.out.println(entityRepository.findOne(product.getId()));
	}

	@Test
	@Rollback(value = false)
	public void checkEntityUpdate() {
		AttributeDTO sizeAttribute = createAttribute("Size","String");
		AttributeDTO weightAttribute = createAttribute("Weight","Number");
		AttributeDTO lengthAttribute = createAttribute("Length","Number");
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(weightAttribute);

		ClassDTO myClass = createClass("TestClass1", attributes);

		attributes = new ArrayList<AttributeDTO>();
		attributes.add(sizeAttribute);
		attributes.add(lengthAttribute);

		ClassDTO myClass1 = createClass("TestClass2", attributes);

		List<ClassDTO> classes = new ArrayList<ClassDTO>();
		classes.add(myClass1);
		EntityDTO product = createProduct("MyProduct", classes);

		EntityDTO entityToUpdate = new EntityDTO();
		entityToUpdate.setId(product.getId());
		Map<String, String> entityProperties = new HashMap<String, String>();
		entityProperties.put("name", "UpdateProduct");
		entityToUpdate.setUpdatedEntityPropertyValues(entityProperties);
		List<String> addedClasses = new ArrayList<String>();
		addedClasses.add(myClass.getId() + "");
		entityToUpdate.setAddedClasses(addedClasses);
		List<String> deletedClasses = new ArrayList<String>();
		deletedClasses.add(myClass1.getId() + "");
		entityToUpdate.setDeletedClasses(deletedClasses);
		Map<String,String> attributeMap = new HashMap<String, String>();
		attributeMap.put(lengthAttribute.getId() + "", "8783782374892");
		entityToUpdate.setUpdatedClassAttributeValues(attributeMap);
		System.out.println("!@#!@#!@INITIAL ENTITY" + new Gson().toJson(entityRepository.getMapppingForGivenEntity(entityToUpdate.getId())));
		entityRepository.update(entityToUpdate);
		System.out.println("!@#!@#!@UPDATEd ENTITY"
				+ new Gson().toJson(entityRepository
						.getMapppingForGivenEntity(entityToUpdate.getId())));
	}

	@Test
	@Rollback(value = false)
	public void fixing() {
		ClassDTO classToUpdate = new ClassDTO();
		classToUpdate.setId(2l);
		List<String> addedAttributes = new ArrayList<String>();
		addedAttributes.add("2");
		classToUpdate.setAddedAttributes(addedAttributes);
		System.out.println("!@#!@#!@INITIAL ENTITY"
				+ new Gson().toJson(entityRepository
						.getMapppingForGivenEntity(classToUpdate.getId())));
		clazzRepository.update(classToUpdate);
		System.out.println("!@#!@#!@UPDATEd ENTITY"
				+ new Gson().toJson(entityRepository
						.getMapppingForGivenEntity(classToUpdate.getId())));
	}

	@Test
	public void queryDSLPOC() {
		AttributeDTO attr = (AttributeDTO)attrRep.findOne(8l);
		System.out.println(attr.getName());
	}

}
