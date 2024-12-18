package com.jmnt.data;

import java.util.*;
import static com.jmnt.tools.GradeTools.GRADES;


/**
 * The `SubjectPoints` class stores and manages points assigned to various subjects, organized by grades.
 * It allows updating and retrieving the points for subjects based on the assigned grade.
 */
public class SubjectPoints {

    /**
     * Gets the points for each subject, sorted by grade.
     *
     * @return a map of subjects with grades as keys and points as values.
     */
    public Map<String, TreeMap<Character, Float>> getFieldsPoints() {
        return fieldsPoints;
    }

    private Map<String, TreeMap<Character, Float>> fieldsPoints;


    /**
     * Gets the table index.
     *
     * @return the index of the table (can represent a specific entry in a collection or list).
     */
    public int getTableIndex() {
        return tableIndex;
    }


    /**
     * Sets the table index.
     *
     * @param tableIndex the index to set for this entry.
     */
    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }


    /**
     * Sets the points for each subject.
     *
     * @param fieldsPoints a map of subjects with grades as keys and points as values.
     */
    public void setFieldsPoints(Map<String, TreeMap<Character, Float>> fieldsPoints) {
        this.fieldsPoints = fieldsPoints;
    }


    private int tableIndex;


    /**
     * Gets the number of subjects to consider for some selection.
     *
     * @return the maximum number of subjects to consider.
     */
    public int getBestOf() {
        return bestOf;
    }


    /**
     * Sets the number of subjects to consider for some selection.
     *
     * @param bestOf the number of subjects to select.
     */
    public void setBestOf(int bestOf) {
        this.bestOf = bestOf;
    }

    public int bestOf;


    /**
     * Default constructor initializes the fieldsPoints map and sets default values for tableIndex and bestOf.
     */
    public SubjectPoints() {
        this.fieldsPoints = new TreeMap<>();
        this.tableIndex = 0;
        this.bestOf = 0;
    }


    /**
     * Updates or adds a grade for a given subject.
     * If the subject does not exist, it is added, and the grade is inserted with the associated points.
     *
     * @param subject the name of the subject.
     * @param grade the grade assigned to the subject.
     * @param points the points associated with the grade.
     */
    public void updateGrade(String subject, Character grade, Float points) {
        addSubject(subject);
        fieldsPoints.get(subject).put(grade, points);
    }


    /**
     * Adds a new subject to the fieldsPoints map if it does not already exist.
     *
     * @param subject the name of the subject to be added.
     */
    public void addSubject(String subject) {
        if (!fieldsPoints.containsKey(subject)) {
            fieldsPoints.put(subject, new TreeMap<>(GRADE_COMPARATOR));
        }
    }

    // Constant for comparing grade characters in a specific order.
    private static final Comparator<Character> GRADE_COMPARATOR = new Comparator<Character>() {
        @Override
        public int compare(Character g1, Character g2) {
            return Integer.compare(GRADES.indexOf(g1), GRADES.indexOf(g2));
        }
    };


    /**
     * Provides a string representation of the `SubjectPoints` object, including the table index,
     * the bestOf value, and the points for each subject.
     *
     * @return a string representation of the `SubjectPoints` object.
     */
    @Override
    public String toString() {
        return "SubjectPoints{" +
                "tableIndex=" + tableIndex + ", " + "bestOf: " + bestOf +
                ", fieldsPoints=" + fieldsPoints +
                '}';
    }
}
