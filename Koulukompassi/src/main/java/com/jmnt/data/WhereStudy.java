package com.jmnt.data;

import java.util.ArrayList;
import java.util.List;


/**
 * The `WhereStudy` class represents a specific field of study and the universities that offer that field.
 */
public class WhereStudy {
    private String field;


    /**
     * Gets the list of universities offering the specified field of study.
     *
     * @return a list of university names offering this field.
     */
    public List<String> getUniversities() {
        return universities;
    }


    /**
     * Sets the list of universities offering the specified field of study.
     *
     * @param universities a list of university names to set for this field of study.
     */
    public void setUniversities(List<String> universities) {
        this.universities = universities;
    }

    /**
     * Gets the name of the academic field.
     *
     * @return the field of study.
     */
    public String getField() {
        return field;
    }


    /**
     * Sets the name of the academic field.
     *
     * @param field the field of study to set.
     */
    public void setField(String field) {
        this.field = field;
    }

    private List<String> universities =  new ArrayList<String>();
}
