package com.jmnt.excelparser;

import com.jmnt.data.University;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ExcelParserTest {

    @Test
    void testParseExcel() {
        List<University> universities = ExcelParser.parseExcel("src/main/resources/ApplicationResources/pisterajat_2024.xlsx");

        assertNotNull(universities);
        assertEquals(36, universities.size());

        University university = universities.get(0);
        assertEquals("Aalto-yliopisto", university.getName());
        assertEquals(44, university.getFields().size());

        University.FieldOfStudy field = university.getFields().get(0);
        assertEquals("Arkkitehtuuri, tekniikan kandidaatti ja arkkitehti (3 v + 2 v) - DIA-yhteisvalinta", field.getName());
        assertEquals(6, field.getAdmissionMethods().size());

        University.AdmissionMethod method = field.getAdmissionMethods().get(0);
        assertEquals("Koepisteet", method.getName());
        assertEquals(171.67, method.getRequiredPoints());
    }

}