package com.wiley.interview.phase.cache.strategies;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class LRUStrategy<K, V> extends LinkedHashMap<K, V> implements CacheStrategy<K, V>{
	private static final float LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;
    private final int capacity;

    public LRUStrategy(int maxSize) {
		super(maxSize, LOAD_FACTOR, ACCESS_ORDER);
        capacity = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity ;
    }
}
