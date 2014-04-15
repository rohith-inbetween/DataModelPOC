package com.cs.layer4.persistence.incoming;

import java.util.Map;

import org.hibernate.search.FullTextSession;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer4.persistence.query.NestedQuery;
import com.mysema.query.jpa.impl.JPAQuery;


public interface IConnector {

	public <T>T save(T objectToSave);

	public <T>Object getById(Long id, Class<T> entityClass);

	public <T>Iterable getBySelectID(String id, Class<T> entityClass, String idName);

	public void delete(BusinessObject entity);

	<T>Iterable searchNested(NestedQuery queryNester, Class<T> entity);
	
	public <T>void deleteByCondition(Class<T> entityClass, Map<String, String> condition);
}
