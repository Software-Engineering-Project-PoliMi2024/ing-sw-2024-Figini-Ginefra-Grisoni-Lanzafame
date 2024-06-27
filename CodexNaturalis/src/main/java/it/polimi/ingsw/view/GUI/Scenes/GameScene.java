package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Components.*;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CollectedCollectablesGUI;
import it.polimi.ingsw.view.GUI.Components.DeckRelated.DeckAreaGUI;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.Components.Logs.LogsGUI;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnChoice;
import it.polimi.ingsw.view.GUI.Root;

/**
 * The scene that contains the game board and all the other elements of the game

 */
public class GameScene extends SceneGUI{
    /**
     * Creates the game scene
     */
    public GameScene() {
        super();

        content.getChildren().add(Root.GAME.getRoot());

        CodexGUI codex = new CodexGUI();
        codex.attachToCodex();

        HandGUI hand = new HandGUI();
        hand.setCodex(codex);

        this.add(codex.getCodex());

        new DecorativeCorners(getContent());

        hand.addHandTo(getContent());

        LogsGUI logs = new LogsGUI(getContent());

        CollectedCollectablesGUI collectedCollectables = new CollectedCollectablesGUI();
        collectedCollectables.attachToCodex();
        collectedCollectables.addThisTo(getContent());

        DeckAreaGUI deck = new DeckAreaGUI();
        deck.addThisTo(getContent());

        LeaderboardGUI leaderboard = new LeaderboardGUI();
        leaderboard.addThisTo(getContent());
        leaderboard.attach();

        ObjectiveChoice objectiveChoice = new ObjectiveChoice(getContent());

        PawnChoice pawnChoice = new PawnChoice(getContent());

        EndGamePopUp endGame = new EndGamePopUp(getContent(), leaderboard);
    }

}
