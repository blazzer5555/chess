package server;

import chess.ChessGame;
import io.javalin.websocket.WsMessageContext;
import model.GameData;
import websocket.commands.UserGameCommand;

public record MoveMadeInfo(UserGameCommand command, WsMessageContext ctx, GameData gameData,
                           String rootUsername, ChessGame.TeamColor enumOppositeColor, String oppositeColor) {
}
