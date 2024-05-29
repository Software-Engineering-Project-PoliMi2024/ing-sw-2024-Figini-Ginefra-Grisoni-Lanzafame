package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiffSetObj;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandRenderableTest {
    HandRenderable renderable;
    @BeforeEach
    void setUp() {
        CardMuseum museum = new CardMuseumFactory(Configs.CardFolder).getCardMuseum();
        LightGame lightGame = new LightGame();
        renderable = new HandRenderable("name", museum, lightGame, new CommandPrompt[]{}, null);
        GameDiff diff = new HandDiffAdd(new LightCard(5, 1), true);
        diff.apply(lightGame);
        diff = new HandDiffAdd(new LightCard(7, 1), false);
        diff.apply(lightGame);
        diff = new HandDiffAdd(new LightCard(9, 1), true);
        diff.apply(lightGame);
        diff = new HandDiffSetObj(new LightCard(91, 87));
        diff.apply(lightGame);
    }

    @Test
    void displayHand() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_HAND, new String[]{"0"});
        renderable.updateCommand(answer);
    }

    @Test
    void displaySecretObjective() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_SECRET_OBJECTIVE, new String[]{});
        renderable.updateCommand(answer);
    }
}