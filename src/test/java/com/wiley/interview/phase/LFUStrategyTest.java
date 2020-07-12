package com.wiley.interview.phase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wiley.interview.phase.cache.strategies.CacheStrategy;
import com.wiley.interview.phase.cache.strategies.LFUStrategy;

public class LFUStrategyTest {
	private int maxSize;
	private CacheStrategy<Integer, Integer> cacheStrategy;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@BeforeEach
	void init() {
		maxSize = 7;
		cacheStrategy = new LFUStrategy(maxSize);
		for (int i = 0; i < maxSize; i++) {
			cacheStrategy.put(i, i);
		}
	}

	@Test
	void testput() {
		Integer key = 1;
		Integer value = 111;
		Integer oldValue = cacheStrategy.put(key, value);
		assertEquals(value, cacheStrategy.get(key));
		assertEquals(oldValue, Integer.valueOf(1));
	}

	@Test
	void testget() {
		Integer key = 1;
		assertEquals(Integer.valueOf(key), key);
	}

	@Test
	void testsize() {
		assertEquals(maxSize, cacheStrategy.size());
		cacheStrategy.put(maxSize + 1, maxSize + 1);
		assertEquals(maxSize, cacheStrategy.size());
	}

	@Test
	void testLfuStrategy() {
		Integer key = maxSize / 2;
		for (int i = 0; i < maxSize; i++) {
			if (i != key) {
				cacheStrategy.get(i);
				cacheStrategy.get(i);
			}
		}
		cacheStrategy.get(key);
		cacheStrategy.put(maxSize + 1, maxSize + 1);
		assertNull(cacheStrategy.get(key));
		cacheStrategy.put(maxSize + 2, maxSize + 2);
		assertNull(cacheStrategy.get(maxSize + 1));
	}

}
