package com.jmnt.data;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Generic cache implementation for storing and retrieving key-value pairs with file persistence.
 * Uses JSON format for saving and loading cache data.
 *
 * @param <T> the type of the value stored in the cache.
 */
public class Cache<T> {
    private Map<String, T> cache;
    private final String filePath;
    private final Type typeT;


    /**
     * Constructs a new Cache object.
     *
     * @param filePath the file path where the cache is stored.
     * @param typeT    the type of the cache values, used for deserialization.
     */
    public Cache(String filePath, Type typeT) {
        this.filePath = filePath;
        this.typeT = typeT;
        this.cache = loadCacheFromFile();
    }


    /**
     * Loads the cache from the specified file path.
     *
     * @return a map containing the cache data, or an empty map if the file is not found or cannot be read.
     */
    private Map<String, T> loadCacheFromFile() {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            cache = gson.fromJson(reader, typeT);
            if (cache == null) {
                cache = new HashMap<>();
            }
        } catch (IOException e) {
            System.out.println("Error reading " + filePath);
            cache = new HashMap<>();
        }
        return cache;
    }


    /**
     * Saves the current cache to the specified file path in JSON format.
     */
    public void saveCache() {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new Gson();
            gson.toJson(this.cache, typeT, writer);
        } catch (IOException e) {
            System.out.println("Error saving cache to " + filePath);
        }
    }


    /**
     * Retrieves the entire cache as a map.
     *
     * @return a map containing all key-value pairs in the cache.
     */
    public Map<String, T> getCache() {
        return cache;
    }


    /**
     * Updates the cache with a new key-value pair.
     *
     * @param key   the key for the cache entry.
     * @param value the value to associate with the given key.
     */
    public void updateCache(String key, T value) {
        cache.put(key, value);
    }


    /**
     * Retrieves a value from the cache by its key.
     *
     * @param key the key associated with the value to retrieve.
     * @return the value associated with the given key, or {@code null} if the key is not found.
     */
    public T get(String key) {
        return cache.get(key);
    }
}
