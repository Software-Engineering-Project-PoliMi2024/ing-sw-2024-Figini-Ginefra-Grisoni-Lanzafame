package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ActiveGamesListAnswerMsg extends AnswerMsg{
    private final String[] games;

    /**
     * The constructor of the class
     * @param parent The CommandMsg being answered.
     * @param games the array of games names
     */
    public ActiveGamesListAnswerMsg(ActionMsg parent, String[] games)
    {
        super(parent);
        this.games = games;
    }

    /**
     * update the view with the games list, and ask the player if want to create or join a new game
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException if an error occur during the sending of CreateGameMsg
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view.update()
        System.out.println("Active games: " + Arrays.toString(games));
        System.out.println("Do you want to create a game? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        /* BAD TESTING OF THE DISCONNECT FEATURE
        System.out.println("Do you want to exit the game? (y/n)");
        if(answer.equals("y")){
            socketServerHandler.getClient().getController().disconnect();
        }*/
        if(answer.equals("y")) {
            //view.transitionTo(createGAMEFORM)
            System.out.println("All of these should be handle by the view");
            System.out.println("Choose a name for the game:");
            String gameName = scanner.nextLine();

            System.out.println("How many players?");
            int players = scanner.nextInt();

            socketServerHandler.getClient().getController().createGame(gameName, players);
        }else{
            System.out.println("Choose a game to join:");
            String gameName = scanner.nextLine();
            socketServerHandler.getClient().getController().joinGame(gameName, socketServerHandler.getClient().getNickname());
        }
    }
}
