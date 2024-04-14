package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.TUI.Renderables.drawables.CardRenderable;
import org.junit.jupiter.api.Test;

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