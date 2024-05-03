package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.SignificantPaths;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.GameDiff;
import it.polimi.ingsw.lightModel.diffs.HandDiffAdd;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.TUI.Renderables.CardRelated.ChooseObjectiveCardRenderable;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChooseObjectiveCardRenderableTest {
    ChooseObjectiveCardRenderable renderable;
    @BeforeEach
    void setUp() {
        CardMuseum museum = new CardMuseumFactory(SignificantPaths.CardFolder).getCardMuseum();
        LightGame lightGame = new LightGame();
        renderable = new ChooseObjectiveCardRenderable("name", museum, lightGame, new CommandPrompt[]{}, null);
        LightCard objectiveCard1 = new LightCard(87);
        LightCard objectiveCard2 = new LightCard(91);
        GameDiff diff = new HandDiffAdd(objectiveCard1, true);
        diff.apply(lightGame);

        diff = new HandDiffAdd(objectiveCard2, true);
        diff.apply(lightGame);
    }

    @Test
    void displayOptions() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS, new String[]{});
        renderable.updateCommand(answer);
    }
}