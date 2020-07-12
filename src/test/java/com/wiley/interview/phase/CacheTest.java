package com.wiley.interview.phase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import com.wiley.interview.phase.applicationexception.ApplicationException;
import com.wiley.interview.phase.cache.Cache;

public class CacheTest {
	
private Cache<Integer, Integer> cache = null;
	@Test
	void checkApplicationExceptionLRU() {
		Throwable exception = assertThrows(ApplicationException.class, () -> {
			cache = new Cache<>(Cache.Algorithm.LRU, 0);
		});
		assertEquals(Cache.MAX_SIZE_ERROR, exception.getMessage());
	}

	@Test
	void checkApplicationExceptionLFU() {
		Throwable exception = assertThrows(ApplicationException.class, () -> {
			cache =new Cache<>(Cache.Algorithm.LFU, 0);
		});
		assertEquals(Cache.MAX_SIZE_ERROR, exception.getMessage());
	}
}
