package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.TUI.Renderables.CardTextStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanvasRenderableTest {

    @Test
    void update() {
    }

    @Test
    void updateInput() {
    }

    @Test
    void draw() {
        Game game = new Game("test", 2);

        CanvasRenderable canvasRenderable = new CanvasRenderable(40, 20);

        CardRenderable cardRenderable = new CardRenderable(game.getResourceCardDeck().getBuffer().stream().findFirst().orElse(null), CardFace.FRONT);

        canvasRenderable.draw(cardRenderable, new Position(0, 0));

        canvasRenderable.draw(cardRenderable, new Position(1, 1));

        canvasRenderable.render();

        assert true;
    }
}