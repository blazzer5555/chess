package client;

import com.google.gson.Gson;
import model.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ServerCommunicator {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public String sendRegisterRequest(UserData registerRequest) throws Exception {
        String urlString = "http://localhost:8080/user";
        String registerBody = GSON.toJson(registerRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(registerBody))
                .build();
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return GSON.fromJson(httpResponse.body(), AuthData.class).authToken();
        } else {
            notifyUserOfProblem(httpResponse, false);
        }
        return null;
    }

    public String sendLoginRequest(LoginRequest loginRequest) throws Exception {
        String urlString = "http://localhost:8080/session";
        String loginBody = GSON.toJson(loginRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                .build();
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return GSON.fromJson(httpResponse.body(), AuthData.class).authToken();
        } else {
            notifyUserOfProblem(httpResponse, false);
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
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            notifyUserOfProblem(httpResponse, false);

        }
    }

    public int sendCreateGameRequest(CreateGameRequest createGameRequest, String authToken) throws Exception {
        String urlString = "http://localhost:8080/game";
        String createBody = GSON.toJson(createGameRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(createBody))
                .build();
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return GSON.fromJson(httpResponse.body(), CreateResponse.class).gameID();
        } else {
            notifyUserOfProblem(httpResponse, false);
            return -1;
        }
    }

    public boolean sendJoinGameRequest(JoinGameRequest joinGameRequest, String authToken) throws Exception {
        boolean successfulJoin;
        String urlString = "http://localhost:8080/game";
        String joinBody = GSON.toJson(joinGameRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(joinBody))
                .build();
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            notifyUserOfProblem(httpResponse, true);
            successfulJoin = false;
        }
        else {
            successfulJoin = true;
        }
        return successfulJoin;
    }

    public ArrayList<ListGamesResponse> sendListGamesRequest(String authToken) throws Exception {
        String urlString = "http://localhost:8080/game";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .GET()
                .build();
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        record ListResponse(ArrayList<ListGamesResponse> games) {
        }
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            ListResponse returnList = GSON.fromJson(httpResponse.body(), ListResponse.class);
            return returnList.games();
        } else {
            notifyUserOfProblem(httpResponse, false);
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
        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            notifyUserOfProblem(httpResponse, false);
        }
    }

    private void notifyUserOfProblem(HttpResponse<String> httpResponse, boolean calledFromJoinGame) {
        switch (httpResponse.statusCode()) {
            case (400):
                System.out.println("You didn't enter valid information. Please try again and make sure everything is typed properly.");
                break;
            case (401):
                System.out.println("The username and/or password is incorrect. Please try again.");
                break;
            case(403):
                if (calledFromJoinGame) {
                    System.out.println("That color is already being played by someone. Please choose a different color.");
                }
                else {
                    System.out.println("That username is already taken. Please choose a different one.");
                }
                break;
            case(500):
                System.out.println("Something went wrong with the server. Please try again later.");
                break;
        }
    }
}
