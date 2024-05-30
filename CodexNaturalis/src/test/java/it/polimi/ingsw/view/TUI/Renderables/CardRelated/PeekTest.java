package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.joinDiffs.GameDiffInitialization;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderableOthers;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PeekTest {
    CodexRenderableOthers codexRenderable;
    HandOthersRenderable handRenderable;
    @BeforeEach
    void setUp() {
        CardMuseum museum = new CardMuseumFactory(Configs.CardResourcesFolderPath, OSRelated.cardFolderDataPath).getCardMuseum();
        LightGame lightGame = new LightGame();

        codexRenderable = new CodexRenderableOthers("name", null, lightGame, museum, new CommandPrompt[]{});
        handRenderable = new HandOthersRenderable("name", museum, lightGame, new CommandPrompt[]{}, null);


        GameDiff diff = new GameDiffInitialization(List.of(new String[]{"Player1","Player2"}), "TestGame", "Player1", new GameDiffCurrentPlayer("Player1") );
        diff.apply(lightGame);
        diff = new GameDiffPlayerActivity(List.of(new String[]{"Player1", "Player2"}), new ArrayList<>());
        diff.apply(lightGame);

        List<LightPlacement> placements = List.of(new LightPlacement[]{
                new LightPlacement(new Position(0, 0), new LightCard(81), CardFace.FRONT)
        });

        List<Position> positions = List.of(new Position[]{
                new Position(1, 1)
        });

        diff = new CodexDiffPlacement("Player1", 0, new HashMap<>(), placements, positions);
        diff.apply(lightGame);

        diff = new CodexDiffPlacement("Player2", 0, new HashMap<>(), placements, positions);
        diff.apply(lightGame);

        diff = new HandOtherDiffAdd(new LightBack(31), "Player2");
        diff.apply(lightGame);

        diff = new HandOtherDiffAdd(new LightBack(21), "Player2");
        diff.apply(lightGame);

        diff = new HandOtherDiffAdd(new LightBack(41), "Player2");
        diff.apply(lightGame);
    }


    @Test
    void render() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.PEEK, new String[]{"Player2"});
        handRenderable.updateCommand(answer);
        codexRenderable.updateCommand(answer);
    }
}