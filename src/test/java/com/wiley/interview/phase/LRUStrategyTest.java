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
    void testLruStrategy() {
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
	public void testEmptyCache() {
		assertEquals(cacheStrategy.get(111), null);
	}
    
    @Test
	public void testLowerCapacityFlow() {
		cacheStrategy.put(1, 1);
		assertEquals(cacheStrategy.get(1).longValue(), 1L);
		assertEquals(cacheStrategy.get(222), null);
		cacheStrategy.put(2, 4);
		assertEquals(cacheStrategy.get(1).longValue(), 1L);
		assertEquals(cacheStrategy.get(2).longValue(), 4L);
	}

	@Test
	public void testCapacityRemoveOldest() {
		cacheStrategy.put(1, 1);
		cacheStrategy.put(2, 3);
		cacheStrategy.put(3, 6);
		assertEquals(cacheStrategy.get(111), null);
		assertEquals(cacheStrategy.get(2).longValue(), 3L);
		assertEquals(cacheStrategy.get(3).longValue(), 6L);
	}

	@Test
	public void testGetRenewsEntry() {
		cacheStrategy.put(1, 1);
		cacheStrategy.put(2, 4);
		assertEquals(cacheStrategy.get(1).longValue(), 1L);
		cacheStrategy.put(3, 9);
		assertEquals(cacheStrategy.get(1).longValue(), 1L);
		assertEquals(cacheStrategy.get(2222), null);
		assertEquals(cacheStrategy.get(3).longValue(), 9L);
	}
	
}
