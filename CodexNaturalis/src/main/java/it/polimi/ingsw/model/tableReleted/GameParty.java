package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GameParty implements Serializable {
    final private List<User> playerList; //player that have joined the game
    private User currentPlayer; //the player that is playing currently
    private int currentPlayerIndex;

    public GameParty(List<String> playerNames) {
        Collections.shuffle(playerNames);
        playerList = Collections.unmodifiableList(playerNames.stream().map(User::new).toList());
        currentPlayerIndex = 0;
        currentPlayer = playerList.getFirst();
    }
    /**
     * This method advances the game to the next player in the rotation sequence.
     * if there is no currentPlayer, it creates it by launching the chooseStartingOrder method
     * @throws IllegalCallerException if the game is empty.
     */
    public void nextPlayer() throws IllegalCallerException {
        if(playerList.isEmpty()){
            throw new IllegalCallerException("The game is empty, there is no next player");
        }
        if (currentPlayer == null) {
            throw new IllegalCallerException("The current player is null");
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            currentPlayer = playerList.get(currentPlayerIndex);
        }
    }
    /** @return list of the players in this match*/
    public List<User> getUsersList() {
        return playerList;
    }
    /** @return the current Player*/
    public User getCurrentPlayer() {
        return currentPlayer;
    }
    public int getNumberOfMaxPlayer() {
        return playerList.size();
    }
}
