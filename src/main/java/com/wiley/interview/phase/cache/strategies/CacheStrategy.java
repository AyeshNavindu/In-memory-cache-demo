package com.wiley.interview.phase.cache.strategies;

public interface CacheStrategy<K, V> {
    V get(K key);
    V put(K key, V value);
    int size();
}