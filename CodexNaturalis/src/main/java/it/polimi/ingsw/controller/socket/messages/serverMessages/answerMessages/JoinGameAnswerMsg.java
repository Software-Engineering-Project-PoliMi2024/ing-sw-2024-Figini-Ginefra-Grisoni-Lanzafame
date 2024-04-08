package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.LeaveLobbyMsg;

import java.io.IOException;
import java.util.Scanner;

public class JoinGameAnswerMsg extends AnswerMsg{

    public enum Status {
        OK,
        ERROR
    }

    private final String gameName;
    private final Status status;

    public JoinGameAnswerMsg(ActionMsg parent, String gameName, Status status)

    {
        super(parent);
        this.gameName = gameName;
        this.status = status;
    }

    public String getGameName()
    {
        return this.gameName;
    }

    public Status getStatus()
    {
        return this.status;
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        if(status == Status.ERROR) {
            System.out.println("Game " + gameName + " not found.");
            return;
        }

        System.out.println("Joined game: " + gameName);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to leave? (y/n)");

        String answer = scanner.nextLine();
        if(answer.equals("y")) {
            socketServerHandler.sendActionMessage(new LeaveLobbyMsg());
        }
    }
}
