package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightCodex;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CodexRenderableTest {

    @Test
    void drawCodex() {
        CardMuseum cardMuseum = new CardMuseumFactory("./cards/").getCardMuseum();

        LightCodex codex = new LightCodex();
        codex.addPlacement(List.of(new LightPlacement[]{
                new LightPlacement(new Position(0, 0), new LightCard(new Random().nextInt(cardMuseum.getSize())), new Random().nextBoolean() ? CardFace.FRONT : CardFace.BACK),
                new LightPlacement(new Position(1, 1), new LightCard(new Random().nextInt(cardMuseum.getSize())), new Random().nextBoolean() ? CardFace.FRONT : CardFace.BACK),
                new LightPlacement(new Position(-1, -1), new LightCard(new Random().nextInt(cardMuseum.getSize())), new Random().nextBoolean() ? CardFace.FRONT : CardFace.BACK),
                new LightPlacement(new Position(2, 0), new LightCard(new Random().nextInt(cardMuseum.getSize())), new Random().nextBoolean() ? CardFace.FRONT : CardFace.BACK),
        }));

        codex.difFrontier(List.of(new Position[]{
                new Position(0, -2),
                new Position(1, -1),
                new Position(3, -1),
                new Position(3, 1),
                new Position(2, 2),
                new Position(0, 2),
                new Position(-1, 1),
                new Position(-2, 0),
                new Position(-2, -2),
        }), new ArrayList<>());


        CodexRenderable codexRenderable = new CodexRenderable(codex, cardMuseum, new CommandPrompt[]{}, null);

        codexRenderable.render();

        // The test is to check if the method runs without exceptions

        assertTrue(true);


    }
}