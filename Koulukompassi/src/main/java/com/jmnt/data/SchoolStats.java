package com.jmnt.data;

import java.util.Map;

/**
 * The `SchoolStats` class represents statistical data for a school or educational institution.
 * It contains information about the number of students, the average grade, and a breakdown of exam grades.
 */
public class SchoolStats {

    private int studentCount;
    private float averageGrade;
    private Map<String, Integer> examGrades;

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(float averageGrade) {
        this.averageGrade = averageGrade;
    }


    /**
     * Gets a map of exam grades, where the key is the exam name and the value is the number of students
     * who achieved that grade in the exam.
     *
     * @return a map containing exam grades and the number of students with each grade.
     */
    public Map<String, Integer> getExamGrades() {
        return examGrades;
    }


    /**
     * Sets the map of exam grades, where the key is the exam name and the value is the number of students
     * who achieved that grade in the exam.
     *
     * @param examGrades the map of exam grades to set.
     */
    public void setExamGrades(Map<String, Integer> examGrades) {
        this.examGrades = examGrades;
    }
}
