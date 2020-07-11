package com.wiley.interview.phase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wiley.interview.phase.cache.strategies.CacheStrategy;
import com.wiley.interview.phase.cache.strategies.LRUStrategy;

public class LRUStrategyTest {
	private int maxSize;
    private CacheStrategy<Integer, Integer> cacheStrategy;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeEach
    void init() {
        maxSize = 6;
        cacheStrategy = new LRUStrategy(maxSize);
        for(int i = 0; i < maxSize; i++) {
            cacheStrategy.put(i, i);
        }
    }

    @Test
    void testPutSuccess() {
        Integer key = 1;
        Integer value = 111;
        Integer oldValue = cacheStrategy.put(key, value);
        assertEquals(value, cacheStrategy.get(key));
        assertEquals(oldValue, Integer.valueOf(1));
    }

    @Test
    void testGetSuccess() {
        Integer key = 1;
        assertEquals(Integer.valueOf(key), key);
    }

    @Test
    void testSize() {
        assertEquals(maxSize, cacheStrategy.size());
        cacheStrategy.put(maxSize + 1, maxSize + 1);
        assertEquals(maxSize, cacheStrategy.size());
    }

    @Test
    void lru() {
        Integer key = maxSize / 2;
        cacheStrategy.get(key);
        for (int i = 0; i < maxSize; i++) {
            if(i != key) {
                cacheStrategy.get(i);
            }
        }
        cacheStrategy.put(maxSize + 1, maxSize + 1);
        assertNull(cacheStrategy.get(key));
    }
    
    @Test
	public void testInitCacheEmpty() {
		assertEquals(cacheStrategy.get(1), null);
	}

	@Test
	public void testSetBelowCapacity() {
		cacheStrategy.put(1, 1);
		assertEquals(cacheStrategy.get(1).intValue(), 1);
		assertEquals(cacheStrategy.get(2), null);
		cacheStrategy.put(2, 4);
		assertEquals(cacheStrategy.get(1).intValue(), 1);
		assertEquals(cacheStrategy.get(2).intValue(), 4);
	}

	@Test
	public void testCapacityReachedOldestRemoved() {
		cacheStrategy.put(1, 1);
		cacheStrategy.put(2, 3);
		cacheStrategy.put(3, 6);
		assertEquals(cacheStrategy.get(1), null);
		assertEquals(cacheStrategy.get(2).intValue(), 3);
		assertEquals(cacheStrategy.get(3).intValue(), 6);
	}

	@Test
	public void testGetRenewsEntry() {
		cacheStrategy.put(1, 1);
		cacheStrategy.put(2, 4);
		assertEquals(cacheStrategy.get(1).intValue(), 1);
		cacheStrategy.put(3, 9);
		assertEquals(cacheStrategy.get(1).intValue(), 1);
		assertEquals(cacheStrategy.get(2).intValue(), null);
		assertEquals(cacheStrategy.get(3).intValue(), 9);
	}
}
