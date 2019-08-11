package controller;

import com.jfoenix.controls.JFXTabPane;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Data;
import model.Movie;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import services.SearchingEngine;
import services.lang.Translator;

import java.util.ArrayList;
import java.util.List;


@Data
public class Fxml {
    @FXML
    private JFXTabPane tabPane;

    @FXML
    private GridPane rootPane;

    @FXML
    private GridPane searchGrid;

    @FXML
    private TextField searchString;

    @FXML
    private ColumnConstraints column;

    @FXML
    private Button searchButton;
    private ArrayList<Pane> searchResult;

    // TODO переделать в коллекцию
    private int movieCount = 0;
    private int rowIndex = 2;

    private SuggestionProvider<String> provider;
    private Thread engine;
    private final SearchingEngine searchingEngine = new SearchingEngine(this);
    private AutoCompletionBinding<String> autoCompletionBinding;

    @FXML
    private void initialize() {
        searchResult = new ArrayList<>();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + rootPane.getHeight() + " Width: " + rootPane.getWidth());

        rootPane.widthProperty().addListener(stageSizeListener);
        rootPane.heightProperty().addListener(stageSizeListener);

        provider = SuggestionProvider.create(new ArrayList<>());
        autoCompletionBinding = TextFields.bindAutoCompletion(searchString, provider);
        autoCompletionBinding.setHideOnEscape(true);
        autoCompletionBinding.setVisibleRowCount(10);
        engine = new Thread(searchingEngine);
        engine.start();

        searchString.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE:
                    searchingEngine.setPause(2000);
            }
        });

        // TODO
        // Отдельным потоком можно реализовать наполнение основных полей
        // Класс "библиотека" лучше держать отдельно, десеарилизовать его из файла.
    }

    private Pane addMovie(Movie movie) {
        Pane addGrid = new Pane();
        addGrid.setId("movie");
        Image poster;
        try {
            poster = new Image(movie.getPoster(), 200, 200, true, true, false);
            // Если картинка маленькая, либо не прогрузилась, выдаем стандартную
            if (poster.getHeight() < 50) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            poster = new Image("poster.jpg", 200, 200, true, true, false);
        }

        Rectangle2D croppedPortion = new Rectangle2D(0, 0, poster.getWidth(), poster.getHeight());

        ImageView posterView = new ImageView(poster);
        posterView.setSmooth(true);
        posterView.setViewport(croppedPortion);
        posterView.setFitWidth(133);
        posterView.setFitHeight(185);
        posterView.setLayoutY(1);
        posterView.setLayoutX(1);

        Pane imageViewWrapper = new Pane(posterView);
        imageViewWrapper.setId("poster");
        addGrid.getChildren().add(imageViewWrapper);

        Pane gradient = new Pane();
        gradient.setId("posterGradient");
        gradient.setPrefWidth(133);
        gradient.setPrefHeight(185);
        gradient.setLayoutY(1);
        gradient.setLayoutX(1);
        gradient.setTranslateZ(1);
        addGrid.getChildren().add(gradient);
        try {
            Label title = new Label(movie.getRussianTitle());
            Label year = new Label(movie.getYear());
            Label imdbRating = new Label("" + movie.getImdbRating().split("/")[0]);
            Label metascore = new Label("Рейтинг Metacritic: " + movie.getMetascore() + "%");
            Label cast = new Label("В ролях: " + movie.getActors());
            Label director = new Label("Режиссер: " + movie.getDirector());
            Label genre = new Label("Жанр: " + Translator.toRussian(movie.getGenre()));

            /*addGrid.getChildren().setMargin(title, new Insets(5, 0, 0, 0));
            addGrid.setMargin(imdbRating, new Insets(0, 0, 0, 2));
            addGrid.setMargin(genre, new Insets(0, 5, 0, 5));
            addGrid.setMargin(director, new Insets(0, 5, 0, 5));
            addGrid.setMargin(cast, new Insets(0, 5, 0, 5));
            addGrid.setMargin(metascore, new Insets(0, 5, 0, 5));*/

            title.setId("title");
            title.setMinWidth(posterView.getFitWidth());
            title.setMaxWidth(posterView.getFitWidth());
            title.setLayoutY(162);
            title.setLayoutX(1);

            year.setId("year");
            imdbRating.setId("imdbRating");
            imdbRating.setLayoutX(115);
            imdbRating.setLayoutY(140);
            metascore.setId("metascore");

            addGrid.getChildren().add(title);
            addGrid.getChildren().add(imdbRating);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return addGrid;
    }

    public void addOneMovieToSearchGrid(Movie movie) {
        if ((movieCount + 1) % 6 == 0) {
            movieCount = 0;
            rowIndex++;
            searchGrid.addRow(rowIndex);
        }

        Pane tempGrid = addMovie(movie);
        searchResult.add(tempGrid);

        FadeTransition ft = new FadeTransition(Duration.millis(500), tempGrid);
        ft.setFromValue(0);
        ft.setToValue(1);

        TranslateTransition tt = new TranslateTransition(Duration.millis(500), tempGrid);
        tt.setFromY(10);
        tt.setToY(0);

        ParallelTransition p = new ParallelTransition(ft, tt);
        p.play();

        searchGrid.add(tempGrid, movieCount, rowIndex);
        movieCount++;
    }

    public void addMoviesToSearchGrid(List<Movie> foundedMovies) {

        for (Movie movie : foundedMovies) {
            addOneMovieToSearchGrid(movie);
        }
    }

    public void clearSearchGrid() {
        //TODO Нетривиально
        ArrayList<Pane> current = searchResult;
        for (int i = current.size()-1; i>=0; i--) {
            FadeTransition ft = new FadeTransition(Duration.millis(Math.abs(1000 - i/current.size()*500)), current.get(i));
            ft.setFromValue(1);
            ft.setToValue(0);

            TranslateTransition tt = new TranslateTransition(Duration.millis(Math.abs(1000 - i/current.size()*500)), current.get(i));
            tt.setFromY(0);
            tt.setToY(10);

            ParallelTransition p = new ParallelTransition(ft, tt);
            p.play();
            searchGrid.getChildren().remove(searchResult.get(i));
        }
        movieCount = 0;
        rowIndex = 2;

        searchResult.clear();
    }

    public void showSuggestions(List<String> suggestions) {
        provider.clearSuggestions();
        provider.addPossibleSuggestions(suggestions);
    }

    public String getEnteredText() {
        return searchString.getText().trim().toLowerCase();
    }

    public boolean isCyrillic(String s) {
        boolean result = false;
        for (char a : s.toCharArray()) {
            if (Character.UnicodeBlock.of(a) == Character.UnicodeBlock.CYRILLIC) {
                result = !result;
                break;
            }
        }
        return result;
    }

    @FXML
    public void addToGallery() {
        // TODO
        // Здесь нужно добавлять фильм в collection, чтобы потом сериализовать её в файл
    }


}
