package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
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
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        System.out.println("Active games: " + Arrays.toString(games));

        //View Update

        System.out.println("Do you want to create a game? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();

        if(answer.equals("y")) {
            System.out.println("Choose a name for the game:");
            String gameName = scanner.nextLine();

            System.out.println("How many players?");
            int players = scanner.nextInt();

            socketServerHandler.sendActionMessage(new CreateGameMsg(gameName, players));
            return;
        }

        System.out.println("Choose a game to join:");
        String gameName = scanner.nextLine();

        socketServerHandler.sendActionMessage(new JoinGameMsg(gameName, socketServerHandler.getNickname()));

    }
}
