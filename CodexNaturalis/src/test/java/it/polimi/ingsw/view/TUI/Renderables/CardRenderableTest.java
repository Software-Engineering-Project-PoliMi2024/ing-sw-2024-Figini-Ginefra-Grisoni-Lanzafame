package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.ResourceCard;
import it.polimi.ingsw.model.tableReleted.Game;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CardRenderableTest {

    @Test
    void render() {
        Game game = new Game("test", 2);

        game.getResourceCardDeck().getBuffer().forEach(card -> {
            CardRenderable cardRenderable = new CardRenderable(card, CardFace.FRONT);
            cardRenderable.render();
        });

        assert true;
    }

    @Test
    void update() {
    }

    @Test
    void updateInput() {
    }
}