package services;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WikiMovieSearcherTest {

    @Test
    void getWikiTitles() {
        String search = "Терминатор 2";
        ArrayList<String> wikiTitles = WikiMovieSearcher.getWikiTitles(search);
        assertEquals(true, wikiTitles.contains("Терминатор 2: Судный день"));
    }

    @Test
    void getMoviesImdbIDs() {
        String search = "Звездные Войны";
        ArrayList<String> wikiTitles = WikiMovieSearcher.getWikiTitles(search);

        HashMap<String, String> moviesImdbIDs = WikiMovieSearcher.getMoviesImdbIDs(wikiTitles);
        System.out.println(moviesImdbIDs.keySet());
        assertEquals(true, moviesImdbIDs.keySet().toString().contains("0120915"));
    }

    @Test
    void showMoviesTitle() {
        String search = "Терминатор";
        ArrayList<String> wikiTitles = WikiMovieSearcher.getWikiTitles(search);
        HashMap<String, String> moviesImdbIDs = WikiMovieSearcher.getMoviesImdbIDs(wikiTitles);
        for (String key : moviesImdbIDs.keySet()) {
            System.out.println(OMDBMovieSearcher.getMovieByImdbID(key).getTitle());
            break;
        }
    }
}