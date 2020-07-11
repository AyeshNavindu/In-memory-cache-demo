package com.wiley.interview.phase.cache;

import com.wiley.interview.phase.applicationexception.ApplicationException;
import com.wiley.interview.phase.cache.strategies.CacheStrategy;
import com.wiley.interview.phase.cache.strategies.LFUStrategy;
import com.wiley.interview.phase.cache.strategies.LRUStrategy;

public class Cache <K, V> {
    public static String MAX_SIZE_ERROR = "maxSize should be more than 0";
    public static String ALGORITHM_ERROR = "caching algorithm was not found";
    public enum Algorithm {LRU, LFU}

    private CacheStrategy<K, V> cacheStrategy;

    public Cache(Algorithm algorithm, int maxSize) throws ApplicationException{
        if(maxSize <= 0) {
            throw new ApplicationException(MAX_SIZE_ERROR);
        }
        switch (algorithm) {
            case LFU:
            	cacheStrategy = new LFUStrategy<>(maxSize);
                break;
            case LRU:
            	cacheStrategy = new LRUStrategy<>(maxSize);
                break;
            default: // It's impossible )
                throw new ApplicationException(ALGORITHM_ERROR);
        }
    }

    V get(K key) {
        return cacheStrategy.get(key);
    }

    V put(K key, V value) {
        return cacheStrategy.put(key, value);
    }

    int size() {
        return cacheStrategy.size();
    }

    @Override
    public String toString() {
        return cacheStrategy.toString();
    }
}
