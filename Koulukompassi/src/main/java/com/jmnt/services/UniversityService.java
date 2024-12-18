package com.jmnt.services;

import com.jmnt.data.*;
import com.jmnt.tools.WebScraper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.FuzzyScore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service class for handling university data, including scraping information from external sources,
 * combining scraped data with existing data, and processing university requirements.
 */
@Service
public class UniversityService {

    private static final Logger logger = LoggerFactory.getLogger(UniversityService.class);

    public static final List<String> SCRAPED_WEBSITES = Arrays.asList(
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/biokemia-biologia-ja-ymparistotieteet-seka-biolaaketiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/diplomi-insinoori-ja-arkkitehtikoulutus",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/elintarviketiede-ja-ravitsemustiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/farmasia-laaketieteelliset-alat-ja-terveystieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/filosofia-historia-ja-teologia",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/fysikaaliset-tieteet-kemia-ja-matemaattiset-tieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/geotieteet-ja-maantiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/informaatioverkostot-tietojenkasittelytiede-ja-tietojarjestelmatiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kasvatusala",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kauppatieteet-liiketaloustiede-ja-tietojenkasittelytiede-oikeustiede-seka-taloustiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kirjallisuustieteet-kulttuurintutkimus-ja-taiteen-tutkimus-seka-saamelainen-kulttuuri",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kotimaiset-kielet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/liikunnan-yhteiskuntatieteet-liikuntabiologia-ja-liikuntapedagogiikka",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/logopedia-ja-psykologia",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/maataloustieteet-ja-metsatieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/vieraat-kielet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/viestintatieteet-ja-yhteiskuntatieteet"
    );

    public static final Set<String> FORBIDDEN_WORDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "maisterihaku",
            "kandidatprogrammet",
            "magisterprogrammet",
            "master's",
            "master of science",
            "kandidat",
            "ammattikorkeakoulu",
            "amk",
            "bachelor",
            "magister"
    )));

    public static final String FILE_LOCATION = "src/main/java/com/jmnt/cache/";

    public static int CURRENT_YEAR = 2024;

    private static final long DELAY_MS = 2000;
    private static final boolean CACHE_ENABLED = false;

    private static int HEURISTIC_THRESHOLD = 15;


    /**
     * Retrieves a list of WhereStudy objects, each containing a field of study and the associated universities.
     * This method processes the university data to organize it by fields of study and returns a list of relevant fields
     * and universities.
     *
     * @return a list of WhereStudy objects
     */
    public List<WhereStudy> getWhereStudy() {
        ParsedUniversityContext data = ParsedUniversityContext.getInstance();
        List<UniversityTopField> scraped = data.getScrapedData();

        List<WhereStudy> whereStudyList = new ArrayList<>();

        for(UniversityTopField field : scraped) {
            WhereStudy whereStudy = new WhereStudy();
            whereStudy.setField(field.getField());

            if(field.getField().equals("Diplomi-insinööri- ja arkkitehtikoulutus")) {
                for(String engineer_university : EngineerFields.UNIVERSITY_FIELDS_MAP.keySet()) {
                    whereStudy.getUniversities().add(engineer_university);
                }
                whereStudyList.add(whereStudy);
                continue;
             }

            for(var unidata : field.getUniversityData()) {
                for(var group : unidata.getGroup()) {
                    if(!whereStudy.getUniversities().contains(group.getUniversity())) {
                        whereStudy.getUniversities().add(group.getUniversity());
                    }
                }
            }
            whereStudyList.add(whereStudy);
        }

        return whereStudyList;
    }

    public List<WhereStudy> getWhereStudyProgrammes() {
        ParsedUniversityContext data = ParsedUniversityContext.getInstance();
        List<UniversityTopField> scraped = data.getScrapedData();

        List<WhereStudy> whereStudyList = new ArrayList<>();

        for(UniversityTopField field : scraped) {
            for(UniversityPoints points : field.getUniversityData()) {
                String pointfield = points.getField();

                // special case for engineering fields
                if(pointfield.equals("Diplomi-insinöörikoulutus")) {
                    WhereStudy whereStudy = new WhereStudy();
                    for(WhereStudy stud : whereStudyList) {
                        if(!stud.getField().equals(pointfield)) {
                            whereStudy.setField("Diplomi-insinööri- ja arkkitehtikoulutus");
                            for (Map.Entry<String,List<String>> entry : EngineerFields.UNIVERSITY_FIELDS_MAP.entrySet()) {
                                whereStudy.getUniversities().add(entry.getKey());
                            }
                            whereStudyList.add(whereStudy);
                            break;
                        }
                    }
                }

                for(UniversityProgram program : points.getGroup()) {
                    String univ = program.getUniversity();
                    Boolean found = false;

                    for(WhereStudy stud : whereStudyList) {
                        if(stud.getField().equals(pointfield)) {
                            stud.getUniversities().add(univ);
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        WhereStudy whereStudy = new WhereStudy();
                        whereStudy.setField(pointfield);
                        whereStudy.getUniversities().add(univ);
                        whereStudyList.add(whereStudy);
                    }
                }
            }
        }

        return whereStudyList;
    }


    /**
     * Retrieves the list of universities for a specified year from the parsed data context.
     *
     * @param year the year for which university data is requested
     * @return a list of universities for the specified year, or an empty list if no data exists
     */
    public List<University> getUniversities(int year) {
        ParsedUniversityContext data = ParsedUniversityContext.getInstance();
        List<University> universities = data.getUniversities().get(year);
        if (universities == null) {
            return new ArrayList<>();
        }
        return universities;
    }


    /**
     * Scrapes and processes university requirements data from predefined websites.
     * This method fetches the university program data and extracts relevant information like fields,
     * subject points, and program names.
     *
     * @return a list of UniversityTopField objects containing the scraped university requirements data
     */
    public static List<UniversityTopField> getUniversityRequirements() {
        List<UniversityTopField> universityTops = new ArrayList<>();

        PointsCache pointsCache = new PointsCache();
        Map<String, Collection<UniversityPoints>> cache = pointsCache.getCache();

        for (int i = 0; i < SCRAPED_WEBSITES.size(); i++) {
            UniversityTopField universityTop = new UniversityTopField();
            List<UniversityPoints> universityPoints = new ArrayList<>();
            boolean doc_exists = false;

            Document doc = null;
            try {
                String[] parts = SCRAPED_WEBSITES.get(i).split("/");
                String name = parts[parts.length - 1];
                File f = new File(FILE_LOCATION + name);
                if (f.exists()) {
                    String content = FileUtils.readFileToString(f, "UTF-8");
                    doc = Jsoup.parse(content);
                    doc_exists = true;
                } else {
                    doc = WebScraper.fetchDocument(SCRAPED_WEBSITES.get(i));
                    FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
                }
            } catch (IOException e) {
                continue;
            }

            List<String> mainHeaders = WebScraper.getMainFieldNames(doc);
            if (mainHeaders.size() < 2) {
                continue;
            }
            String headerKeyOriginal = mainHeaders.get(1).trim();
            String headerKey = normalizeFieldName(headerKeyOriginal);
            universityTop.setField(headerKeyOriginal);

            if (cache != null && CACHE_ENABLED && cache.containsKey(headerKey)) {
                Collection<UniversityPoints> cachedPoints = cache.get(headerKey);
                List<UniversityPoints> cachedList = new ArrayList<>(cachedPoints);

                universityTop.setUniversityData(cachedList);
                universityTops.add(universityTop);
                continue;
            }

            List<String> subHeaders = WebScraper.getSubFieldNames(doc);
            List<UniversityProgram> program_data = WebScraper.getUniversityProgramData(doc);
            List<SubjectPoints> subject_points = WebScraper.getSubjectPointsData(doc);

            for (int j = 0; j < subHeaders.size(); j++) {
                List<UniversityProgram> program = new ArrayList<>();
                List<SubjectPoints> points = new ArrayList<>();
                for (UniversityProgram programData : program_data) {
                    if (programData.getTableIndex() == j) {
                        program.add(programData);
                    }
                }
                for (SubjectPoints subjectPoint : subject_points) {
                    if (subjectPoint.getTableIndex() == j) {
                        points.add(subjectPoint);
                    }
                }
                String subHeader = subHeaders.get(j);
                if(subHeader.equals("Kiintiöt ja kynnysehdot")) {
                    subHeader = "Erityispedagogiikka ja erityisopettaja sekä opinto-ohjaaja ja uraohjaaja";
                }
                else if(subHeader.equals("Todistusvalinnan pisteytys: kasvatusala")) {
                    subHeader = "Kasvatusala";
                }
                UniversityPoints universityProgram = new UniversityPoints(subHeader, program, points, i);
                universityPoints.add(universityProgram);
            }

            universityTop.setUniversityData(universityPoints);
            universityTops.add(universityTop);
            cache.put(headerKey, universityPoints);

            try {
                if (!doc_exists) {
                    Thread.sleep(DELAY_MS);
                }
            } catch (InterruptedException e) {
                logger.error("Thread interrupted while sleeping: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        pointsCache.save();
        return universityTops;
    }

    static class RelevantData {
        RelevantData(List<SubjectPoints> subject_points, String field, String category) {
            this.subject_points_ = subject_points;
            this.field_ = field;
            this.poinst_category_ = category;
        }

        public List<SubjectPoints> getSubject_points_() {
            return subject_points_;
        }

        public String getField_() {
            return field_;
        }

        List<SubjectPoints> subject_points_;
        String field_;

        public String getPoinst_category_() {
            return poinst_category_;
        }

        String poinst_category_;
    }


    /**
     * Combines university data from Excel files with the scraped university program data.
     * This method matches universities to their respective fields and creates a list of CombinedUniversityData objects.
     *
     * @return a list of CombinedUniversityData objects containing combined university and program information
     */
    public List<CombinedUniversityData> getCombinedData() {
        List<University> excelUniversities = getUniversities(CURRENT_YEAR);
        List<CombinedUniversityData> combined = new ArrayList<>();

        List<University> filteredUniversities = excelUniversities.stream()
                .filter(univ -> {
                    String lowerName = univ.getName().toLowerCase();
                    return FORBIDDEN_WORDS.stream().noneMatch(lowerName::contains);
                })
                .collect(Collectors.toList());
        List<UniversityTopField> requirementsData = getUniversityRequirements();
        // fields from 0 to n
        for (University university : filteredUniversities) {
            String uniName = university.getName().toLowerCase();

            Map<String, RelevantData> scraped_points = new HashMap<>();

            for(UniversityTopField scraped : requirementsData) {
                for(var universityData : scraped.getUniversityData()) {
                    for(var group : universityData.getGroup()) {
                        if(group.getUniversity().toLowerCase().equals(uniName)) {
                            RelevantData data = new RelevantData(universityData.getFieldsPoints(), universityData.getField(), scraped.getField());
                            scraped_points.put(group.getProgram(), data);
                            break;
                        }
                    }
                }
            }
            var fields = university.getFields();

            for(var field : fields) {
                Locale finnishLocale = new Locale("fi", "FI");
                FuzzyScore fuzzyScore = new FuzzyScore(finnishLocale);

                for (Map.Entry<String, RelevantData> entry : scraped_points.entrySet()) {
                    int score = fuzzyScore.fuzzyScore(entry.getKey(), field.getName());

                    if(field.getName().contains("DIA-yhteisvalinta")) {
                        var university_engineer_fields = EngineerFields.UNIVERSITY_FIELDS_MAP.get(university.getName());
                        int score2 = 0;
                        String best_scored_field = null;
                        for(String engineer_field : university_engineer_fields) {
                            int new_score = fuzzyScore.fuzzyScore(engineer_field, field.getName());
                            if(new_score > score2) {
                                score2 = new_score;
                                best_scored_field = engineer_field;
                            }
                        }
                        if(score2 >= HEURISTIC_THRESHOLD) {
                            CombinedUniversityData combinedUniversityData = new CombinedUniversityData();
                            combinedUniversityData.setUniversityName(university.getName());
                            combinedUniversityData.setFieldName(field.getName());
                            combinedUniversityData.setUniversityPoints(requirementsData.get(1).getUniversityData().getFirst().getFieldsPoints());
                            combinedUniversityData.setTopField("Diplomi-insinööri- ja arkkitehtikoulutus");
                            combinedUniversityData.setScrapedProgramName(best_scored_field);
                            combinedUniversityData.setAdmissionMethods(field.getAdmissionMethods());
                            combinedUniversityData.setPointsFieldName(entry.getValue().getPoinst_category_());

                            combined.add(combinedUniversityData);
                        }

                        break;
                    }

                    if(score >= HEURISTIC_THRESHOLD) {
                        CombinedUniversityData combinedUniversityData = new CombinedUniversityData();
                        combinedUniversityData.setUniversityName(university.getName());
                        combinedUniversityData.setFieldName(field.getName());
                        combinedUniversityData.setUniversityPoints(entry.getValue().getSubject_points_());
                        combinedUniversityData.setTopField(entry.getValue().getField_());
                        combinedUniversityData.setScrapedProgramName(entry.getKey());
                        combinedUniversityData.setAdmissionMethods(field.getAdmissionMethods());
                        combinedUniversityData.setPointsFieldName(entry.getValue().getPoinst_category_());

                        combined.add(combinedUniversityData);
                    }
                }
            }
        }

        return combined;
    }


    /**
     * Normalizes a field name by trimming whitespace, converting to lowercase,
     * and removing punctuation characters.
     *
     * @param fieldName the field name to normalize
     * @return the normalized field name
     */
    private static String normalizeFieldName(String fieldName) {
        if (fieldName == null) return "";
        return fieldName.trim().toLowerCase()
                .replaceAll("[,\\.]", "")
                .replaceAll("\\s+", " ");
    }
}