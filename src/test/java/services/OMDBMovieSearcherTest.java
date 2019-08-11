package services;

import model.Movie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OMDBMovieSearcherTest {

    @Test
    void getMovieByTitle() {
        String title = "Infinity";
        Movie film = OMDBMovieSearcher.getMovieByTitle(title);
        assertEquals(title, film.getTitle());
    }

    @Test
    void getMovieByImdbID() {
        String id = "0120915";
        Movie film = OMDBMovieSearcher.getMovieByImdbID(id);
        assertEquals("tt" + id, film.getImdbID());
    }
    @Test
    void getMovieByImdbIDAsync() throws Exception
    {
        String id = "0851851";
        CompletableFuture<Movie> future = CompletableFuture.supplyAsync(() -> OMDBMovieSearcher.getMovieByImdbID(id));
        Movie film = future.get();
        System.out.println(film.getTitle());
    }

    @Test
    void getMovies() {
        MovieSearcher searcher = new OMDBMovieSearcher();
        ArrayList<Movie> list = searcher.getMovies(new HashSet<>(Arrays.asList("1185834", "0120915", "2527338", "0080684", "0121765", "0121766", "2930604", "0086190", "0076759", "2527336", "2488496", "8336340")));
        assertTrue(list.get(0).getTitle().contains("Star Wars"));
    }
}