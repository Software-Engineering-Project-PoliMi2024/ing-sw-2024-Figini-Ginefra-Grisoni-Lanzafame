package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.Reception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck<String> deck;
    @BeforeEach
    public void setUp() {
        deck = new Deck<>(2, new LinkedList<>(List.of("a", "b", "c", "d", "e")));
    }

    @Test
    public void drawFromDeck() {
        for(int i = 0; i < deck.getActualDeck().size(); i++){
            assertEquals(deck.getActualDeck().peek(), deck.drawFromDeck());
        }
        System.out.println(deck.getActualDeck().size());
    }

    @Test
    public void deckTest(){
        Reception reception = new Reception();
    }

}