package client;

import com.google.gson.Gson;
import model.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ServerCommunicator {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public String sendRegisterRequest(UserData registerRequest) throws Exception {
        String urlString = "http://localhost:8080/user";
        String registerBody = gson.toJson(registerRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(registerBody))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return gson.fromJson(httpResponse.body(), AuthData.class).authToken();
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
        return null;
    }

    public String sendLoginRequest(LoginRequest loginRequest) throws Exception {
        String urlString = "http://localhost:8080/session";
        String loginBody = gson.toJson(loginRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return gson.fromJson(httpResponse.body(), AuthData.class).authToken();
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
        return null;
    }

    public void sendLogoutRequest(String authToken) throws Exception {
        String urlString = "http://localhost:8080/session";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .DELETE()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {

        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }

    public int sendCreateGameRequest(CreateGameRequest createGameRequest, String authToken) throws Exception {
        String urlString = "http://localhost:8080/game";
        String createBody = gson.toJson(createGameRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(createBody))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return gson.fromJson(httpResponse.body(), CreateResponse.class).gameID();
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
            return -1;
        }
    }

    public void sendJoinGameRequest(JoinGameRequest joinGameRequest, String authToken) throws Exception {
        String urlString = "http://localhost:8080/game";
        String joinBody = gson.toJson(joinGameRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(joinBody))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {

        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }

    public ArrayList<ListGamesResponse> sendListGamesRequest(String authToken) throws Exception {
        String urlString = "http://localhost:8080/game";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        record ListResponse(ArrayList<ListGamesResponse> games) {
        }
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            ListResponse returnList = gson.fromJson(httpResponse.body(), ListResponse.class);
            return returnList.games();
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
        return null;
    }

    public void sendClearRequest(String authToken) throws Exception {
        String urlString = "http://localhost:8080/db";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .DELETE()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {

        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }
}
