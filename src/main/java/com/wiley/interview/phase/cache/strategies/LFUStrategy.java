package com.wiley.interview.phase.cache.strategies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class LFUStrategy<K, V> implements CacheStrategy<K, V> {

	private final int capacity;
	private Map<K, V> storage;
	private Map<K, Long> elementCount;
	private TreeMap<Long, HashSet<K>> sortedElements;

	public LFUStrategy(int maxSize) {
		this.capacity = maxSize;
		this.storage = new HashMap<>();
		this.elementCount = new HashMap<>();
		this.sortedElements = new TreeMap<>();
	}
	

	private void evictionStrategy() {
		Long minFrequency = sortedElements.firstKey();
		K evictionKey = sortedElements.get(minFrequency).iterator().next();
		removeFromSortedFrequencies(evictionKey, minFrequency);
		elementCount.remove(evictionKey);
		storage.remove(evictionKey);
	}

	private void frequencyIncrement(K key) {
		Long frequency = elementCount.compute(key, (k, f) -> f + 1L);
		removeFromSortedFrequencies(key, frequency - 1);
		sortedElements.computeIfAbsent(frequency, keys -> new HashSet<>()).add(key);
	}

	private void removeFromSortedFrequencies(K key, Long frequency) {
		if (sortedElements.get(frequency).size() > 1) {
			sortedElements.get(frequency).remove(key);
		} else {
			sortedElements.remove(frequency);
		}
	}

	@Override
	public V put(K key, V value) {
		if (storage.size() == capacity) {
			evictionStrategy();
		}
		V oldValue = storage.put(key, value);
		Long frequency = elementCount.computeIfAbsent(key, f -> 1L);
		sortedElements.computeIfAbsent(frequency, keys -> new HashSet<>()).add(key);
		return oldValue;
	}

	@Override
	public V get(K key) {
		V value = storage.get(key);
		if (value != null) {
			frequencyIncrement(key);
		}
		return value;
	}

	@Override
	public int size() {
		return storage.size();
	}

	@Override
	public String toString() {
		return storage.toString();
	}

}
