package com.jmnt.data;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents the exam results data retrieved from the Vipunen API.
 * This class is mapped to the JSON structure of the API response using Jackson annotations.
 */
public class ExamResults {
    @JsonProperty("opiskelijaKoodi")
    private int studentCode;
    @JsonProperty("tutkintokertaKoodi")
    private String examPeriodCode;
    @JsonProperty("tutkintokerta")
    private String examPeriod;
    @JsonProperty("lukioKoodi")
    private String hsCode;
    @JsonProperty("lukio")
    private String highSchool;
    @JsonProperty("oppilaitostyyppiKoodi")
    private String schoolTypeCode;
    @JsonProperty("oppilaitostyyppi")
    private String schoolType;
    @JsonProperty("opetuskieliKoodi")
    private String langCode;
    @JsonProperty("opetuskieli")
    private String lang;
    @JsonProperty("sukupuoliKoodi")
    private String genderCode;
    @JsonProperty("sukupuoli")
    private String gender;
    @JsonProperty("koeKoodi")
    private String examCode;
    @JsonProperty("koe")
    private String exam;
    @JsonProperty("arvosanaKoodi")
    private String gradeCode;
    @JsonProperty("arvosana")
    private String grade;
    @JsonProperty("arvosanapisteet")
    private String gradePoints;
    @JsonProperty("tietojoukkoPaivitettyPvm")
    private String dateUpdated;

    public int getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(int studentCode) {
        this.studentCode = studentCode;
    }

    public String getExamPeriodCode() {
        return examPeriodCode;
    }

    public void setExamPeriodCode(String examPeriodCode) {
        this.examPeriodCode = examPeriodCode;
    }

    public String getExamPeriod() {
        return examPeriod;
    }

    public void setExamPeriod(String examPeriod) {
        this.examPeriod = examPeriod;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public String getSchoolTypeCode() {
        return schoolTypeCode;
    }

    public void setSchoolTypeCode(String schoolTypeCode) {
        this.schoolTypeCode = schoolTypeCode;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(String gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
