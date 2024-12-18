package com.jmnt.tools;

import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.data.University;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Utility class providing tools for managing and retrieving university-related data.
 */
public class UniTools {

    /**
     * Retrieves a list of university names for the year 2024.
     *
     * @return a list of university names as String
     */
    public static List<String> getUniversityNames(){
        ParsedUniversityContext context = ParsedUniversityContext.getInstance();
        Map<Integer, List<University>> uniMap = context.getUniversities();
        List<String> uniNames = new ArrayList<String>();
        for (University uni : uniMap.get(2024)){
            uniNames.add(uni.getName());
        }

        return uniNames;
    }

    /**
     * Normalizes a string by converting it to lowercase and replacing special Finnish characters
     * (ä, ö, å) with their base ASCII equivalents (a, o, a).
     *
     * @param str the input string to be normalized.
     * @return the normalized string.
     */
    public static String normalizeString(String str){
        str = str.toLowerCase();
        return str.replace('ä', 'a')
                .replace('ö', 'o')
                .replace('å', 'a');
    }

    /**
     * Fetches a list of high school (HS) names and returns them
     *
     * @return a list of high school names as String
     * @throws RuntimeException if no names are found from the API response.
     */
    public static List<String> fetchHsNames() {
        RestTemplate restTemplate = new RestTemplate();
        String[] HsNames = restTemplate.getForObject("http://localhost:8080/api/schools", String[].class);
        if (HsNames != null){
            return Arrays.asList(HsNames);
        } else {
            throw new RuntimeException("No names found");
        }
    }
}
