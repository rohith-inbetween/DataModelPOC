package com.cs.layer4.persistence.business;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer4.persistence.incoming.SqlConnector;
import com.cs.layer4.persistence.query.NestedQuery;
import com.cs.layer4.persistence.query.TableJoiner;
import com.mysema.query.jpa.impl.JPAQuery;

public class MySqlAdapter implements SqlConnector {

	@Autowired
	EntityManager entityManager;

	public <T> T save(T objectToSave) {
		BusinessObject bo = (BusinessObject) objectToSave;

		if (objectToSave != null && bo.getId() == null) {
			entityManager.persist(objectToSave);

		} else {
			objectToSave = entityManager.merge(objectToSave);
		}
		entityManager.flush();
		// entityManager.getTransaction().commit();
		return (T) objectToSave;

	}

	public <T> Object getById(Long id, Class<T> entityClass) {
		String queryString = "select * from " + entityClass.getSimpleName()
				+ " where id=" + id;

		Query query = entityManager.createNativeQuery(queryString, entityClass);
		Iterable results = query.getResultList();
		return results.iterator().next();
	}

	public <T> Iterable getBySelectID(String id, Class<T> entityClass,
			String idName) {
		String queryString = "select * from " + entityClass.getSimpleName()
				+ " where " + idName + "=\"" + id + "\"";
		Query query = entityManager.createNativeQuery(queryString, entityClass);
		return query.getResultList();
	}

	public void delete(BusinessObject entity) {
		if (entity != null) {
			entityManager.remove(entity);
		}

	}

	public <T> Iterable searchNested(NestedQuery queryNester, Class<T> entity) {
		Query query = entityManager.createNativeQuery(
				queryNester.query("mySQL"), entity);
		return query.getResultList();
	}

	public Iterable search(TableJoiner tableJoiner) {

		return null;
	}

	public <T> void deleteByCondition(Class<T> entityClass,
			Map<String, String> condition) {

		String whereClause = " where ";
		boolean first = true;
		for (String element : condition.keySet()) {
			if (first) {
				whereClause += " " + element + "=" + condition.get(element);
				first = false;
			} else {
				whereClause += " and " + element + "=" + condition.get(element);
			}

		}
		String queryString = "delete from " + entityClass.getSimpleName()
				+ whereClause;
		Query query = entityManager.createNativeQuery(queryString, entityClass);
		query.executeUpdate();
	}

	public JPAQuery getJPAQuery() {
		return new JPAQuery(entityManager);
	}

	@Override
	public FullTextSession getFullTextSession() {
		return Search.getFullTextSession(entityManager.unwrap(Session.class));
	}

}
