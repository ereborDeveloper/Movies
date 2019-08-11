package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
public class Movie implements Comparable {
    private String poster;

    @JsonPropertyOrder("title")
    private String title;
    private String russianTitle;
    private String year;
    private String imdbID;
    private String imdbRating;
    private String metascore;
    private String director;
    private String actors;
    private String genre;

    @JsonIgnore
    private String description;


    @Override
    public int compareTo(Object o) {
        return this.getTitle().compareTo(((Movie) o).getTitle());
    }
}
