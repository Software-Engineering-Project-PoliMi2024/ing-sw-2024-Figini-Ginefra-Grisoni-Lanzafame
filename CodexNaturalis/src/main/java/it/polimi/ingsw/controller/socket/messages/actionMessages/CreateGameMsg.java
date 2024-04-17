package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.CreateGameAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;

public class CreateGameMsg extends ActionMsg{
    private final String name;
    private final int numberOfPlayers;

    /**
     * The constructor of the class
     * @param name of the game that is being created
     * @param numberOfPlayers needed to start the game
     */
    public CreateGameMsg(String name, int numberOfPlayers)
    {
        super();

        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
    }


    /**
     * Create a game and try to add it to MultiGame.
     * Always answer to the client with a CreateGameAnswerMsg with a status (OK, ERROR)
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws RuntimeException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler){
        Game game = new Game(name, numberOfPlayers);
        ActionMsg.updateMultiGame(socketClientHandler, games -> {
            try{
                if(!games.addGame(game)){
                    socketClientHandler.sendServerMessage(new CreateGameAnswerMsg(this, name, CreateGameAnswerMsg.Status.ERROR));
                }else{
                    socketClientHandler.sendServerMessage(new CreateGameAnswerMsg(this, name, CreateGameAnswerMsg.Status.OK));
                    System.out.println("Game created: " + name + " with " + numberOfPlayers + " players.");
                    System.out.println("Game list: " + String.join(", ", socketClientHandler.getGames().getGameNames()));
                }
            }catch (IOException e){
                throw new RuntimeException();
            }
        });
    }
}
