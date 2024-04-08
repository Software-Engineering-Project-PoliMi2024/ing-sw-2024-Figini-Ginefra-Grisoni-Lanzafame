package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.controller.socket.messages.observers.ServerMsgObserved;
import it.polimi.ingsw.controller.socket.messages.observers.ServerMsgObserver;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;
import it.polimi.ingsw.designPatterns.Observer.Observed;
import it.polimi.ingsw.designPatterns.Observer.Observer;
import it.polimi.ingsw.model.playerReleted.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class GameParty implements ServerMsgObserved {
    final List<ServerMsgObserver> observers = new ArrayList<>();
    final private List<User> usersList; //played by
    private User currentPlayer;

    private int currentPlayerIndex;

    private final int numberOfMaxPlayer;

    public GameParty(int numberOfMaxPlayer) {
        usersList = new ArrayList<>();

        if(numberOfMaxPlayer < 2 || numberOfMaxPlayer > 4){
            throw new IllegalArgumentException("The number of player must be at least 1 and at most 4");
        }
        this.numberOfMaxPlayer = numberOfMaxPlayer;
    }

    /**
     * Handles the adding of a user to the current game.
     * If the game is empty, the first player added will become the current player.
     * @param user The user to add to the game.
     * @throws FullMatchException if the number of player currently in the game is equal to the number of max player allowed.
     */
    public void addUser(User user) throws FullMatchException {
        if (usersList.size() == numberOfMaxPlayer) {
            throw new FullMatchException("The match is already full");
        } else {
            usersList.add(user);
        }
    }

    /**
     * Handles the removal of a user of the current game.
     *
     * @param user The user that need to be removed.
     * @throws EmptyMatchException if, after the removal of the player, the game is left without users.
     * @throws UserNotFoundException if the specified user is not found in the userList of this game.
     */
    public void removeUser(User user) throws EmptyMatchException, UserNotFoundException{
        if(!usersList.remove(user)){
            throw new UserNotFoundException("The user is not in this game");
        }else{
            if (usersList.isEmpty()) {
                currentPlayer=null;
                throw new EmptyMatchException("There are more not active player in this game");
            }
            assert user != null;        //handle the case of user being null while currentPlayer is null
            if(user==currentPlayer){
                nextPlayer();
            }
        }
    }

    /**
     * This method advances the game to the next player in the rotation sequence.
     * if there is no currentPlayer, it creates it by launching the chooseStartingOrder method
     * @throws EmptyMatchException if the game is empty.
     */
    public void nextPlayer() throws EmptyMatchException {
        if(usersList.isEmpty()){
            throw new EmptyMatchException("The game is empty, there is no next player");
        }
        if (currentPlayer == null) {
            chooseStartingOrder();
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % usersList.size();
            currentPlayer = usersList.get(currentPlayerIndex);
        }
    }

    /**
     * Randomize the userList and set the currentPlayer to the first of the list
     */
    public void chooseStartingOrder(){
        if(usersList.size() < this.numberOfMaxPlayer){
            throw new IllegalStateException("The number of player is not enough to start the game");
        }

        Collections.shuffle(usersList);
        currentPlayerIndex = 0;
        currentPlayer=usersList.getFirst();
    }

    /** @return list of active player in this match*/
    public List<User> getUsersList() {
        return usersList;
    }

    /** @return the current Player*/
    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public int getNumberOfMaxPlayer() {
        return numberOfMaxPlayer;
    }


    public void attach(ServerMsgObserver observer){
        observers.add(observer);
    }

    public void detach(ServerMsgObserver observer){
        observers.remove(observer);
    }

    public void notifyObservers(ServerMsg serverMsg){
        for(ServerMsgObserver observer : observers){
            observer.update(serverMsg);
        }
    }
}
