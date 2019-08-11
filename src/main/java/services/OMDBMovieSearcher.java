package services;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Movie;
import model.OMDBMovie;
import services.exc.ServiceNotAvailableException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

public class OMDBMovieSearcher implements MovieSearcher {

    private static String apiKey = "63f68b84";

    // Получить фильм из OMDB
    public static Movie getMovieByTitle(String title) {
        String url = "http://www.omdbapi.com/?t=" + Encoder.encodePath(title) + "&apikey=" + apiKey;
        String json = "";
        HTTPResponse httpResponse = new HTTPResponse();
        try {
            json = httpResponse.getResponse(url);
        } catch (ServiceNotAvailableException e) {
            e.printStackTrace();
        }
        return deserialize(json);
    }

    public static Movie getMovieByImdbID(String id) {
        String url = "http://www.omdbapi.com/?i=tt" + id + "&apikey=" + apiKey;
        String json = "";
        HTTPResponse httpResponse = new HTTPResponse();
        try {
            json = httpResponse.getResponse(url);
        } catch (ServiceNotAvailableException e) {
            e.printStackTrace();
        }
        return deserialize(json);
    }

    @Override
    public ArrayList<Movie> getMovies(Set<String> ids) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (String id : ids) {
            String url = "http://www.omdbapi.com/?i=tt" + id + "&apikey=" + apiKey;
            String json = "";
            HTTPResponse httpResponse = new HTTPResponse();
            try {
                json = httpResponse.getResponse(url);
            } catch (ServiceNotAvailableException e) {
                e.printStackTrace();
            }
            Movie film = deserialize(json);
            // Отсеиваем всякий шмурдяк
            String rating = film.getImdbRating();
            if (!rating.contains("N")) {
                if (Double.parseDouble(rating) > 4) {
                    movies.add(film);
                }
            }
        }
        return movies;
    }

    private static OMDBMovie deserialize(String json) {
        OMDBMovie film = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            film = mapper.readValue(json, OMDBMovie.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Фильм не найден");
        }
        return film;
    }
}
