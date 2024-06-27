package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.io.Serializable;

/**
 * This interface is used to define the methods of the game controller used by the user
 * to interact with and modify the game state
 */
public interface GameControllerInterface extends Serializable {
    /**
     * This method is used to choose the secret objective card by the player
     * it sets the secret objective card chosen by the player in the model
     * it notifies the players views of the successful choice
     * If the player has already chosen a secret objective card or the card is not valid,
     * the player will be considered malevolent and notified
     * If the player choosing is the last one to choose the game will progress to the next state
     * i.e. the actual game will start and all the active players will be notified
     * @param nickname the nickname of the player choosing the secret objective card
     * @param objectiveCard the secret objective card chosen by the player
     */
    void chooseSecretObjective(String nickname, LightCard objectiveCard);

    /**
     * This method is used to choose the pawn color by the player
     * it sets the pawn color chosen by the player in the model
     * it notifies the players views of the successful choice
     * If the player has already chosen a pawn color or the color is not valid,
     * the player will be considered malevolent and notified
     * If the pawn chosen is already taken, the player will be notified and repeated the choice
     * If the player choosing is the last one to choose the game will progress to the next state
     * i.e. the choice of the secret objective and all the active players will be notified
     * @param nickname the nickname of the player choosing the pawn color
     * @param color the pawn color chosen by the player
     */
    void choosePawn(String nickname, PawnColors color);

    /**
     * This method is used to place a card in the player codex
     * it sets the card in the player codex,
     * and notifies all the players views of the placement
     * If it is not the player's turn or the card is not valid,
     * the player will be considered malevolent and notified
     * After the placement, the player will progress to the drawing phase
     * If the decks are empty, the turns will progress
     * If the winning condition are met, the game will end
     * @param nickname the nickname of the player placing the card
     * @param placement the placement of the card in the player codex,
     *                  specifying the card placed, its position
     *                  and the face shawn
     */
    void place(String nickname, LightPlacement placement);

    /**
     * This method is used to draw a card from the deck
     * it adds the card to the player's hand and
     * notifies all the players views of the drawn
     * to the player that drew the card it notifies the drawn card
     * to the other players it notifies just the back of the card
     * If it is not the player's turn or the deckID / cardID is not valid,
     * the player will be considered malevolent and notified
     * If the decks are empty, the phase will be skipped
     * After the drawing, the game turns will progress
     * If the winning condition are met, the game will end
     * @param nickname the nickname of the player drawing the card
     * @param deckID the deck from which the card is drawn (either Resource or Gold)
     * @param cardID the position of the card to draw (0,1 for the buffer, 2 for the deck)
     */
    void draw(String nickname, DrawableCard deckID, int cardID);
}
