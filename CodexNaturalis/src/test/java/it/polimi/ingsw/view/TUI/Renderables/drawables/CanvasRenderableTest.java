package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

class CanvasRenderableTest {

    @Test
    void update() {
    }

    @Test
    void updateInput() {
    }

    @Test
    void draw() {
        Lobby lobby = new Lobby(4, "Gianni", "game1");
        Game game = new Game(lobby);

        CanvasRenderable canvasRenderable = new CanvasRenderable(41, 10, null);

        CardRenderable cardRenderable = new CardRenderable(game.getResourceCardDeck().getBuffer().stream().findFirst().orElse(null), CardFace.FRONT, null);

        canvasRenderable.draw(cardRenderable, new Position(0, 0));

        canvasRenderable.draw(cardRenderable, new Position(1, 1));

        canvasRenderable.render();

        canvasRenderable.setCenter(new Position(1, 1));

        canvasRenderable.render();

        canvasRenderable.draw(cardRenderable, new Position(2, 2));

        canvasRenderable.render();

        assert true;
    }
}