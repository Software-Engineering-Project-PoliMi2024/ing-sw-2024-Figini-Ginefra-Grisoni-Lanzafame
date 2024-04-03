package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.ServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.LeaveLobbyMsg;

import java.io.IOException;
import java.util.Scanner;

public class JoinGameAnswerMsg extends AnswerMsg{
    private final String gameName;

    public JoinGameAnswerMsg(ActionMsg parent, String gameName)
    {
        super(parent);
        this.gameName = gameName;
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        System.out.println("Joined game: " + gameName);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to leave? (y/n)");

        String answer = scanner.nextLine();
        if(answer.equals("y")) {
            serverHandler.sendActionMessage(new LeaveLobbyMsg());
        }
    }
}
