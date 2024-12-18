package com.jmnt.data;

import java.util.Map;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * Represents a specialized cache for storing and retrieving school coordinates.
 * Each school name maps to a collection of campus names, each associated with its coordinates and type.
 * The cache is persisted to a JSON file for reuse.
 */
public class CoordinateCache extends Cache<Map<String, CampusInfo>> {
    // For every school name, there is map containing campus names and corresponding coordinates.
    private Map<String, Map<String, CampusInfo>> cache;
    private final static String CACHE_FILE_PATH = "src/main/java/com/jmnt/cache/schoolCoordinatesCache.json";


    /**
     * Constructs a new CoordinateCache, loading data from a predefined JSON file.
     */
    public CoordinateCache() {
        super(CACHE_FILE_PATH, getCacheType());
    }


    /**
     * Returns the type information for the cache, which is a map of school names to campus details.
     *
     * @return the Type of the cache data, used for JSON deserialization.
     */
    private static Type getCacheType() {
        return new TypeToken<Map<String, Map<String, CampusInfo>>>(){}.getType();
    }
}
