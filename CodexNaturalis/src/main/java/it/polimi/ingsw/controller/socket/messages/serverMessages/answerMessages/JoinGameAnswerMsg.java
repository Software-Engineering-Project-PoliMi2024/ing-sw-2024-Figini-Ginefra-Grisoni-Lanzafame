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

    /**
     * The constructor of the class
     * @param parent The CommandMsg being answered.
     * @param gameName of the game that the player want to join
     * @param status is equal to OK if the player was being successfully added to the game else is equal to ERROR
     */
    public JoinGameAnswerMsg(ActionMsg parent, String gameName, Status status)

    {
        super(parent);
        this.gameName = gameName;
        this.status = status;
    }

    /**
     * @return the game that the player want to join
     */
    public String getGameName()
    {
        return this.gameName;
    }

    /**
     * @return the status of the answerMsg
     */
    public Status getStatus()
    {
        return this.status;
    }

    /**
     * Update the view. If the player want to leave the lobby send a LeaveLobbyMsg
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //View.update
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
