package model.TMDB;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import model.Movie;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data

public class TMDBMovie extends Movie {
    private String vote_count;
    private String id;
    private String video;
    private String vote_average;
    private String title;
    private String popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private String[] genre_ids;
    private String backdrop_path;
    private String adult;
    private String overview;
    private String release_date;
}
