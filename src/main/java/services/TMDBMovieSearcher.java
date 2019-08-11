package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Movie;
import model.TMDB.SearchMovieResult;
import services.exc.ServiceNotAvailableException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

public class TMDBMovieSearcher implements MovieSearcher {
    private static String apiKey = "5092b50b3bce02c1f3c9fc7a27583f69";

    public static SearchMovieResult getSearchResultByRussian(String russian) {
        ArrayList<String> ids = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/search/movie?page=1&query=" + URLEncoder.encode(russian) + "&language=ru-RU&api_key=" + apiKey;
        SearchMovieResult result = new SearchMovieResult();
        String json = "";
        HTTPResponse httpResponse = new HTTPResponse();
        try {
            json = httpResponse.getResponse(url);
        } catch (ServiceNotAvailableException e) {
            e.printStackTrace();
        }
        try {
            result = new ObjectMapper().readValue(json, SearchMovieResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getImdbId(String internalId) {
        String url = "https://api.themoviedb.org/3/movie/" + internalId + "/external_ids?api_key=" + apiKey;
        String json = "";
        HTTPResponse httpResponse = new HTTPResponse();
        try {
            json = httpResponse.getResponse(url);
        } catch (ServiceNotAvailableException e) {
            e.printStackTrace();
        }
        return json.split(",")[1].replace('"', ' ').replace("imdb_id : tt", "").trim();
    }

    @Override
    public ArrayList<Movie> getMovies(Set<String> ids) {
        return null;
    }

    public void appendMovieInfo() {

    }


}
