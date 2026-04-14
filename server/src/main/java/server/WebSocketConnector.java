package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import model.JoinGameRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnector implements WsMessageHandler, WsConnectHandler, WsCloseHandler {

    private final Gson gson = new Gson();
    private final ConcurrentHashMap<Session, Integer> sessionHolder = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Session, String> sessionToAuth = new ConcurrentHashMap<>();
    private final DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
    private final DatabaseGameDAO gameDAO = new DatabaseGameDAO();

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            handleConnection(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            handleLeaving(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            handleMakeMove(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            handleResign(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.GET_BOARD) {
            handleGetBoard(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.HIGHLIGHT_BOARD) {
            handleHighlightBoard(ctx, command);
        }
    }

    private void handleHighlightBoard(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.blackUsername(), username)) {
                color = "BLACK";
            }
            else {
                color = "WHITE";
            }
            String position = command.getMove().getStartPosition().print();
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.HIGHLIGHTED_GAME, null, position, game, color);
            String serializedMessage = gson.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleGetBoard(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.blackUsername(), username)) {
                color = "BLACK";
            }
            else {
                color = "WHITE";
            }
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, null, game, color);
            String serializedMessage = gson.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleResign(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            if (gameData.game().isGameOver()) {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, "The other player has already resigned. You win!");
                return;
            }
            gameData.game().resign();
            gameDAO.updateGame(gameData);
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "Black";
            }
            else if (Objects.equals(gameData.blackUsername(), username)) {
                color = "White";
            }
            else {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, "You are observing! You can't resign from a match that you aren't playing.");
                return;
            }
            String notificationMessage = username + " has forfeited the match. " + color + " wins!";
            notifyAllOtherPlayers(command, ctx, notificationMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            String rootUsername = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            ChessGame.TeamColor enumOppositeColor;
            String oppositeColor;
            if (Objects.equals(gameData.whiteUsername(), rootUsername)) {
                oppositeColor = "Black";
                enumOppositeColor = ChessGame.TeamColor.BLACK;
                if (gameData.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor()
                        == ChessGame.TeamColor.BLACK) {
                    WebsocketErrorResponder er = new WebsocketErrorResponder();
                    er.handleErrorResponse(ctx, "You can't move another person's piece!");
                    return;
                }
            }
            else if (Objects.equals(gameData.blackUsername(), rootUsername)) {
                oppositeColor = "White";
                enumOppositeColor = ChessGame.TeamColor.WHITE;
                if (gameData.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor()
                        == ChessGame.TeamColor.WHITE) {
                    WebsocketErrorResponder er = new WebsocketErrorResponder();
                    er.handleErrorResponse(ctx, "You can't move another person's piece!");
                    return;
                }
            }
            else {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, "You are observing! You can't make any moves.");
                return;
            }
            try {
                gameData.game().makeMove(command.getMove());
            }
            catch (Exception e) {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, e.getMessage());
                return;
            }
            if (gameData.game().isInCheckmate(enumOppositeColor) || gameData.game().isInStalemate(enumOppositeColor)) {
                gameData.game().resign();
            }
            gameDAO.updateGame(gameData);
            MoveMadeInfo info = new MoveMadeInfo(command, ctx, gameData, rootUsername, enumOppositeColor, oppositeColor);
            notifyMoveMade(info);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleLeaving(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            String enumColor;
            boolean observerLeaving = false;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "white";
                enumColor = "WHITE";
            }
            else if (Objects.equals(gameData.blackUsername(), username)){
                color = "black";
                enumColor = "BLACK";
            }
            else {
                observerLeaving = true;
                color = "white";
                enumColor = "WHITE";
            }
            String notificationMessage;
            if (observerLeaving) {
                notificationMessage = "Player " + username + " is no longer spectating the game.";
            }
            else {
                notificationMessage = "Player " + username + " is no longer playing " + color + ".";
                JoinGameRequest request = new JoinGameRequest(enumColor, command.getGameID());
                gameDAO.leavePlayer(request);
            }
            sessionHolder.remove(ctx.session);
            sessionToAuth.remove(ctx.session);
            notifyAllOtherPlayers(command, ctx, notificationMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleConnection(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            sessionHolder.put(ctx.session, command.getGameID());
            sessionToAuth.put(ctx.session, command.getAuthToken());
            String enumColor;
            String color;
            boolean observerJoined = false;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                enumColor = "WHITE";
                color = "white";
            }
            else if (Objects.equals(gameData.blackUsername(), username)) {
                enumColor = "BLACK";
                color = "black";
            }
            else {
                enumColor = "WHITE";
                color = "white";
                observerJoined = true;
            }
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                    null, null, game, enumColor);
            String serializedMessage = gson.toJson(message);
            ctx.send(serializedMessage);
            String notificationMessage;
            if (observerJoined) {
                notificationMessage = "Player " + username + " is now spectating the game.";
            }
            else {
                notificationMessage = "Player " + username + " has joined the game as " + color + ".";
            }
            notifyAllOtherPlayers(command, ctx, notificationMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void notifyAllOtherPlayers(UserGameCommand command, WsMessageContext ctx, String notificationMessage) throws Exception {
        for (Session session: sessionHolder.keySet()) {
            if ((Objects.equals(sessionHolder.get(session), command.getGameID())) && (session != ctx.session)) {
                notify(session, notificationMessage);
            }
        }
    }

    private void notifyMoveMade(MoveMadeInfo info) throws Exception {
        for (Session session: sessionHolder.keySet()) {
            if (Objects.equals(sessionHolder.get(session), info.command().getGameID())) {
                String recipientUsername = authDAO.getAuthByAuthToken(sessionToAuth.get(session)).username();
                String color;
                if (Objects.equals(info.gameData().blackUsername(), recipientUsername)) {
                    color = "BLACK";
                }
                else {
                    color = "WHITE";
                }
                ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                        null, null, info.gameData().game(), color);
                String serializedMessage = gson.toJson(loadGameMessage);
                session.getRemote().sendString(serializedMessage);
                if (session != info.ctx().session) {
                    String notificationString = info.rootUsername() + " has made a move from " +
                            info.command().getMove().getStartPosition().print() +
                            " to " + info.command().getMove().getEndPosition().print();
                    notify(session, notificationString);
                }
                if (info.gameData().game().isInCheckmate(info.enumOppositeColor())) {
                    notifyCheckmate(session, info.oppositeColor(), info.gameData());
                }
                else if (info.gameData().game().isInCheck(info.enumOppositeColor())) {
                    notifyCheck(session, info.oppositeColor(), info.gameData());
                }
                else if (info.gameData().game().isInStalemate(info.enumOppositeColor())) {
                    notifyStalemate(session, info.oppositeColor(), info.gameData());
                }
            }
        }
    }

    private void notifyCheck(Session session, String color, GameData gameData) throws Exception {
        String notificationString;
        if (Objects.equals(color, "BLACK")) {
            notificationString = gameData.blackUsername() + " is in check!";
        }
        else {
            notificationString = gameData.whiteUsername() + " is in check!";
        }
        notify(session, notificationString);
    }

    private void notifyCheckmate(Session session, String color, GameData gameData) throws Exception {
        String notificationString;
        if (Objects.equals(color, "BLACK")) {
            notificationString = gameData.blackUsername() + " has been checkmated. " + gameData.whiteUsername() + " wins!";
        }
        else {
            notificationString = gameData.whiteUsername() + " has been checkmated. " + gameData.blackUsername() + " wins!";
        }
        notify(session, notificationString);
    }

    private void notifyStalemate(Session session, String color, GameData gameData) throws Exception {
        String notificationString;
        if (Objects.equals(color, "BLACK")) {
            notificationString = gameData.blackUsername() + " is in stalemate. The game ends in a draw.";
        }
        else {
            notificationString = gameData.whiteUsername() + " is in stalemate. The game ends in a draw.";
        }
        notify(session, notificationString);
    }

    private void notify(Session session, String notificationString) throws Exception {
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                null, notificationString, null, null);
        String otherSerializedMessage = gson.toJson(notificationMessage);
        session.getRemote().sendString(otherSerializedMessage);
    }
}
