package com.jmnt.controllers;

import com.jmnt.data.CombinedUniversityData;
import com.jmnt.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * This controller handles requests related to combined university data.
 * It fetches the data using the `UniversityService` and returns it as a RESTful API response.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CombinedDataController {

    private final UniversityService universityService;


    /**
     * Constructor to inject the UniversityService into the controller.
     *
     * @param universityService the UniversityService used to fetch combined data.
     */
    @Autowired
    public CombinedDataController(UniversityService universityService) {
        this.universityService = universityService;
    }


    /**
     * Handles GET requests to retrieve combined university data.
     *
     * @return a ResponseEntity containing a list of CombinedUniversityData or an error response.
     */
    @GetMapping("/api/combined")
    public ResponseEntity<List<CombinedUniversityData>> getCombinedData() {
        try {
            List<CombinedUniversityData> combinedData = universityService.getCombinedData();
            return ResponseEntity.ok(combinedData);
        } catch (Exception e) {
            System.out.println(e);
            List<CombinedUniversityData> combinedData = new ArrayList<>();
            CombinedUniversityData combinedUniversityData = new CombinedUniversityData();
            combinedUniversityData.setUniversityName("Error university!");
            combinedData.add(combinedUniversityData);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(combinedData);
        }
    }
}
