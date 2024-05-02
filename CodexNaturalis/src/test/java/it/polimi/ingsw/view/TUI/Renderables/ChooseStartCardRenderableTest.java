package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.SignificantPaths;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.GameDiff;
import it.polimi.ingsw.lightModel.diffs.HandDiffAdd;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.TUI.Renderables.CardRelated.ChooseStartCardRenderable;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChooseStartCardRenderableTest {
    ChooseStartCardRenderable renderable;
    @BeforeEach
    void setUp() {
        CardMuseum museum = new CardMuseumFactory(SignificantPaths.CardFolder).getCardMuseum();
        LightGame lightGame = new LightGame();
        renderable = new ChooseStartCardRenderable("name", museum, lightGame, new CommandPrompt[]{}, null);
        LightCard startCard = new LightCard(82);
        GameDiff diff = new HandDiffAdd(startCard, true);
        diff.apply(lightGame);
    }

    @Test
    void displayFrontCommand() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_START_FRONT, new String[]{});
        renderable.updateCommand(answer);
        assert true;
    }

    @Test
    void displayBackCommand() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_START_BACK, new String[]{});
        renderable.updateCommand(answer);
        assert true;
    }
}