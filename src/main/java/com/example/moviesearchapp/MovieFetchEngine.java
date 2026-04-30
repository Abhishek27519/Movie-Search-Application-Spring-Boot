package com.example.moviesearchapp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MovieFetchEngine {

    private final RestTemplate restClient;
    private static final String API_ENDPOINT = "https://jsonmock.hackerrank.com/api/moviesdata/search/";

    public MovieFetchEngine(RestTemplate restTemplate) {
        this.restClient = restTemplate;
    }

    /**
     * Orchestrates multiple threads to scrape all movie pages simultaneously.
     */
    public List<Object> retrieveAllMovieRecords() {
        // Fetch the initial payload to determine scope
        Map<String, Object> initialPayload = restClient.getForObject(API_ENDPOINT, Map.class);
        int totalPages = (int) initialPayload.get("total_pages");

        List<CompletableFuture<Map>> pendingRequests = new ArrayList<>();

        // Queue up background tasks for each page
        for (int pageIndex = 1; pageIndex <= totalPages; pageIndex++) {
            String pageUrl = API_ENDPOINT + "?page=" + pageIndex;

            pendingRequests.add(CompletableFuture.supplyAsync(() ->
                    restClient.getForObject(pageUrl, Map.class)
            ));
        }

        // Aggregate results as they return from threads
        return pendingRequests.stream()
                .map(CompletableFuture::join)
                .map(pageResponse -> pageResponse.get("data"))
                .collect(Collectors.toList());
    }

    /**
     * Handles specific filtered searches based on user-provided criteria.
     */
    public Object findMoviesByCriteria(String title, Integer releaseYear, Integer pageNumber) {
        StringBuilder queryBuilder = new StringBuilder(API_ENDPOINT + "?");

        if (title != null) queryBuilder.append("Title=").append(title).append("&");
        if (releaseYear != null) queryBuilder.append("Year=").append(releaseYear).append("&");
        if (pageNumber != null) queryBuilder.append("page=").append(pageNumber);

        return restClient.getForObject(queryBuilder.toString(), Object.class);
    }
}