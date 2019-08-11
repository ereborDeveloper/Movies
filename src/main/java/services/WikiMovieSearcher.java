package services;

import services.exc.ServiceNotAvailableException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class WikiMovieSearcher {

    // Количество страниц Wiki
    private static int accuracy = 10;
    // Количество внутренних ссылок
    private static int links = 50;

    public static ArrayList<String> getWikiTitles(String search) {
        String url = "https://ru.wikipedia.org//w/api.php?action=opensearch&format=json&requestid=pageid&search=" + URLEncoder.encode(search) + "&limit=" + accuracy + "&redirects=return&utf8=1&formatversion=2";
        ArrayList<String> wikiTitles = new ArrayList<String>();
        HTTPResponse httpResponse = new HTTPResponse();
        String[] titles = null;
        try {
            String response = httpResponse.getResponse(url);
            titles = response.split(",\\[")[1].split(",");
        } catch (ServiceNotAvailableException e) {
            e.printStackTrace();
        }

        for (String title : titles) {
            wikiTitles.add(title.replace("]", "").replace('"', ' ').trim());
        }

        return wikiTitles;
    }

    public static HashMap<String, String> getMoviesImdbIDs(ArrayList<String> wikiTitles) {
        HashMap<String, String> map = new HashMap<String, String>();
        HTTPResponse httpResponse = new HTTPResponse();
        for (String title : wikiTitles) {
            String json = "";
            String url = "https://ru.wikipedia.org/w/api.php?action=query&format=json&prop=iwlinks&iwlimit=" + links + "&titles=" + URLEncoder.encode(title) + "&utf8=1&formatversion=2";
            try {
                json = httpResponse.getResponse(url);
            } catch (ServiceNotAvailableException e) {
                e.printStackTrace();
            }
            String[] links = json.split("},\\{");
            for (String link : links) {
                if (link.contains("imdbtitle")) {
                    String imdbID = link.replace('"', ' ').replace(",", "").replace(":", "").split("imdbtitle")[1].replace("title", "").replace("}", "").replace("]", "").trim();
                    map.put(imdbID, title);
                    break;
                }
            }
        }
        return map;
    }
}
