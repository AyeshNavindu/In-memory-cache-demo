package com.wiley.interview.phase.cache;

public interface  CacheSystem <K, V> {
    void putToCache(K key, V value);

    V getFromCache(K key);

    void removeFromCache(K key);

    int getCacheSize();

    boolean isObjectPresent(K key);

    boolean hasEmptyPlace();

    void clearCache();
}
