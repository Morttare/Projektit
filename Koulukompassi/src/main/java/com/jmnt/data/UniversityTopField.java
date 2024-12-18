package com.jmnt.data;

import java.util.List;


/**
 * The `UniversityTopField` class represents a top academic field
 * and contains a list of universities along with their associated points for that field.
 */
public class UniversityTopField {
    private String field;


    /**
     * Gets the name of the academic field.
     *
     * @return the field name.
     */
    public String getField() {
        return field;
    }


    /**
     * Sets the name of the academic field.
     *
     * @param field the field name to set.
     */
    public void setField(String field) {
        this.field = field;
    }


    /**
     * Gets the list of university programs and points associated with this academic field.
     *
     * @return the list of `UniversityPoints` associated with the field.
     */
    public List<UniversityPoints> getUniversityData() {
        return universityData;
    }


    /**
     * Sets the list of university programs and points for the academic field.
     *
     * @param universityData the list of `UniversityPoints` to set for this field.
     */
    public void setUniversityData(List<UniversityPoints> universityData) {
        this.universityData = universityData;
    }

    private List<UniversityPoints> universityData;
}
