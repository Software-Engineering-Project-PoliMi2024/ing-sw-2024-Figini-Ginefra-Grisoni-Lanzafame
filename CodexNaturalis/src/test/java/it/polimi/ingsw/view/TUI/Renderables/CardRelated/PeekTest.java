package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.SignificantPaths;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderable;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderableOthers;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeekTest {
    CodexRenderableOthers codexRenderable;
    HandOthersRenderable handRenderable;
    @BeforeEach
    void setUp() {
        CardMuseum museum = new CardMuseumFactory(SignificantPaths.CardFolder).getCardMuseum();
        LightGame lightGame = new LightGame();

        codexRenderable = new CodexRenderableOthers("name", lightGame, museum, new CommandPrompt[]{}, null);
        handRenderable = new HandOthersRenderable("name", museum, lightGame, new CommandPrompt[]{}, null);

        GameDiff diff = new GameDiffPlayerActivity(List.of(new String[]{"Player1", "Player2"}), new ArrayList<>());
        diff.apply(lightGame);

        diff = new GameDiffGameNames("TestGame", "Player1");
        diff.apply(lightGame);

        List<LightPlacement> placements = List.of(new LightPlacement[]{
                new LightPlacement(new Position(0, 0), new LightCard(81), CardFace.FRONT)
        });

        List<Position> positions = List.of(new Position[]{
                new Position(1, 1)
        });

        diff = new CodexDiff("Player1", 0, null, placements, positions);
        diff.apply(lightGame);

        diff = new CodexDiff("Player2", 0, null, placements, positions);
        diff.apply(lightGame);

        diff = new HandOtherDiffAdd(Resource.INSECT, "Player2");
        diff.apply(lightGame);

        diff = new HandOtherDiffAdd(Resource.ANIMAL, "Player2");
        diff.apply(lightGame);

        diff = new HandOtherDiffAdd(Resource.PLANT, "Player2");
        diff.apply(lightGame);
    }


    @Test
    void render() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.PEEK, new String[]{"Player2"});
        handRenderable.updateCommand(answer);
        codexRenderable.updateCommand(answer);
    }
}