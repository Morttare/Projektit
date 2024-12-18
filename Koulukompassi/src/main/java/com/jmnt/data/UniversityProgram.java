package com.jmnt.data;

import java.util.Map;


/**
 * The `UniversityProgram` class represents a university program and its associated
 * properties, including the program name, the university offering it, the required grades
 * for each subject, and other configuration flags.
 */
public class UniversityProgram {

    /**
     * Gets the name of the program.
     *
     * @return the program name.
     */
    public String getProgram() {
        return program;
    }

    /**
     * Sets the name of the program.
     *
     * @param program the program name to set.
     */
    public void setProgram(String program) {
        this.program = program;
    }


    /**
     * Gets the university offering this program.
     *
     * @return the university name.
     */
    public String getUniversity() {
        return university;
    }


    /**
     * Sets the university offering this program.
     *
     * @param university the university name to set.
     */
    public void setUniversity(String university) {
        this.university = university;
    }


    /**
     * Adds a required grade for a specific subject to the program.
     *
     * @param subject the subject name (e.g., Mathematics).
     * @param grade the required grade for the subject (e.g., 'A', 'B').
     */
    public void addGrade(String subject, Character grade) {
        this.requiredGrades.put(subject, grade);
    }


    /**
     * Constructs a new `UniversityProgram` with the specified program name, university,
     * required grades, table index, and OR flag.
     *
     * @param program the name of the program (e.g., Computer Science).
     * @param university the university offering the program (e.g., University of Helsinki).
     * @param requiredGrades a map of required grades for specific subjects.
     * @param tableIndex the index of the program in a table (used for sorting).
     * @param or_flag a flag indicating whether the required grades are specified with an OR condition.
     */
    public UniversityProgram(String program, String university, Map<String, Character> requiredGrades, int tableIndex, boolean or_flag) {
        this.program = program;
        this.university = university;
        this.requiredGrades = requiredGrades;
        this.tableIndex = tableIndex;
        this.or_flag = or_flag;
    }


    /**
     * Provides a string representation of the `UniversityProgram` object.
     *
     * @return a string containing the program, university, required grades, and table index.
     */
    @Override
    public String toString() {
        return "UniversityProgram{" +
                "program='" + program + '\'' +
                ", university='" + university + '\'' +
                ", requiredGrades=" + requiredGrades +
                ", tableIndex=" + tableIndex +
                '}';
    }

    private String program;
    private String university;


    /**
     * Checks if the OR flag is set. This flag indicates whether the required grades for
     * the subjects are specified with an OR condition.
     *
     * @return true if the OR flag is set, false otherwise.
     */
    public boolean isOr_flag() {
        return or_flag;
    }


    /**
     * Sets the OR flag. This flag indicates whether the required grades for
     * the subjects are specified with an OR condition.
     *
     * @param or_flag the OR flag value to set.
     */
    public void setOr_flag(boolean or_flag) {
        this.or_flag = or_flag;
    }

    private boolean or_flag;


    /**
     * Gets the table index for this program (used for sorting or referencing).
     *
     * @return the table index.
     */
    public int getTableIndex() {
        return tableIndex;
    }


    /**
     * Sets the table index for this program.
     *
     * @param tableIndex the index to set.
     */
    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    private int tableIndex = 0;


    /**
     * Gets the required grades for subjects associated with this program.
     *
     * @return a map of subjects and their required grades.
     */
    public Map<String, Character> getRequiredGrades() {
        return requiredGrades;
    }

    /**
     * Sets the required grades for subjects associated with this program.
     *
     * @param requiredGrades a map of subjects and their required grades to set.
     */
    public void setRequiredGrades(Map<String, Character> requiredGrades) {
        this.requiredGrades = requiredGrades;
    }

    private Map<String, Character> requiredGrades;
}
