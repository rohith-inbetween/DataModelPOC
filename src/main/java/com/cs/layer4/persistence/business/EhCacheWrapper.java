package com.cs.layer4.persistence.business;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhCacheWrapper<K, V> implements CacheWrapper<K, V> 
{
    private final String cacheName;
    private final CacheManager cacheManager;

    public EhCacheWrapper(final String cacheName, final CacheManager cacheManager)
    {
        this.cacheName = cacheName;
        this.cacheManager = cacheManager;
        this.cacheManager.addCache(this.cacheName);
    }

    public synchronized void put(final K key, final V value)
    {
        getCache().put(new Element(key, value));
    }

    public synchronized V get(final K key) 
    {
        Element element = getCache().get(key);
        if (element != null) {
            return (V) element.getObjectValue();
        }
        return null;
    }

    public Ehcache getCache() 
    {
    	Ehcache cache = cacheManager.getEhcache(cacheName);
//    	if(cache == null){
//    		cacheManager.addCache(cacheName);
//    		cache = cacheManager.getEhcache(cacheName);
//    	}
        return cache;
        
    }
}