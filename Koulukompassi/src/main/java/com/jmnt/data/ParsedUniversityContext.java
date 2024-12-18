package com.jmnt.data;
import com.jmnt.excelparser.ExcelParser;
import com.jmnt.services.UniversityService;

import java.util.List;
import java.util.Map;

/**
 * Singleton class that provides a shared context for parsed university data.
 * It retrieves university data from Excel files and additional scraped data
 * about university requirements.
 */
public class ParsedUniversityContext {
    private static ParsedUniversityContext instance;

    /**
     * Map containing university data organized by year.
     * The key represents the year, and the value is a list of {@link University} objects.
     */
    public Map<Integer, List<University>> getUniversities() {
        return universities;
    }

    private Map<Integer, List<University>> universities;

    public List<UniversityTopField> getScrapedData() {
        return scrapedData;
    }

    private List<UniversityTopField> scrapedData;

    /**
     * Private constructor to initialize university data from the Excel parser
     * and the scraped data from {@link UniversityService}.
     */
    private ParsedUniversityContext() {
        universities = ExcelParser.parse();
        scrapedData = UniversityService.getUniversityRequirements();
    };

    /**
     * Retrieves the singleton instance of {@code ParsedUniversityContext}.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of {@code ParsedUniversityContext}.
     */
    public static ParsedUniversityContext getInstance() {
        if (instance == null) {
            instance = new ParsedUniversityContext();
        }
        return instance;
    }
}
