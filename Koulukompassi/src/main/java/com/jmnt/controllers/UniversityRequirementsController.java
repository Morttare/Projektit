package com.jmnt.controllers;

import com.jmnt.data.*;
import com.jmnt.services.UniversityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * REST controller that provides endpoints to fetch university requirements and study options.
 * This controller integrates with the UniversityService to retrieve and process data.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UniversityRequirementsController {


    @Autowired
    private UniversityService universityService;

    /**
     * Fetches university requirements data from the service layer.
     *
     * @return A ResponseEntity containing a list of UniversityTopField objects
     *         or an error response in case of failure.
     */
    @GetMapping("/api/points")
    public ResponseEntity<List<UniversityTopField>> getRequirementsData() {
        try {
            List<UniversityTopField> data = universityService.getUniversityRequirements();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error fetching requirements data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Fetches data about where students can study specific fields.
     *
     * @return A ResponseEntity containing a list of WhereStudy objects
     *         or an error response in case of failure.
     */
    @GetMapping("/api/wherestudy")
    public ResponseEntity<List<WhereStudy>> getWhereStudyData() {
        try {
            List<WhereStudy> data = universityService.getWhereStudy();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error fetching wherestudy data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Fetches data about specific study programs for given fields.
     *
     * @return A ResponseEntity containing a list of WhereStudy objects
     *         or an error response in case of failure.
     */
    @GetMapping("/api/wherestudyspecific")
    public ResponseEntity<List<WhereStudy>> getWhereStudySpecificData() {
        try {
            List<WhereStudy> data = universityService.getWhereStudyProgrammes();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error fetching getWhereStudySpecificData data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
