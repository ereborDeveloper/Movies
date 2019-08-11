package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
public class OMDBMovie extends Movie {
    private String Rated;
    private String Released;
    private String Runtime;
    private String Writer;
    private String Plot;
    private String Language;
    private String Country;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String totalSeasons;
    private String Season;

    @JsonProperty("Title")
    private String originalTitle;

    @JsonIgnore
    private ArrayList<Object> Ratings;
    @JsonIgnore
    private String imdbVotes;
    @JsonIgnore
    private String Response;
    @JsonIgnore
    private String Awards;
    @JsonIgnore
    private String Episode;
    @JsonIgnore
    private String seriesID;
    @JsonIgnore
    private String Error;
}
