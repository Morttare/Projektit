package com.jmnt.controllers;

import com.jmnt.data.ExamResultCaller;
import com.jmnt.data.ExamResults;
import com.jmnt.data.SchoolStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Controller class for handling API endpoints related to exam results and school statistics.
 * Provides endpoints to retrieve school information, exam grades, and student grade statistics.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ExamResultController {

    private final ExamResultCaller caller;
    private final ExamResults[] results;

    /**
     * Constructor for the ExamResultController.
     *
     * @param caller the service used to fetch exam results and related data.
     */
    @Autowired
    public ExamResultController(ExamResultCaller caller) {
        this.caller = caller;
        this.results = caller.searchExams();
    }

    /**
     * Custom comparator for sorting grades in a specific order.
     */
    private static class CustomGradeComparator implements Comparator<String> {
        private static final List<String> gradeOrder = Arrays.asList("I", "A", "B", "C", "M", "E", "L");

        /**
         * Compares two grade codes based on a predefined order.
         *
         * @param name1 the first grade code.
         * @param name2 the second grade code.
         * @return a negative integer, zero, or a positive integer as the first grade precedes,
         *         is equal to, or follows the second grade in the order.
         */
        @Override
        public int compare(String name1, String name2) {
            int index1 = gradeOrder.indexOf(name1);
            int index2 = gradeOrder.indexOf(name2);
            return Integer.compare(index1, index2);
        }
    }

    /**
     * Retrieves a list of high schools from the exam results.
     *
     * @return a list of unique high school names.
     */
    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/api/schools")
    public ArrayList<String> getSchools(){
        ArrayList<String> schools = new ArrayList<>();
        for (ExamResults result : results){
            if (!schools.contains(result.getHighSchool())){
                schools.add(result.getHighSchool());
            }
        }
        return schools;
    }


    /**
     * Calculates statistics for a given school and year.
     *
     * @param school the name of the school.
     * @param year   the year for which statistics are to be calculated.
     * @return a SchoolStats object containing exam and student statistics.
     */
    public SchoolStats getStats(String school, String year){

        ExamResults[] results = caller.searchStats(school, year);
        int numberOfStudents = caller.searchStudents(school, year);
        float numberOfExams = 0;
        float totalGrades = 0;

        Map<String, Integer> grades = new TreeMap<>(new CustomGradeComparator());
        SchoolStats stats = new SchoolStats();

        for (ExamResults result : results){

            grades.merge(result.getGradeCode(), 1, Integer::sum);
            numberOfExams++;
            totalGrades += Integer.parseInt(result.getGradePoints());

        }

        float average = totalGrades / numberOfExams;
        average = Math.round(average * 100) / 100.0f;
        stats.setStudentCount(numberOfStudents);
        stats.setAverageGrade(average);
        stats.setExamGrades(grades);
        return stats;
    }


    /**
     * Retrieves a map of exam grades for a specific school and year.
     *
     * @param school the name of the school.
     * @param year   the year for which exam grades are to be retrieved.
     * @return a ResponseEntity containing a map of grade codes to their frequencies.
     */
    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/api/stats/{school}/{year}/examgrades")
    public ResponseEntity<Map<String, Integer>> getExamGrades(@PathVariable String school, @PathVariable String year){
        SchoolStats stats = getStats(school, year);
        return ResponseEntity.ok(stats.getExamGrades());
    }


    /**
     * Retrieves student count and average grade for a specific school and year.
     *
     * @param school the name of the school.
     * @param year   the year for which student grades are to be retrieved.
     * @return a ResponseEntity containing a map with student count and average grade.
     */
    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/api/stats/{school}/{year}/studentgrades")
    public ResponseEntity<Map<Integer, Float>> getStudentGrades(@PathVariable String school, @PathVariable String year){
        SchoolStats stats = getStats(school, year);
        Map<Integer, Float> pair = new HashMap<>();
        pair.put(stats.getStudentCount(), stats.getAverageGrade());
        return ResponseEntity.ok(pair);
    }

}
