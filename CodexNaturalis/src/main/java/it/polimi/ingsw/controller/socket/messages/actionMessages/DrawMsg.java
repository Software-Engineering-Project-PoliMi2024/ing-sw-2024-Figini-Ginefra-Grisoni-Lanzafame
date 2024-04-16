package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.DrawAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.toManyCardException;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;

public class DrawMsg extends ActionMsg{

    DrawableCard deckID;
    int cardID;

    /**
     * The constructor of the class
     * @param deckID the type of CardInHand the player want to draw
     * @param cardID can be 0/1 for the first/second card in the buffer or 2 to draw from the deck
     */
    public DrawMsg(DrawableCard deckID, int cardID){
        this.deckID = deckID;
        this.cardID = cardID;
    }

    /**
     * Call drawCardInHand() with the correct deck corresponding to the deckID
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        Game targetGame = socketClientHandler.getGame();
        switch (deckID){
            case GOLDCARD -> {
                Deck<GoldCard> deck = targetGame.getGoldCardDeck();
                drawCardInHand(deck, socketClientHandler);
            }
            case RESOURCECARD -> {
                Deck<ResourceCard> deck = targetGame.getResourceCardDeck();
                drawCardInHand(deck, socketClientHandler);
            }
        }
    }

    /**
     * Draws a card from either the buffer or the deck and adds it to the user's hand.
     * @param deck The deck from which the card is drawn.
     * @param socketClientHandler The ClientHandler responsible for processing the action message from the client.
     * @param <T> The generic type of card in hand: GoldCard or ResourceCard.
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     * @throws RuntimeException If the player already has the maximum number of cards in hand.
     */
    private  <T extends CardInHand> void drawCardInHand(Deck<T> deck, SocketClientHandler socketClientHandler) throws IOException {
        CardInHand card;
        try{
            if(cardID == 0 | cardID == 1){
                card = deck.drawFromBuffer(cardID);
                socketClientHandler.getUser().getUserHand().addCard(card);
            }else{
                card = deck.drawFromDeck();
                socketClientHandler.getUser().getUserHand().addCard(card);
            }
        }catch (toManyCardException e){
            throw new RuntimeException("Player has already the max amount of card in Hand");
        }
        socketClientHandler.sendServerMessage(new DrawAnswerMsg(this, card));
    }
}