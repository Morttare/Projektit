package com.jmnt.data;

import com.jmnt.tools.UniTools;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * This service class interacts with the Nominatim API (OpenStreetMap) to search for locations.
 * It ensures that requests are made with a delay to comply with the Nominatim API's rate-limiting policy (1 request per second).
 */
@Service
public class NominatimApiCaller {
    private final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private long lastRequestTime = 0;


    /**
     * Searches for a location using the Nominatim API.
     *
     * @param location the location to search for.
     * @return an array of Place objects representing the search results.
     */
    public Place[] searchLocation(String location) {
        RestTemplate restTemplate = new RestTemplate();
        Place[] places = new Place[0];
        try {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastRequest = currentTime - lastRequestTime;
            if (timeSinceLastRequest < 1000) {
                // Sleep for 1 second on total because of Nominatim API usage policy (max 1 request per second).
                Thread.sleep(1000 - timeSinceLastRequest);
            }

            lastRequestTime = System.currentTimeMillis();

            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                    .queryParam("q", URLEncoder.encode(UniTools.normalizeString(location), StandardCharsets.UTF_8.toString()))
                    .queryParam("format", "json")
                    .toUriString();
            // Make the API call and map the response to an array of Place objects
            places = restTemplate.getForObject(url, Place[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error making nominatim API call", e);
        }
        if (places.length == 0) {
            System.out.println("No places found for " + location);
        }

        return places;
    }
}
