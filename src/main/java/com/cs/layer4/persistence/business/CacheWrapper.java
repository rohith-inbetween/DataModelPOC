package com.cs.layer4.persistence.business;

public interface CacheWrapper<K, V>
{
  void put(K key, V value);

  V get(K key);
}
