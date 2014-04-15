package com.cs.layer4.persistence.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import com.cs.layer3.repository.business.defalt.bo.BusinessObject;
import com.cs.layer4.persistence.incoming.CacheConnector;
import com.cs.layer4.persistence.query.NestedQuery;
import com.mysema.query.jpa.impl.JPAQuery;

public class EhCacheAdapter implements CacheConnector {

	private Long counter = 0l;

	private Map<String, EhCacheWrapper<String, Object>> caches = new HashMap<String, EhCacheWrapper<String, Object>>();

	private CacheManager manager;

	public EhCacheAdapter() {
		this.manager = CacheManager.getInstance();
		/*
		 * EhCacheWrapper<String, Object> entityCacheWrapper = new
		 * EhCacheWrapper<String, Object>( "entityCache", manager);
		 * caches.put("entityCache", entityCacheWrapper); EhCacheWrapper<String,
		 * Object> classCacheWrapper = new EhCacheWrapper<String, Object>(
		 * "classCache", manager); caches.put("classCache", classCacheWrapper);
		 * EhCacheWrapper<String, Object> attributeCacheWrapper = new
		 * EhCacheWrapper<String, Object>( "attributeCache", manager);
		 * caches.put("attributeCache", attributeCacheWrapper);
		 * EhCacheWrapper<String, Object> classAttributeMappingCacheWrapper =
		 * new EhCacheWrapper<String, Object>( "classAttributeMappingCache",
		 * manager); caches.put("classAttributeMappingCache",
		 * classAttributeMappingCacheWrapper); EhCacheWrapper<String, Object>
		 * entityClassAttributeMappingCacheWrapper = new EhCacheWrapper<String,
		 * Object>( "entityClassAttributeMappingCache", manager);
		 * caches.put("entityClassAttributeMappingCache",
		 * entityClassAttributeMappingCacheWrapper);
		 */
	}

	private synchronized Long getId() {
		return ++counter;
	}

	public <T> T save(T objectToSave) {
		String cacheName = null;
		if (objectToSave instanceof List) {
			List objectsList = (List) objectToSave;
			if (objectsList.size() > 0) {
				cacheName = objectsList.get(0).getClass().getSimpleName();
				EhCacheWrapper<String, Object> cacheWrapper = getCache(cacheName);
				String id = null;
				List cacheObjects = null;
				for (Object objectsToAdd : objectsList) {
					if (objectsToAdd instanceof BusinessObject) {
						BusinessObject businessObject = (BusinessObject) objectsToAdd;
						if (id == null) {
							id = businessObject.getId() + "";
							cacheObjects = (List) cacheWrapper.get(id);
							if (cacheObjects == null) {
								cacheObjects = new ArrayList<BusinessObject>();
							}
						}
						cacheObjects.add(objectsToAdd);
					}
				}
				cacheWrapper.put(id, cacheObjects);
				return (T) cacheObjects;
			}
		} else if (objectToSave instanceof BusinessObject) {
			cacheName = objectToSave.getClass().getSimpleName();
			EhCacheWrapper<String, Object> cacheWrapper = getCache(cacheName);
			BusinessObject businessObject = (BusinessObject) objectToSave;
			if(businessObject.getId() == null){
				businessObject.setId(getId());
			}
			cacheWrapper.put(businessObject.getId() + "", businessObject);
			return (T) businessObject;
		}
		return null;
	}

	public <T> Object getById(Long id, Class<T> entityClass) {
		EhCacheWrapper<String, Object> cache = getCache(entityClass
				.getSimpleName());
		if (cache != null) {
			return cache.get(id + "");
		}
		return null;
	}

	private EhCacheWrapper<String, Object> getCache(String cacheName) {
		EhCacheWrapper<String, Object> cacheWrapper = caches.get(cacheName);
		if (cacheWrapper == null) {
			cacheWrapper = new EhCacheWrapper<String, Object>(cacheName,
					this.manager);
			caches.put(cacheName, cacheWrapper);
		}
		return cacheWrapper;
	}

	public <T> Iterable getBySelectID(String id, Class<T> entityClass,
			String idName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(BusinessObject entity) {
		// TODO Auto-generated method stub
		
	}

	public <T> Iterable searchNested(NestedQuery queryNester, Class<T> entity) {
		// TODO Auto-generated method stub
		return null;
	}
	public <T> void deleteByCondition(Class<T> entityClass,
			Map<String, String> condition) {
		// TODO Auto-generated method stub
		
	}
}
