package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Encoder {
    public static String encodePath(String path) {
        try {
            path = URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }
}
