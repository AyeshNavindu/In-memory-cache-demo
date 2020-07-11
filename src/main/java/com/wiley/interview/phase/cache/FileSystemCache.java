package com.wiley.interview.phase.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.SneakyThrows;
import lombok.val;

public class FileSystemCache<K extends Serializable, V extends Serializable> implements CacheSystem<K, V> {
	private final Map<K, String> objectsStorage;
	private final Path tempDir;
	private int capacity;

	private static final Logger logger = LoggerFactory.getLogger(FileSystemCache.class);

	@SneakyThrows
	public FileSystemCache() {
		this.tempDir = Files.createTempDirectory("cache");
		this.tempDir.toFile().deleteOnExit();
		this.objectsStorage = new ConcurrentHashMap<>();
	}

	@SneakyThrows
	public FileSystemCache(int capacity) {
		this.tempDir = Files.createTempDirectory("cache");
		this.tempDir.toFile().deleteOnExit();
		this.capacity = capacity;
		this.objectsStorage = new ConcurrentHashMap<>(capacity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized V getFromCache(K key) {
		if (isObjectPresent(key)) {
			val fileName = objectsStorage.get(key);
			try (val fileInputStream = new FileInputStream(new File(tempDir + File.separator + fileName));
					val objectInputStream = new ObjectInputStream(fileInputStream)) {
				return (V) objectInputStream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				logger.info("Can't read a file.", fileName, e.getMessage());
			}
		}
		return null;

	}

	@Override
	@SneakyThrows
	public synchronized void putToCache(K key, V value) {
		val tmpFile = Files.createTempFile(tempDir, "", "").toFile();

		try (val outputStream = new ObjectOutputStream(new FileOutputStream(tmpFile))) {
			outputStream.writeObject(value);
			outputStream.flush();
			objectsStorage.put(key, tmpFile.getName());
		} catch (IOException e) {
			logger.info("Can't write an object to a file " + tmpFile.getName() + ": " + e.getMessage());
		}
	}

	@Override
	public synchronized void removeFromCache(K key) {
		val fileName = objectsStorage.get(key);
		val deletedFile = new File(tempDir + File.separator + fileName);
		if (deletedFile.delete()) {
			logger.info("Cache file has been deleted" + fileName);
		} else {
			logger.info("Can't delete a file " + fileName);
		}
		objectsStorage.remove(key);
	}

	@Override
	public int getCacheSize() {
		return objectsStorage.size();
	}

	@Override
	public boolean isObjectPresent(K key) {
		return objectsStorage.containsKey(key);
	}

	@Override
	public boolean hasEmptyPlace() {
		return getCacheSize() < this.capacity;
	}

	@SneakyThrows
	@Override
	public void clearCache() {
		Files.walk(tempDir).filter(Files::isRegularFile).map(Path::toFile).forEach(file -> {
			if (file.delete()) {
				logger.info("Cache file has been deleted" + file);
			} else {
				logger.info("Can't delete a file " + file);
			}
		});
		objectsStorage.clear();
	}
}
