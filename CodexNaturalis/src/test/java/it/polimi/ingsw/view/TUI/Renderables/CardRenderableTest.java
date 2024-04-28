package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

class CardRenderableTest {
    @Test
    void render() {
        System.out.println(Paths.get("").toAbsolutePath());
        Lobby lobby = new Lobby(2, "test", "test");
        Game game = new Game(lobby);

//        System.out.println("Resource Cards FRONT:");
//        game.getResourceCardDeck().getBuffer().forEach(card -> {
//            CardRenderable cardRenderable = new CardRenderable(card, CardFace.FRONT, null);
//            cardRenderable.render();
//        });
//
//        System.out.println("Resource Cards BACK:");
//        game.getResourceCardDeck().getBuffer().forEach(card -> {
//            CardRenderable cardRenderable = new CardRenderable(card, CardFace.BACK, null);
//            cardRenderable.render();
//        });
//
//        System.out.println("Gold Cards FRONT:");
//        game.getGoldCardDeck().getBuffer().forEach(card -> {
//            CardRenderable cardRenderable = new CardRenderable(card, CardFace.FRONT, null);
//            cardRenderable.render();
//        });
//
//        System.out.println("Gold Cards BACK:");
//        game.getGoldCardDeck().getBuffer().forEach(card -> {
//            CardRenderable cardRenderable = new CardRenderable(card, CardFace.BACK, null);
//            cardRenderable.render();
//        });
//
//        System.out.println("StartingCard Cards FRONT:");
//        game.getStartingCardDeck().getActualDeck().forEach(card -> {
//            CardRenderable cardRenderable = new CardRenderable(card, CardFace.FRONT, null);
//            cardRenderable.render();
//        });
//
//        System.out.println("StartingCard Cards BACK:");
//        game.getStartingCardDeck().getActualDeck().forEach(card -> {
//            CardRenderable cardRenderable = new CardRenderable(card, CardFace.BACK, null);
//            cardRenderable.render();
//        });

        assert true;
    }

    @Test
    void update() {
    }

    @Test
    void updateInput() {
    }
}