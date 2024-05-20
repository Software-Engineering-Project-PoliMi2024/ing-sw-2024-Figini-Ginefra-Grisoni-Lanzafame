package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffs.game.HandDiffAddOneSecretObjectiveOption;
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
        CardMuseum museum = new CardMuseumFactory(Configs.CardFolder).getCardMuseum();
        LightGame lightGame = new LightGame();
        renderable = new ChooseObjectiveCardRenderable("name", museum, lightGame, new CommandPrompt[]{}, null);
        LightCard objectiveCard1 = new LightCard(87);
        LightCard objectiveCard2 = new LightCard(91);
        GameDiff diff = new HandDiffAddOneSecretObjectiveOption(objectiveCard1);
        diff.apply(lightGame);

        diff = new HandDiffAddOneSecretObjectiveOption(objectiveCard2);
        diff.apply(lightGame);
    }

    @Test
    void displayOptions() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS, new String[]{});
        renderable.updateCommand(answer);
    }
}