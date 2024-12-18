package com.jmnt.controllers;

import com.jmnt.data.*;
import com.jmnt.tools.UniTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * Controller for managing API endpoints related to location data for universities and high schools.
 * Uses the Nominatim API to fetch geographical coordinates and associated information for educational institutions.
 */
@RestController
public class NominatimApiController {
    private final NominatimApiCaller nominatimApiCaller;


    /**
     * Constructor for NominatimApiController.
     *
     * @param nominatimApiCaller the service used to query location data via the Nominatim API.
     */
    @Autowired
    public NominatimApiController(NominatimApiCaller nominatimApiCaller) {
        this.nominatimApiCaller = nominatimApiCaller;
    }


    /**
     * Searches and retrieves coordinates for a list of school names.
     *
     * @param schoolNames a list of school names to search for.
     * @return a map where the key is the school name, and the value is another map containing campus names and their information.
     */
    public Map<String, Map<String, CampusInfo>> searchAllCoordinates(List<String> schoolNames) {
        Map<String, Map<String, CampusInfo>> coordMap = new HashMap<>();

        CoordinateCache coordinateCache = new CoordinateCache();
        Map<String, Map<String, CampusInfo>> cache = coordinateCache.getCache();

        for (String schoolName : schoolNames) {

            if (cache != null) {
                if (cache.containsKey(schoolName)) {
                    coordMap.put(schoolName, cache.get(schoolName));
                    continue;
                }
            }

            Place[] result = nominatimApiCaller.searchLocation(schoolName);

            // We want the empty school entries for cache but not for the actual JSON response to frontend.
            cache.put(schoolName, new HashMap<>());

            if (result.length == 0) {
                continue;
            }

            Map<String, CampusInfo> innerMap = new HashMap<>();
            for (Place place : result) {

                String[] parts = place.getDisplay_name().split(", ");
                if (place.getAddresstype().equals("amenity") && parts[parts.length-1].equals("Suomi / Finland")) {
                    Coord coord = new Coord(place.getLat(), place.getLon());
                    CampusInfo campusInfo;

                    // Nominatim API gets some of the schools' types wrong, so they need to be manipulated in some cases
                    if (!place.getType().equals("college") && (schoolName.contains("mmattikorkeakoulu") || place.getName().contains("mmattikorkeakoulu"))) {
                        campusInfo = new CampusInfo(coord, "college");
                    } else {
                        campusInfo = new CampusInfo(coord, place.getType());
                    }

                    innerMap.put(place.getName(), campusInfo);
                    cache.get(schoolName).put(place.getName(), campusInfo);
                }
            }
            coordMap.put(schoolName, innerMap);
        }
        coordinateCache.saveCache();
        return coordMap;
    }


    /**
     * API endpoint to retrieve coordinates for all universities.
     *
     * @return a map where the key is the university name, and the value is another map containing campus names and their information.
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/unicoordinates")
    public Map<String, Map<String, CampusInfo>> getUniCoordinates() {
        return searchAllCoordinates(UniTools.getUniversityNames());
    }


    /**
     * API endpoint to retrieve coordinates for all high schools.
     *
     * @return a map where the key is the high school name, and the value is another map containing campus names and their information.
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/hscoordinates")
    public Map<String, Map<String, CampusInfo>> getHsCoordinates() {
        return searchAllCoordinates(UniTools.fetchHsNames());
    }
}
