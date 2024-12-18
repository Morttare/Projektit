package com.jmnt.tools;

import com.jmnt.data.SubjectPoints;
import com.jmnt.data.UniversityProgram;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jmnt.tools.GradeTools.GRADES;


/**
 * A utility class for web scraping and extracting specific data from university program and subject information web pages.
 * It uses the JSoup library to fetch and parse HTML documents, and includes methods to extract university program data, subject points data,
 * table data, and field names from web pages.
 */
public class WebScraper {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
            + " AppleWebKit/537.36 (KHTML, like Gecko)"
            + " Chrome/58.0.3029.110 Safari/537.3";
    private static final int TIMEOUT = 5000;
    public static final List<String> NUMBERS = Arrays.asList(new String [] {
            "yksi","kaksi", "kolme", "neljä", "viisi", "kuusi", "seitsemän"});
    public static final List<String> FORBIDDEN_WORDS = Arrays.asList(new String [] {
            "Sisällysluettelo", "Hakukohde", "Yliopisto", "Kynnysehdot"});
    public static final List<String> SUBJECTS = Arrays.asList(new String [] {
            "äidinkieli", "biologia", "filosofia", "fysiikka", "historia", "lyhyt matematiikka", "pitkä matematiikka", "kemia", "kieli, pitkä",
    "kieli, keskipitkä", "kieli, lyhyt", "maantiede", "psykologia", "terveystieto", "uskonto", "yhteiskuntaoppi"});
    private static final Pattern NUMBER_WORD_PATTERN = Pattern.compile("Pisteytystaulukko:\\s+(yksi|kaksi|kolme|neljä|viisi|kuusi|seitsemän|kahdeksan|yhdeksän)");

    private static final Map<String, Integer> FI_NUMBER_WORDS = new HashMap<>();
    private static final int h2Difference = -2;

    static {
        FI_NUMBER_WORDS.put("yksi", 1);
        FI_NUMBER_WORDS.put("kaksi", 2);
        FI_NUMBER_WORDS.put("kolme", 3);
        FI_NUMBER_WORDS.put("neljä", 4);
        FI_NUMBER_WORDS.put("viisi", 5);
        FI_NUMBER_WORDS.put("kuusi", 6);
        FI_NUMBER_WORDS.put("seitsemän", 7);
        FI_NUMBER_WORDS.put("kahdeksan", 8);
        FI_NUMBER_WORDS.put("yhdeksän", 9);
    }


    /**
     * Converts a Finnish number word to its corresponding integer value.
     *
     * @param word the Finnish word to convert
     * @return the integer value of the word, or null if the word is not recognized
     */
    public static Integer convertToNumber(String word) {
        if (word == null) return null;
        return FI_NUMBER_WORDS.get(word.toLowerCase());
    }


    /**
     * Extracts the number word from a string, if present.
     *
     * @param header the header string to extract the number word from
     * @return the extracted number word or "yksi" if "parhaat" is found, or null if no match is found
     */
    public static String extractNumberWord(String header) {
        if (header == null) return null;
        Matcher matcher = NUMBER_WORD_PATTERN.matcher(header);
        if (matcher.find()) {
            return matcher.group(1);
        }
        else if(header.contains("parhaat")) {
            return "yksi";
        }
        return null;
    }


    /**
     * Fetches an HTML document from the given URL.
     *
     * @param url the URL to fetch the document from
     * @return the parsed HTML document
     * @throws IOException if an error occurs while fetching the document
     */
    public static Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .get();
    }


    /**
     * Extracts the main field names (h1 tags) from the document, filtering out forbidden words.
     *
     * @param doc the HTML document to extract field names from
     * @return a list of extracted field names
     */
    public static List<String> getMainFieldNames(Document doc) {
        List<String> h1Texts = new ArrayList<>();

        Elements h1Elements = doc.select("h1");

        for (Element h1 : h1Elements) {
            String text = h1.text().trim();
            if (!text.isEmpty() && !FORBIDDEN_WORDS.contains(text)) {
                h1Texts.add(text);
            }
        }

        return h1Texts;
    }


    /**
     * Extracts the subfield names (h2 tags) from the document, filtering out forbidden words.
     *
     * @param doc the HTML document to extract subfield names from
     * @return a list of extracted subfield names
     */
    public static List<String> getSubFieldNames(Document doc) {
        List<String> h2Texts = new ArrayList<>();

        Elements h2Elements = doc.select("h2");

        for (Element h2 : h2Elements) {
            String text = h2.text().trim();
            if (!text.isEmpty() && !FORBIDDEN_WORDS.contains(text)) {
                h2Texts.add(text);
            }
        }

        return h2Texts;
    }


    /**
     * Extracts the best points from the "h4" elements in the document.
     *
     * @param doc the HTML document to extract points from
     * @return a list of best points represented as integers
     */
    public static List<Integer> getBestPointsFromTableAmount(Document doc) {
        List<Integer> bestPointsFromTableAmount = new ArrayList<>();

        Elements h4Elements = doc.select("h4");

        for (Element h4 : h4Elements) {
            String text = h4.text().trim();
            for(String number : NUMBERS) {
                if(text.contains(number)) {
                    int index = NUMBERS.indexOf(number);
                    bestPointsFromTableAmount.add(index + 1);
                }
            }
        }
        return bestPointsFromTableAmount;
    }


    /**
     * Extracts all tables from the document and returns their data as a nested list structure.
     *
     * @param doc the HTML document to extract tables from
     * @return a list of tables, where each table is represented by a list of rows and columns
     */
    public static List<List<List<String>>> extractTables(Document doc) {
        List<List<List<String>>> tablesData = new ArrayList<>();

        Elements tables = doc.select("table");

        for (Element table : tables) {
            List<List<String>> tableData = new ArrayList<>();
            Elements rows = table.select("tr");

            for (Element row : rows) {
                List<String> rowData = new ArrayList<>();

                Elements headers = row.select("th");
                if (!headers.isEmpty()) {
                    for (Element header : headers) {
                        rowData.add(header.text().trim());
                    }
                } else {
                    Elements cols = row.select("td");
                    for (Element col : cols) {
                        rowData.add(col.text().trim());
                    }
                }

                if (!rowData.isEmpty()) {
                    tableData.add(rowData);
                }
            }

            if (!tableData.isEmpty()) {
                tablesData.add(tableData);
            }
        }

        return tablesData;
    }


    /**
     * Determines the index of a specific table within the document.
     *
     * @param doc the HTML document
     * @param target the target table element
     * @return the index of the table in the document
     */
    public static int getTableIndex(Document doc, Element target) {
        int count = 0;
        for (Element elem : doc.getAllElements()) {
            if (elem.tagName().equalsIgnoreCase("h2")) {
                count++;
            }
            if (elem.equals(target)) {
                break;
            }
        }
        return count;
    }


    /**
     * Extracts university program data from the document and returns a list of UniversityProgram objects.
     *
     * @param doc the HTML document containing university program data
     * @return a list of UniversityProgram objects
     */
    public static List<UniversityProgram> getUniversityProgramData(Document doc) {
        List<UniversityProgram> programData = new ArrayList<>();

        Elements hakukohde = doc.select("[data-mtr-content=Hakukohde]");
        Elements yliopisto = doc.select("[data-mtr-content=Yliopisto]");
        Elements kynnysehdot = doc.select("[data-mtr-content=Kynnysehdot]");

        Elements h2Elements = doc.select("h2");

        int minSize = Math.min(hakukohde.size(), Math.min(yliopisto.size(), kynnysehdot.size()));
        for (int i = 0; i < minSize; i++) {
            if(FORBIDDEN_WORDS.contains(hakukohde.get(i).text()) ||
                    FORBIDDEN_WORDS.contains(yliopisto.get(i).text()) ||
                    FORBIDDEN_WORDS.contains(kynnysehdot.get(i).text())) {continue;}
            String program = hakukohde.get(i).text().trim();
            String university = yliopisto.get(i).text().trim();
            Map<String, Character> required_grades = new HashMap<>();
            String[] same_grades = kynnysehdot.get(i).text().trim().split("\\s+ja\\s+");
            String[] or_grades = kynnysehdot.get(i).text().trim().split("\\s+tai\\s+");
            boolean same_grade = false;
            boolean or_grade = false;
            Character earlier_grade = '-';

            Element currentHakukohde = hakukohde.get(i);
            int tableIndex = getTableIndex(doc, currentHakukohde) + h2Difference;

            if(same_grades.length >= 2) {
                same_grade = true;
            }
            else if(or_grades.length >= 2) {
                or_grade = true;
            }

            for (int j = same_grades.length - 1; j >= 0; j--) {
                String subjectFound = null;
                Character gradeFound = '-';
                var part = same_grades[j];

                for (String subject : SUBJECTS) {
                    if (part.toLowerCase().contains(subject.toLowerCase())) {
                        subjectFound = subject;
                        if(same_grade) {
                            required_grades.put(subjectFound, earlier_grade);
                        }
                        break;
                    }
                }

                if (subjectFound != null) {
                    String gradeRegexString = "arvosanalla\\s+([LEMCBA])";
                    Pattern gradePattern = Pattern.compile(gradeRegexString, Pattern.CASE_INSENSITIVE);
                    Matcher gradeMatcher = gradePattern.matcher(part);

                    boolean found = gradeMatcher.find();

                    if (found) {
                        String matchedGrade = gradeMatcher.group(1);

                        gradeFound = matchedGrade.charAt(0);

                        if (same_grade || or_grade) {
                            earlier_grade = gradeFound;
                        }
                    }
                    else {
                        if (same_grade || or_grade) {
                            required_grades.put(subjectFound, earlier_grade);
                            continue;
                        }
                    }

                    required_grades.put(subjectFound, gradeFound);
                }
            }

            UniversityProgram universityProgram = new UniversityProgram(program, university, required_grades, tableIndex, or_grade);
            programData.add(universityProgram);
        }
        return programData;
    }


    /**
     * Extracts subject points data from the document, including the best of points from tables.
     *
     * @param doc the HTML document containing subject points data
     * @return a list of SubjectPoints objects
     */
    public static List<SubjectPoints> getSubjectPointsData(Document doc) {
        List<SubjectPoints> subjectPointsData = new ArrayList<>();

        Elements wrappers = doc.select("div.wpb_wrapper");
        int currentIndex = h2Difference;

        for (Element wrapper : wrappers) {
            Elements children = wrapper.children();
            boolean isPisteytysTaulukkoFound = false;
            var word = "-";
            int number = 0;

            for (Element child : children) {
                if (child.tagName().equalsIgnoreCase("h2")) {
                    currentIndex++;
                }
                if (child.tagName().equalsIgnoreCase("h4") && child.text().contains("Pisteytystaulukko")) {
                    isPisteytysTaulukkoFound = true;
                }
                if (isPisteytysTaulukkoFound) {
                    if(child.tagName().equalsIgnoreCase("h4") && child.text().contains("hakijalle")) {
                        word = extractNumberWord(child.text());
                        number = convertToNumber(word);
                    }
                    if (child.tagName().equalsIgnoreCase("table")) {
                        SubjectPoints parsedTable = parseTable(child, currentIndex, number);
                        subjectPointsData.add(parsedTable);
                    }
                }
            }
        }

        return subjectPointsData;
    }


    /**
     * Parses a table to extract subject points and grades.
     *
     * @param table the table element to parse
     * @param index the index of the table
     * @param bestof the "best of" number for points
     * @return a SubjectPoints object containing the parsed data
     */
    private static SubjectPoints parseTable(Element table, int index, int bestof) {
        SubjectPoints subjectPoints = new SubjectPoints();
        subjectPoints.setTableIndex(index);
        subjectPoints.setBestOf(bestof);

        Elements headerRows = table.select("thead tr");
        if (headerRows.isEmpty()) {
            return subjectPoints;
        }

        Element headerRow = headerRows.first();
        Elements headers = headerRow.select("th");

        List<Character> grades = new ArrayList<>();

        for (int i = 1; i < headers.size(); i++) {
            String gradeText = headers.get(i).text().trim().toUpperCase();
            if (gradeText.length() == 1 && GRADES.contains(gradeText.charAt(0))) {
                grades.add(gradeText.charAt(0));
            } else {
                grades.add('?');
            }
        }

        Elements dataRows = table.select("tbody tr");
        for (Element row : dataRows) {
            Elements cells = row.select("td");

            if (cells.size() < grades.size() + 1) {
                continue;
            }

            String subject = cells.get(0).text().trim();
            for (int i = 1; i < cells.size(); i++) {
                String pointsStr = cells.get(i).text().trim();

                Character grade = grades.get(i - 1);

                pointsStr = pointsStr.replace(',', '.');

                Float points = 0.0f;
                try {
                    points = Float.parseFloat(pointsStr);
                } catch (NumberFormatException e) {
                    continue;
                }

                subjectPoints.updateGrade(subject, grade, points);
            }
        }

        return subjectPoints;
    }
}
