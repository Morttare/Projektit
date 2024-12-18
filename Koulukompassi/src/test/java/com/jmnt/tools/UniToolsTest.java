package com.jmnt.tools;

import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.data.University;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniToolsTest {

    @Test
    void testGetUniversityNames() {
        ParsedUniversityContext mockedContext = mock(ParsedUniversityContext.class);
        try (MockedStatic<ParsedUniversityContext> mockedStatic = mockStatic(ParsedUniversityContext.class)) {
            mockedStatic.when(ParsedUniversityContext::getInstance).thenReturn(mockedContext);
            University university1 = mock(University.class);
            when(university1.getName()).thenReturn("University A");
            University university2 = mock(University.class);
            when(university2.getName()).thenReturn("University B");
            Map<Integer, List<University>> mockUniversities = Map.of(2024, Arrays.asList(university1, university2));
            when(mockedContext.getUniversities()).thenReturn(mockUniversities);
            List<String> uniNames = UniTools.getUniversityNames();
            assertEquals(Arrays.asList("University A", "University B"), uniNames);
        }
    }

    @Test
    void testNormalizeString() {
        String input = "Är ÖppÅ";
        String expectedOutput = "ar oppa";
        assertEquals(expectedOutput, UniTools.normalizeString(input));
    }

}