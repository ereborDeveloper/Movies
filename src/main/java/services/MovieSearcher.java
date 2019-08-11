package services;

import model.Movie;

import java.util.ArrayList;
import java.util.Set;

public interface MovieSearcher {
    abstract ArrayList<Movie> getMovies(Set<String> ids);
}
