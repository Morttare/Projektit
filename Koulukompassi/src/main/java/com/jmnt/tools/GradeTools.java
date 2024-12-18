package com.jmnt.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A utility class that provides predefined constants and helper methods related to grades.
 * This class is not meant to be instantiated.
 */
public class GradeTools {

    /**
     * A constant list of grade characters used in the application.
     * The grades represent various academic performance levels.
     */
    public static final List<Character> GRADES = Collections.unmodifiableList(
            Arrays.asList('A', 'B', 'C', 'M', 'E', 'L')
    );


    /**
     * Private constructor to prevent instantiation of this utility class.
     * Since the class is meant for utility purposes only, instantiating it is not allowed.
     *
     * @throws UnsupportedOperationException if the constructor is called
     */
    private GradeTools() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
