package com.jmnt.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;


/**
 * This class handles caching of university points data. It stores a collection of university points,
 * which is mapped by university name or other identifier to the corresponding university point data.
 * The data is serialized to and deserialized from a JSON file.
 * It extends the generic Cache class to handle caching functionality for university points.
 */
public class PointsCache extends Cache<Collection<UniversityPoints>> {
    private Map<String, Collection<UniversityPoints>> cache;
    private final static String CACHE_FILE_PATH = "src/main/java/com/jmnt/cache/uniPointsCache.json";


    /**
     * Default constructor that initializes the cache with the specified cache file path and cache type.
     */
    public PointsCache() {
        super(CACHE_FILE_PATH, getCacheType());
    }


    /**
     * Returns the type for deserialization, which is a map where the key is a String (university identifier)
     * and the value is a collection of UniversityPoints.
     *
     * @return the type for the cache.
     */
    private static Type getCacheType() {
        return new TypeToken<Map<String, Collection<UniversityPoints>>>() {}.getType();
    }


    /**
     * Saves the current cache data to the cache file.
     * This method calls the saveCache() method from the parent Cache class.
     */
    public void save() {
        saveCache();
    }
}
