package pl.promotion.finder.utils;


import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataDownloader {
    public static String fetchData(String apiURL) throws IOException {
        return fetchData(apiURL, Maps.newHashMap());
    }

    public static String fetchData(String apiURL, String key, String value) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put(key, value);
        return fetchData(apiURL, header);
    }

    public static String fetchData(String apiURL, Map<String, String> header) throws IOException {
        URL url = new URL(apiURL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setUseCaches(true);
        header.forEach(urlConnection::setRequestProperty);
        InputStream inputFile = urlConnection.getInputStream();
        return new String(inputFile.readAllBytes(), StandardCharsets.UTF_8);
    }
}
