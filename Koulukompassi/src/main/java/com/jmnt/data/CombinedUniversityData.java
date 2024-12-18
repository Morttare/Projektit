package com.jmnt.data;

import java.util.List;


/**
 * Represents combined data about a university, including its name, top field of study,
 * program information, admission methods, and subject-specific points.
 */
public class CombinedUniversityData {
    private String universityName;


    /**
     * Retrieves the top field of study associated with the university.
     *
     * @return the top field of study.
     */
    public String getTopField() {
        return topField;
    }

    /**
     * Sets the top field of study associated with the university.
     *
     * @param topField the top field of study.
     */
    public void setTopField(String topField) {
        this.topField = topField;
    }

    private String topField;


    /**
     * Retrieves the name of the field from which points are derived.
     *
     * @return the points field name.
     */
    public String getPointsFieldName() {
        return pointsFieldName;
    }


    /**
     * Sets the name of the field from which points are derived.
     *
     * @param pointsFieldName the points field name.
     */
    public void setPointsFieldName(String pointsFieldName) {
        this.pointsFieldName = pointsFieldName;
    }

    private String pointsFieldName;


    /**
     * Retrieves the name of the scraped program.
     *
     * @return the name of the scraped program.
     */
    public String getScrapedProgramName() {
        return scrapedProgramName;
    }


    /**
     * Sets the name of the scraped program.
     *
     * @param scrapedProgramName the name of the scraped program.
     */
    public void setScrapedProgramName(String scrapedProgramName) {
        this.scrapedProgramName = scrapedProgramName;
    }

    private String scrapedProgramName;
    private String fieldName;
    private List<University.AdmissionMethod> admissionMethods; // From Excel data


    /**
     * Retrieves the list of subject-specific points associated with the university.
     *
     * @return a list of subject points.
     */
    public List<SubjectPoints> getUniversityPoints() {
        return universityPoints;
    }


    /**
     * Sets the list of subject-specific points for the university.
     *
     * @param universityPoints a list of subject points.
     */
    public void setUniversityPoints(List<SubjectPoints> universityPoints) {
        this.universityPoints = universityPoints;
    }


    /**
     * Retrieves the list of admission methods associated with the university.
     *
     * @return a list of admission methods.
     */
    public List<University.AdmissionMethod> getAdmissionMethods() {
        return admissionMethods;
    }


    /**
     * Sets the list of admission methods for the university.
     *
     * @param admissionMethods a list of admission methods.
     */
    public void setAdmissionMethods(List<University.AdmissionMethod> admissionMethods) {
        this.admissionMethods = admissionMethods;
    }


    /**
     * Retrieves the name of the academic field.
     *
     * @return the name of the field.
     */
    public String getFieldName() {
        return fieldName;
    }


    /**
     * Sets the name of the academic field.
     *
     * @param fieldName the name of the field.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Retrieves the name of the university.
     *
     * @return the name of the university.
     */
    public String getUniversityName() {
        return universityName;
    }


    /**
     * Sets the name of the university.
     *
     * @param universityName the name of the university.
     */
    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    private List<SubjectPoints> universityPoints; // From UniversityRequirements data
}
