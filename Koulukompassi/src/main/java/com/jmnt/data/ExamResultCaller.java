package com.jmnt.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


/**
 * A service class that interacts with the Vipunen API to fetch examination results,
 * school statistics, and student data.
 */
@Service
public class ExamResultCaller {

    private  String VIPUNEN_URL;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;


    /**
     * Constructs an instance of ExamResultCaller with a pre-configured HTTP client and JSON mapper.
     */
    public ExamResultCaller() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Fetches exam results from the Vipunen API.
     *
     * @return an array of ExamResults containing the exam data. Returns an empty array if the request fails.
     */
    public ExamResults[] searchExams() {

        VIPUNEN_URL  = "https://api.vipunen.fi/api/resources/ytl_arvosanat/data?filter=koe%3D%3D%27%C3%84idinkieli%2C%20suomi%27%20and%20tutkintokertaKoodi%3D%3D%272021K%27%20and%20arvosana%3D%3D%27Lubenter%20approbatur%27&sort=%28%2Blukio%29&";
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VIPUNEN_URL))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ExamResults[].class);
            } else {
                System.err.println("Failed to fetch data. HTTP Status: " + response.statusCode());
                return new ExamResults[0];
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ExamResults[0];
        }
    }


    /**
     * Fetches exam statistics for a specific school and year from the Vipunen API.
     *
     * @param schoolname the name of the school.
     * @param year       the year of the exam session.
     * @return an array of ExamResults containing the statistics. Returns an empty array if the request fails.
     */
    public ExamResults[] searchStats(String schoolname, String year) {

        schoolname = schoolname.replaceAll(" ", "%20");
        VIPUNEN_URL  = "https://api.vipunen.fi/api/resources/ytl_arvosanat/data?filter=lukio%3D%3D%27" + schoolname + "%27%20and%20%28tutkintokertaKoodi%3D%3D%27" + year + "K%27%20or%20tutkintokertaKoodi%3D%3D%27" + year + "S%27%29&sort=%28-arvosanapisteet%29&offset=0&";

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VIPUNEN_URL))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ExamResults[].class);
            } else {
                System.err.println("Failed to fetch stats. HTTP Status: " + response.statusCode());
                return new ExamResults[0];
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ExamResults[0];
        }
    }


    /**
     * Fetches the number of students who took exams for a specific school and year.
     *
     * @param schoolname the name of the school.
     * @param year       the year of the exam session.
     * @return the number of students or -1 if the request fails.
     */
    public int searchStudents(String schoolname, String year) {

        schoolname = schoolname.replaceAll(" ", "%20");
        VIPUNEN_URL = "https://api.vipunen.fi/api/resources/ytl_arvosanat/data/count?filter=lukio%3D%3D%27" + schoolname + "%27%20and%20%28tutkintokertaKoodi%3D%3D%27" + year + "K%27%20or%20tutkintokertaKoodi%3D%3D%27" + year + "S%27%29%20and%20%28koeKoodi%3D%3D%27A%27%20or%20koeKoodi%3D%3D%27A5%27%29";
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VIPUNEN_URL))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Integer.parseInt(response.body());
            } else {
                System.err.println("Failed to fetch students. HTTP Status: " + response.statusCode());
                return -1;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
