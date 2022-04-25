package org.example.service;

import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class JavaNetURLPingService implements URLPingService {

    @Override
    public boolean pingURL(String url, int timeout) {
        try{
            url = url.replaceFirst("^https", "http");
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setReadTimeout(timeout);
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            return responseCode >= 200 && responseCode <= 399;
        } catch (Exception e){
            return false;
        }
    }
}