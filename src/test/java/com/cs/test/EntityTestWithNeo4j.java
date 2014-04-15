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
import org.springframework.dao.DataRetrievalFailureException;
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
@ContextConfiguration(locations = { "file:src/main/resources/Neo4jContext.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class EntityTestWithNeo4j {

	@Autowired
	ClazzApi clazzRepository;

	@Autowired
	AttributeApi attrRep;

	@Autowired
	EntityApi entityRepository;

	@Before
	public void beforeStart() {

	}

	private EntityDTO createProduct(String name,
			List<ClassDTO> classBOs) {
		List<String> classIds = new ArrayList<String>();
		if (classBOs != null) {
			for (ClassDTO classBusinessObject : classBOs) {
				classIds.add(classBusinessObject.getId() + "");
			}
		}
		EntityDTO product = new EntityDTO();
		product.setName(name);
		for (String id : classIds) {
			product.addClass(new ClassDTO(Long.valueOf(id)));
		}
		EntityDTO savedEntity = (EntityDTO) entityRepository
				.save(product);
		System.out.println("Saved Entity Id  : " + savedEntity.getId());
		return savedEntity;
	}

	private ClassDTO createClass(String name,
			List<AttributeDTO> attributes) {
		List<String> attributeIds = new ArrayList<String>();
		if (attributes != null) {
			for (AttributeDTO attributeBusinessObject : attributes) {
				attributeIds.add(attributeBusinessObject.getId() + "");
			}
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
		AttributeDTO savedAttr = (AttributeDTO) attrRep
				.save(attr);
		System.out.println("Saved Attr Id  : " + savedAttr.getId());
		return savedAttr;
	}

	@Test
	@Transactional
	public void getEntitiesWithMappping() {
		AttributeDTO sizeAttribute = createAttribute("Size");
		AttributeDTO weightAttribute = createAttribute("Weight");
		AttributeDTO lengthAttribute = createAttribute("Length");
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
		Map<String,String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		entityRepository.setEntityAttributeValues(product.getId(), attributeValues);
		EntityDTO entityBO = (EntityDTO)entityRepository
				.getMapppingForGivenEntity(product.getId());
		System.out.println(new Gson().toJson(entityBO));
	}
	
	@Test
	public void deleteEntityWithMappping() {
		AttributeDTO sizeAttribute = createAttribute("Size");
		AttributeDTO weightAttribute = createAttribute("Weight");
		AttributeDTO lengthAttribute = createAttribute("Length");
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
		Map<String,String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		entityRepository.setEntityAttributeValues(product.getId(), attributeValues);
		entityRepository.deleteEntity(product.getId());
		EntityDTO dto = (EntityDTO)entityRepository.findOne(product.getId());
	}
	
	
	@Test
	public void checkUpdateOfAttribute()
	{
		AttributeDTO sizeAttribute = createAttribute("Size");
		AttributeDTO weightAttribute = createAttribute("Weight");
//		Attribute updatedAttribute  = (Attribute)ObjectConverter.convertToModel(weightAttribute);
		weightAttribute.setName("anindyaUpdated");	//Updated Value
		System.out.println("@#%#%#%@Initial Attribute\n" + new Gson().toJson(attrRep.findOne(weightAttribute.getId())));
		attrRep.update(weightAttribute);
		System.out.println("@#%#%#%@Updated Attribute\n" + new Gson().toJson(attrRep.findOne(weightAttribute.getId())));
	}
	
	
	
	@Test(expected = DataRetrievalFailureException.class)
	public void checkDeleteOfAttribute()
	{
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
		
		Map<String,String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		entityRepository.setEntityAttributeValues(product.getId(), attributeValues);
		attrRep.delete(sizeAttribute.getId());
		attrRep.findOne(sizeAttribute.getId());
	}
	
	
	@Test
	public void checkClassUpdate()
	{
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
		
		Map<String,String> attributeValues = new HashMap<String, String>();
		attributeValues.put(sizeAttribute.getId() + "", "Huge");
		attributeValues.put(weightAttribute.getId() + "", "500kg");
		attributeValues.put(lengthAttribute.getId() + "", "500m");
		entityRepository.setEntityAttributeValues(product.getId(), attributeValues);

		
		ClassDTO classDTO = new ClassDTO();
		classDTO.setId(myClass1.getId());
		Map<String, String> updatedProperties = new HashMap<String, String>();
		updatedProperties.put("name", "AnindyaUpdatedClass");
		classDTO.setUpdatedClassPropertyValues(updatedProperties);
		List<String> deletedAttributes = new ArrayList<String>();
		deletedAttributes.add(lengthAttribute.getId()+"");
		classDTO.setDeletedAttributes(deletedAttributes);
		List<String> addedAttributes = new ArrayList<String>();
		addedAttributes.add(weightAttribute.getId() + "");
		classDTO.setAddedAttributes(addedAttributes);
		System.out.println("%#$%$%$%Initial Class\n" + new Gson().toJson(clazzRepository.findOne(classDTO.getId())));
		clazzRepository.update(classDTO);
		System.out.println("%#$%$%$%Updated Class\n" + new Gson().toJson(clazzRepository.findOne(classDTO.getId())));
		
	}
	
	
	@Test(expected = DataRetrievalFailureException.class)
	@Rollback(value = true)
	public void checkDeleteOfClass()
	{
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
		clazzRepository.delete(myClass1.getId());
		clazzRepository.findOne(myClass1.getId());
	}
	
	
	@Test(expected = DataRetrievalFailureException.class)
	@Rollback(value = true)
	public void checkDeleteOfEntity()
	{
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
		entityRepository.deleteEntity(product.getId());
		entityRepository.findOne(product.getId());
	}
	
	
	@Test
	@Rollback(value = true)
	public void checkEntityUpdate()
	{
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
		EntityDTO product = createProduct("MyProduct",classes);
		
		EntityDTO entityToUpdate = new EntityDTO();
		entityToUpdate.setId(product.getId());
		Map<String, String> entityProperties = new HashMap<String, String>();
		entityProperties.put("name", "UpdateProduct");
		entityToUpdate.setUpdatedEntityPropertyValues(entityProperties);
		List<String> addedClasses = new ArrayList<String>();
		addedClasses.add(myClass.getId()+"");
		entityToUpdate.setAddedClasses(addedClasses);
		List<String> deletedClasses = new ArrayList<String>();
		deletedClasses.add(myClass1.getId() +"");
		entityToUpdate.setDeletedClasses(deletedClasses);
		System.out.println("!@#!@#!@INITIAL ENTITY" + new Gson().toJson(entityRepository.getMapppingForGivenEntity(entityToUpdate.getId())));
		entityRepository.update(entityToUpdate);
		System.out.println("!@#!@#!@UPDATEd ENTITY" + new Gson().toJson(entityRepository.getMapppingForGivenEntity(entityToUpdate.getId())));
	}
	
	@Test
	public void searchForValues() {
		AttributeDTO sizeAttribute = createAttribute("Size");
		AttributeDTO weightAttribute = createAttribute("Weight");
		AttributeDTO lengthAttribute = createAttribute("Length");
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
		entityRepository.setEntityAttributeValues(product.getId(),
				attributeValues);

		EntityDTO product2 = createProduct("MyProduct2", classes);

		Map<String, String> attributeValues2 = new HashMap<String, String>();
		attributeValues2.put(sizeAttribute.getId() + "", "Huge");
		attributeValues2.put(weightAttribute.getId() + "", "511kg");
		attributeValues2.put(lengthAttribute.getId() + "", "522m");
		entityRepository.setEntityAttributeValues(product2.getId(),
				attributeValues2);

		SearchDTO searchDTO = new SearchDTO();
		Map<String, List<ConditionParam>> conditionsMap = new HashMap<String, List<ConditionParam>>();
		List<ConditionParam> conditionParams = new ArrayList<ConditionParam>();
		ConditionParam condition = new ConditionParam();
		condition.setLhs("attributeName");
		condition.setRhs("Length");
		conditionParams.add(condition);
		conditionsMap.put("Attribute", conditionParams);
		conditionParams = new ArrayList<ConditionParam>();
		condition = new ConditionParam();
		condition.setLhs("value");
		condition.setRhs("522m");
		conditionParams.add(condition);
		conditionsMap.put("Value", conditionParams);
//		searchDTO.setConditions(conditionsMap);
//		searchDTO.setConditionType("OR");
		System.out.println("AFTER CREATING CONDITIONSSS");
		System.out.println("###########Searched For length=522m \n"
				+ new Gson().toJson(entityRepository.searchValues(searchDTO)));
	}
}
