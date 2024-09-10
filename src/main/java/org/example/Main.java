package org.example;


import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    private static final String GITHUB_API_URL = "https://api.github.com/users/";
    public static void main (String[] args) throws Exception{
        if (args.length == 0) {
            System.out.println("Please provide a username as the argument");
            return;
        }

        String username = args[0];
        String url = GITHUB_API_URL + username + "/events";

        //Create an HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github.v3+json") // response format
                .build();

        //Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            JsonArray events = JsonParser.parseString(responseBody).getAsJsonArray();

            System.out.println("lasts github events of user " + username + ": ");
            for (int i = 0; i < Math.min(events.size(), 5); i++) {
                String type = events.get(i).getAsJsonObject().get("type").getAsString();
                String repo = events.get(i).getAsJsonObject().get("repo").getAsJsonObject().get("name").getAsString();
                System.out.println("Event: " + type + " in repo: " + repo);
            }
        } else {
            System.out.println("Error: " + response.statusCode() + " " + response.body());
        }
    }
}