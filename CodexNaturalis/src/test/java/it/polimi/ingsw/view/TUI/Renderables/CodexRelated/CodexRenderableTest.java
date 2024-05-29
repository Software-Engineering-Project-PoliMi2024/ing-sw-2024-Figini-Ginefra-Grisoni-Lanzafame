package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.joinDiffs.GameDiffInitialization;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CodexRenderableTest {
    CodexRenderable renderable;
    @BeforeEach
    void setUp() {
        CardMuseum museum = new CardMuseumFactory(Configs.CardFolder).getCardMuseum();
        LightGame lightGame = new LightGame();
        renderable = new CodexRenderable("name", lightGame, museum, new CommandPrompt[]{}, null);

        GameDiff diff = new GameDiffInitialization(List.of(new String[]{"Player1"}), "TestGame", "Player1", new GameDiffCurrentPlayer("Player1"));
        diff.apply(lightGame);

        diff = new GameDiffPlayerActivity(List.of(new String[]{"Player1"}), new ArrayList<>());
        diff.apply(lightGame);

        List<LightPlacement> placements = List.of(new LightPlacement[]{
                new LightPlacement(new Position(0, 0), new LightCard(81), CardFace.FRONT),
                new LightPlacement(new Position(-1, -1), new LightCard(82), CardFace.BACK),
                new LightPlacement(new Position(1, -1), new LightCard(85), CardFace.FRONT),
                new LightPlacement(new Position(-1, 1), new LightCard(86), CardFace.BACK),

        });

        List<Position> positions = List.of(new Position[]{
                new Position(1, 1),
        });


        diff = new CodexDiffPlacement("Player1", 0, new HashMap<>(), placements, positions);
        diff.apply(lightGame);
    }

    @Test
    void render() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_CODEX, new String[0]);
        renderable.updateCommand(answer);
    }
}