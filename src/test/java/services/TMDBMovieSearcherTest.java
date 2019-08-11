package services;

import model.TMDB.TMDBMovie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TMDBMovieSearcherTest {

    @Test
    void getInternalIds() {
        // Фильмы
        assertFalse(TMDBMovieSearcher.getSearchResultByRussian("Терминатор").getResults().isEmpty());
    }

    @Test
    void getImdbIds() {
        ArrayList<TMDBMovie> internalIds = TMDBMovieSearcher.getSearchResultByRussian("Терминатор").getResults();

//        assertFalse(TMDBMovieSearcher.getImdbId(internalIds).isEmpty());
    }
}