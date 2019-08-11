package model.TMDB;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.ArrayList;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data

public class SearchMovieResult {
    private String page;
    private String total_results;
    private String total_pages;
    private ArrayList<TMDBMovie> results;

}
