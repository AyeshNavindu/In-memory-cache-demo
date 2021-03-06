package com.wiley.interview.phase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wiley.interview.phase.cache.FileSystemCache;

public class FileSystemCacheTest {
	private static final String VALUE1 = "value1";
	private static final String VALUE2 = "value2";

	private FileSystemCache<Integer, String> fileSystemCache;

	@BeforeEach
	public void init() {
		fileSystemCache = new FileSystemCache<>();
	}

	@AfterEach
	public void clearCache() {
		fileSystemCache.clearCache();
	}

	@Test
	public void shouldPutGetAndRemoveObjectTest() {
		fileSystemCache.putToCache(0, VALUE1);
		assertEquals(VALUE1, fileSystemCache.getFromCache(0));
		assertEquals(1, fileSystemCache.getCacheSize());

		fileSystemCache.removeFromCache(0);
		assertNull(fileSystemCache.getFromCache(0));
	}

	@Test
	public void shouldNotGetObjectFromCacheIfNotExistsTest() {
		fileSystemCache.putToCache(0, VALUE1);
		assertEquals(VALUE1, fileSystemCache.getFromCache(0));
		assertNull(fileSystemCache.getFromCache(111));
	}

	@Test
	public void shouldNotRemoveObjectFromCacheIfNotExistsTest() {
		fileSystemCache.putToCache(0, VALUE1);
		assertEquals(VALUE1, fileSystemCache.getFromCache(0));
		assertEquals(1, fileSystemCache.getCacheSize());

		fileSystemCache.removeFromCache(5);
		assertEquals(VALUE1, fileSystemCache.getFromCache(0));
	}

	@Test
	public void shouldGetCacheSizeTest() {
		fileSystemCache.putToCache(0, VALUE1);
		assertEquals(1, fileSystemCache.getCacheSize());

		fileSystemCache.putToCache(1, VALUE2);
		assertEquals(2, fileSystemCache.getCacheSize());
	}

	@Test
	public void isObjectPresentTest() {
		assertFalse(fileSystemCache.isObjectPresent(0));

		fileSystemCache.putToCache(0, VALUE1);
		assertTrue(fileSystemCache.isObjectPresent(0));
	}

	@Test
	public void isEmptyPlaceTest() {
		fileSystemCache = new FileSystemCache<>(5);

		IntStream.range(0, 4).forEach(i -> fileSystemCache.putToCache(i, "String " + i));
		assertTrue(fileSystemCache.hasEmptyPlace());
		fileSystemCache.putToCache(5, "String");
		assertFalse(fileSystemCache.hasEmptyPlace());
	}

	@Test
	public void shouldClearCacheTest() {
		IntStream.range(0, 3).forEach(i -> fileSystemCache.putToCache(i, "String " + i));

		assertEquals(3, fileSystemCache.getCacheSize());
		fileSystemCache.clearCache();
		assertEquals(0, fileSystemCache.getCacheSize());
	}
}
