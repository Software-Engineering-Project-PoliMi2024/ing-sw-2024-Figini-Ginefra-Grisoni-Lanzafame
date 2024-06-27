package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * This interface is used to define the methods that the player can call
 * to interact with the server and the game
 * It extends the Remote and Serializable interface to be used in the RMI protocol
 */
public interface ControllerInterface extends Remote, Serializable {
    /**
     * This method is used to log In the player
     * setting the nickname chosen by the player
     * and biding the controller to the player view
     * If the nickname is already taken, the player is notified
     * If the nickname was in a game, the player is reconnected to the game
     * @param nickname the nickname chosen by the player
     * @throws Exception required by the RMI protocol
     */
    void login(String nickname) throws Exception;

    /**
     * This method publishes a new lobby creating it
     * If the lobby name is already taken, the player is notified
     * @param gameName the name of the lobby
     * @param maxPlayerCount the maximum number of players that can join the lobby
     * @throws Exception required by the RMI protocol
     */
    void createLobby(String gameName, int maxPlayerCount) throws Exception;

    /**
     * This method is used to join an existent lobby
     * adding the player to the lobby and updating its view
     * If the lobby becomes full after the player joins, the game starts
     * If the lobby doesn't exist, the player is notified
     * @param lobbyName the name of the lobby to join
     * @throws Exception required by the RMI protocol
     */
    void joinLobby(String lobbyName) throws Exception;

    /**
     * This method is used to leave from whichever state the player is in
     * and moves him back to the main menu
     * @throws Exception required by the RMI protocol
     */
    void leave() throws Exception;

    /**
     * This method is used to leave the lobby the player is in
     * and moves him back to the lobbyList
     * @throws Exception required by the RMI protocol
     */
    void leaveLobby() throws Exception;

    /**
     * This method is used to choose the secret objective card by the player
     * @param objectiveCard the secret objective card chosen by the player
     * @throws Exception required by the RMI protocol
     */
    void chooseSecretObjective(LightCard objectiveCard) throws Exception;

    /**
     * This method is used to choose the pawn color by the player
     * @param color the pawn color chosen by the player
     * @throws Exception required by the RMI protocol
     */
    void choosePawn(PawnColors color) throws Exception;

    /**
     * This method is used to place a card on the board
     * It can either be the startCard or a cardInHand
     * @param placement the placement of the pawn
     * @throws Exception required by the RMI protocol
     */
    void place(LightPlacement placement) throws Exception;

    /**
     * This method is used to draw a card from the decks
     * @param deckID the deck from which the card is drawn (either Resource or Gold)
     * @param cardID the position of the card to draw (0,1 for the buffer, 2 for the deck)
     * @throws Exception required by the RMI protocol
     */
    void draw(DrawableCard deckID, int cardID) throws Exception;

    /**
     * This method is used to send a chat message to the other players
     * @param message the message to send
     * @throws Exception required by the RMI protocol
     */
    void sendChatMessage(ChatMessage message) throws Exception;
}
