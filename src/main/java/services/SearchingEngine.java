package services;

import controller.Fxml;
import javafx.application.Platform;
import lombok.Data;
import model.Movie;
import model.TMDB.SearchMovieResult;
import model.TMDB.TMDBMovie;
import services.lang.Translator;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Data
public class SearchingEngine implements Runnable {

    private Fxml controller;
    private ArrayList<String> foundedTitles;
    private Map<String, String> foundedIDs;
    private ArrayList<Movie> foundedMovies;
    private String search = "";
    private String currentID = "";
    private int pause = 0;

    private final OMDBMovieSearcher omdbMovieSearcher = new OMDBMovieSearcher();
    private boolean wikiSearchEnabled = false;
    private boolean TMDBSearchEnabled = true;

    public SearchingEngine(Fxml controller) {
        this.controller = controller;
        foundedTitles = new ArrayList<>();
        foundedIDs = new HashMap<>();
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(pause);
                pause = 0;
            } catch (Exception e) {

            }
            // Очистка поиска после удаления запроса. Пауза выставляется через bind на backspace
            if (controller.getEnteredText().equals("")) {
                Platform.runLater(() -> controller.clearSearchGrid());
            }


            if (!controller.getEnteredText().equals("") && search.compareTo(controller.getEnteredText()) != 0) {
                Platform.runLater(() -> controller.clearSearchGrid());
                search = controller.getEnteredText();
                boolean contains = false;
                for (String key : foundedIDs.keySet()) {
                    String res = "";
                    if (search.length() <= key.length()) {
                        res = key.substring(0, search.length()).trim().toLowerCase();
                        if (search.contains(res)) {
                            contains = true;
                            break;
                        }
                    } else {
                        res = search.substring(0, key.length()).trim().toLowerCase();
                        if (search.equals(res)) {
                            contains = true;
                            break;
                        }
                    }
                    /*if(key.compareTo(search)>=0)
                    {
                        System.out.println(true);
                        contains = true;
                        break;
                    }*/
                }
                if (!contains) {
                    Platform.runLater(() -> controller.clearSearchGrid());

                    // Ищем один фильм для поиска по оригинальному названию
                    if (!Translator.isCyrillic(search)) {
                        System.out.println("Ищем страницу по оригинальному названию");
                        CompletableFuture<Movie> movie = CompletableFuture.supplyAsync(() -> OMDBMovieSearcher.getMovieByTitle(search));
                        try {
                            foundedMovies = new ArrayList<>();
                            foundedMovies.add(movie.get());
                            System.out.println(foundedMovies);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {
                            controller.addMoviesToSearchGrid(foundedMovies);
                        });
                    } else {

                        // Поиск страниц на википедии, потом по точным названиям получаем imdbid.
                        // TODO: Совместить с текущим поисом
                        if (wikiSearchEnabled) {
                            System.out.println("Ищем страницы на википедии по совпадению");
                            CompletableFuture<ArrayList<String>> titles = CompletableFuture.supplyAsync(() -> WikiMovieSearcher.getWikiTitles(search));
                            try {
                                foundedTitles = titles.get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (!foundedTitles.isEmpty() && search.equals(controller.getEnteredText())) {
                                System.out.println("Получаем id/title со страниц");

                                CompletableFuture<HashMap<String, String>> ids = CompletableFuture.supplyAsync(() -> WikiMovieSearcher.getMoviesImdbIDs(foundedTitles));
                                try {
                                    foundedIDs = ids.get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                List suggestions = new ArrayList(foundedIDs.values());
                                Collections.sort(suggestions);
                                controller.showSuggestions(suggestions);

                                CompletableFuture<ArrayList<Movie>> movie = CompletableFuture.supplyAsync(() -> omdbMovieSearcher.getMovies(foundedIDs.keySet()));
                                try {
                                    foundedMovies = movie.get();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Platform.runLater(() -> {
                                    List toSort = foundedMovies;
                                    Collections.sort(toSort);
                                    controller.addMoviesToSearchGrid(toSort);
                                });
                            }
                        }
                        // Поиск по The Movie Data Base
                        if (TMDBSearchEnabled) {
                            System.out.println("");
                            SearchMovieResult result = TMDBMovieSearcher.getSearchResultByRussian(search);
                            foundedMovies = new ArrayList<>();
                            for (TMDBMovie movie : result.getResults()) {
                                String title = movie.getOriginal_title();
                                System.out.println(title);
                                CompletableFuture<Movie> filmFuture;
                                if (Translator.isCyrillic(title)) {
                                    filmFuture = CompletableFuture.supplyAsync(() -> OMDBMovieSearcher.getMovieByImdbID(TMDBMovieSearcher.getImdbId(movie.getId())));
                                } else {
                                    filmFuture = CompletableFuture.supplyAsync(() -> OMDBMovieSearcher.getMovieByTitle(title));
                                }
                                try {
                                    Movie film = filmFuture.get();
                                    if (film.getTitle() != null && !film.getImdbRating().contains("N")) {
                                        film.setRussianTitle(movie.getTitle());
                                        Platform.runLater(() -> {
                                            controller.addOneMovieToSearchGrid(film);
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {

            }
        }
    }
}
