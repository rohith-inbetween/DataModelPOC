package com.cs.layer4.persistence.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer4.persistence.incoming.NoSqlConnector;
import com.cs.layer4.persistence.query.NestedQuery;
import com.mysema.query.jpa.impl.JPAQuery;

public class Neo4jAdapter implements NoSqlConnector {

	 @Autowired
	private Neo4jTemplate neo4jTemplate;

	Transaction tx;

	public Neo4jAdapter() {
		// TODO Auto-generated constructor stub
	}

	public void startTransaction() {
		tx = neo4jTemplate.getGraphDatabase().beginTx();
	}

	public void stopSuccessTransaction() {
		tx.success();
		tx.close();
	}

	public void stopFailTransaction() {
		tx.failure();
		tx.close();
	}

	public <T> T save(T objectToSave) {
		startTransaction();
		T object = neo4jTemplate.save(objectToSave);
		stopSuccessTransaction();
		return object;
	}

	public <T> Object getById(Long id, Class<T> entityClass) {
		startTransaction();
		Object ret = neo4jTemplate.findOne(id, entityClass);
		stopSuccessTransaction();
		return ret;
	}

	public <T> Iterable getBySelectID(String id, Class<T> entityClass,
			String idName) {
		startTransaction();
		String query = "START n1= node(" + id + ") match n1-[r:"
				+ entityClass.getSimpleName() + "]-n2 return r";
		Result<Map<String, Object>> queryResult = neo4jTemplate.query(query,
				new HashMap<String, Object>());
		EndResult<T> endResult = queryResult.to(entityClass);
			ArrayList list = new ArrayList();
			for (Object object : endResult) {
				list.add(object);
				
			}
//			return (Iterable) endResult;
			stopSuccessTransaction();
			return list;
	}

	public void delete(BusinessObject entity) {
		startTransaction();
		neo4jTemplate.delete(entity);
		stopSuccessTransaction();
	}

	public <T> Iterable searchNested(NestedQuery queryNester, Class<T> entity) {
		
		startTransaction();
		Result<Map<String, Object>> queryResult = neo4jTemplate.query(queryNester.query("neo4j"),
				new HashMap<String, Object>());
		
		EndResult<T> endResult = queryResult.to(entity);
		ArrayList list = new ArrayList();
		for (Object object : endResult) {
			list.add(object);
			
		}
//		return (Iterable) endResult;
		stopSuccessTransaction();
		return list;
	}

	public <T> void deleteByCondition(Class<T> entityClass,
			Map<String, String> condition) {
		// TODO Auto-generated method stub

	}
}
