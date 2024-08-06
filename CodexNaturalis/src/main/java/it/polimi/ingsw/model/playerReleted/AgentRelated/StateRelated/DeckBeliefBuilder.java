package it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.utils.cardFactories.ResourceCardFactory;

import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class DeckBeliefBuilder {
    static CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);

    /**
     * This method takes a queue of cards and removes all the cards that are in the buffer of the current deck, in the placement history of the players and in the hand of the perspective player
     * @param cards the queue of cards to cleanse
     * @param game the game being played
     * @param currentDeck the deck that is currently being used
     * @param perspectivePlayer the nickname of the player whose hand is to be cleansed
     */
    private static void cleanseCardQueue(Queue<? extends Card> cards, Game game, Deck<? extends Card> currentDeck, String perspectivePlayer){
        //Remove all the cards that are in the buffer of the current deck
        for(Card card : currentDeck.getBuffer()){
            cards.remove(card);
        }

        //Remove all the cards that are in the placement history of the players
        game.getGameParty().getPlayersList().forEach(player -> {
            player.getUserCodex().getPlacementHistory().forEach(placement -> {
                cards.remove(placement.card());
            });

            //Remove all the cards that are in the hand of the perspective player
            if(Objects.equals(player.getNickname(), perspectivePlayer)){
                player.getUserHand().getHand().forEach(cards::remove);
            }
        });
    }

    /**
     * This method builds a DeckBelief of ResourceCards by removing all the known cards to the perspectivePlayers
     * @param game the game being played
     * @param currentDeck the deck that is currently being used
     * @param perspectivePlayer the nickname of the player whose hand is to be cleansed
     * @return the DeckBelief of ResourceCards
     */
    public static DeckBelief<ResourceCard> buildDeckBeliefResourceCard(Game game, Deck<ResourceCard> currentDeck, String perspectivePlayer){
        Queue<ResourceCard> resourceCards = cardTable.getCardLookUpResourceCard().getQueue();
        cleanseCardQueue(resourceCards, game, currentDeck, perspectivePlayer);
        int drawsLeft = currentDeck.numberOfDrawsLeft();
        return new DeckBelief<ResourceCard>(resourceCards, currentDeck.getBuffer(), drawsLeft);
    }

    /**
     * This method builds a DeckBelief of GoldCards by removing all the known cards to the perspectivePlayers
     * @param game the game being played
     * @param currentDeck the deck that is currently being used
     * @param perspectivePlayer the nickname of the player whose hand is to be cleansed
     * @return the DeckBelief of GoldCards
     */
    public static DeckBelief<GoldCard> buildDeckBeliefGoldCard(Game game, Deck<GoldCard> currentDeck, String perspectivePlayer) {
        Queue<GoldCard> goldCards = cardTable.getCardLookUpGoldCard().getQueue();
        cleanseCardQueue(goldCards, game, currentDeck, perspectivePlayer);
        int drawsLeft = currentDeck.numberOfDrawsLeft();
        return new DeckBelief<GoldCard>(goldCards, currentDeck.getBuffer(), drawsLeft);
    }
}
