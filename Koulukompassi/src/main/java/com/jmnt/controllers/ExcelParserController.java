package com.jmnt.controllers;

import com.jmnt.data.University;
import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for handling requests related to university data parsed from Excel files.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/excel")
public class ExcelParserController {

    @Autowired
    private UniversityService universityService;

    /**
     * Retrieves a list of universities for the year 2024.
     *
     * @return a {@link ResponseEntity} containing a list of {@link University} objects
     *         or an HTTP status of INTERNAL_SERVER_ERROR if an error occurs.
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/universities")
    public ResponseEntity<List<University>> getUniversities() {
        try {
            List<University> universities = universityService.getUniversities(2024);
            return ResponseEntity.ok(universities);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves details of a university by its name for the year 2024.
     *
     * @param name the name of the university.
     * @return a {@link ResponseEntity} containing the {@link University} object
     *         or an HTTP status of NOT_FOUND if the university is not found.
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/university/{name}")
    public ResponseEntity<University> getUniversity(@PathVariable String name) {
        try {
            ParsedUniversityContext data = ParsedUniversityContext.getInstance();
            for (University university:data.getUniversities().get(2024)){
                if (university.getName().equals(name)){
                    return ResponseEntity.ok(university);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found");
    }

    /**
     * Retrieves university data for a specific year by its name.
     *
     * @param name the name of the university.
     * @param year the year of the data to retrieve.
     * @return a {@link ResponseEntity} containing the {@link University} object
     *         or an HTTP status of NOT_FOUND if the university is not found for the given year.
     */
    public ResponseEntity<University> getUniversityDataForYear(@PathVariable String name,int year) {
        try {
            ParsedUniversityContext data = ParsedUniversityContext.getInstance();
            for (University university:data.getUniversities().get(year)){
                if (university.getName().equals(name)){
                    return ResponseEntity.ok(university);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found");
    }


    /**
     * Retrieves a mapping of admission methods for a specific university, field of study,
     * and admission method across years from 2020 to 2024.
     *
     * @param name the name of the university.
     * @param field the field of study.
     * @param admissionMethod the admission method.
     * @return a {@link ResponseEntity} containing a map where the key is the year
     *         and the value is the corresponding {@link University.AdmissionMethod}.
     */
    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/graph/{name}/{field}/{admissionMethod}")
    public ResponseEntity<Map<Integer,University.AdmissionMethod>> getGraph(@PathVariable String name
            ,@PathVariable String field,@PathVariable String admissionMethod) {
        Map<Integer,University> universityMap = new HashMap<>(Map.of());
        for (int yr=2020;yr<2025;yr++){
            universityMap.put(yr,getUniversityDataForYear(name,yr).getBody());
        }
        Map<Integer,University.AdmissionMethod> admissionMap = new HashMap<>(Map.of());
        for (Integer year:universityMap.keySet()){
            University yearData=universityMap.get(year);
            for (University.FieldOfStudy fi:yearData.getFields()){
                if (fi.getName().equals(field)){
                    for (University.AdmissionMethod am:fi.getAdmissionMethods()){
                        if (am.getName().equals(admissionMethod)){
                            admissionMap.put(year,am);
                        }
                    }
                }
            }
        }
        return ResponseEntity.ok(admissionMap);
    }
}
