package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.ServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.CreateGameMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.JoinGameMsg;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ActiveGamesListAnswerMsg extends AnswerMsg{
    private final String[] games;

    public ActiveGamesListAnswerMsg(ActionMsg parent, String[] games)
    {
        super(parent);
        this.games = games;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        System.out.println("Active games: " + Arrays.toString(games));

        Scanner scanner = new Scanner(System.in);

        if(games.length == 0) {
            ActionMsg createGameMsg = new CreateGameMsg("game1", 2);
            System.out.println("No active games. Creating One");
            serverHandler.sendActionMessage(createGameMsg);
        }
        else {
            //Read the nickname and the chosen game
            System.out.println("Choose a game to join: ");
            String gameName = scanner.nextLine();

            //Send the join game message
            serverHandler.sendActionMessage(new JoinGameMsg(gameName, serverHandler.getNickname()));
        }
    }
}
