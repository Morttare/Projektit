package com.jmnt.data;

import java.util.List;


/**
 * The `UniversityPoints` class stores information about university fields, programs,
 * and subject points for a particular university entry.
 * It encapsulates a list of university programs, subject points, and related data.
 */
public class UniversityPoints {

    /**
     * Gets the field of study.
     *
     * @return the field of study.
     */
    public String getField() {
        return field;
    }


    /**
     * Sets the field of study.
     *
     * @param field the field of study to set.
     */
    public void setField(String field) {
        this.field = field;
    }


    /**
     * Constructs a new `UniversityPoints` object with the provided field, group,
     * fieldsPoints, and tableIndex values.
     *
     * @param field the field of study.
     * @param group the list of university programs under this field.
     * @param fieldsPoints the list of subject points associated with this field.
     * @param tableIndex the index of the entry in the table.
     */
    public UniversityPoints(String field, List<UniversityProgram> group, List<SubjectPoints> fieldsPoints, int tableIndex) {
        this.field = field;
        this.group = group;
        this.fieldsPoints = fieldsPoints;
        this.tableIndex = tableIndex;
    }


    /**
     * Provides a string representation of the `UniversityPoints` object.
     *
     * @return a string containing field, group, fieldsPoints, and tableIndex.
     */
    @Override
    public String toString() {
        return "UniversityPoints{" +
                "field='" + field + '\'' +
                ", UniversityProgram='" + group + '\'' +
                ", SubjectPoints=" + fieldsPoints +
                ", tableIndex=" + tableIndex +
                '}';
    }

    private String field;


    /**
     * Gets the list of university programs associated with this field of study.
     *
     * @return a list of university programs (e.g., Bachelor's, Master's, etc.).
     */
    public List<UniversityProgram> getGroup() {
        return group;
    }


    /**
     * Sets the list of university programs.
     *
     * @param group a list of university programs to set.
     */
    public void setGroup(List<UniversityProgram> group) {
        this.group = group;
    }


    /**
     * Gets the list of subject points associated with this field of study.
     *
     * @return a list of subject points (mapping subjects to their respective grades and points).
     */
    public List<SubjectPoints> getFieldsPoints() {
        return fieldsPoints;
    }


    /**
     * Sets the list of subject points for this field.
     *
     * @param fieldsPoints a list of subject points to set.
     */
    public void setFieldsPoints(List<SubjectPoints> fieldsPoints) {
        this.fieldsPoints = fieldsPoints;
    }

    private List<UniversityProgram> group;
    private List<SubjectPoints> fieldsPoints;

    /**
     * Gets the table index for this university points entry.
     *
     * @return the table index.
     */
    public int getTableIndex() {
        return tableIndex;
    }


    /**
     * Sets the table index for this university points entry.
     *
     * @param tableIndex the index to set for this entry.
     */
    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    private int tableIndex;
}
