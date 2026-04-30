package com.example.moviesearchapp;

import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Main entry point for our movie search tool.
 * This class handles the incoming web requests and routes them
 * to the fetch engine for processing.
 */
@RestController
@RequestMapping("/api/movies")
public class MovieSearchApi {

    private final MovieFetchEngine dataScanner;

    // Injecting the engine via constructor - standard practice for better testability
    public MovieSearchApi(MovieFetchEngine dataScanner) {
        this.dataScanner = dataScanner;
    }

    /**
     * This endpoint handles the "heavy lifting" by requesting every single
     * page of movie data from the external API concurrently.
     */
    @GetMapping
    public List<Object> listAllMoviesFound() {
        // We're letting the engine handle the multithreading complexity here
        return dataScanner.retrieveAllMovieRecords();
    }

    /**
     * This method handles specific searches. I've used the 'params' attribute
     * here to distinguish this from the general fetch above, as per the
     * requirement for handling different arguments on the same URL.
     */
    @GetMapping(params = {"Title"})
    public Object findMoviesBySpecificDetails(
            @RequestParam(required = false) String Title,
            @RequestParam(required = false, name = "Year") Integer yearFilter,
            @RequestParam(required = false, name = "page") Integer targetPage) {

        // Pass the filters down to the engine's criteria method
        return dataScanner.findMoviesByCriteria(Title, yearFilter, targetPage);
    }
}