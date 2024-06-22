package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Components.*;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CollectedCollectablesGUI;
import it.polimi.ingsw.view.GUI.Components.DeckRelated.DeckAreaGUI;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.Components.Logs.LogsGUI;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnChoice;
import it.polimi.ingsw.view.GUI.Root;

public class GameScene extends SceneGUI{
    private DeckAreaGUI deck;
    private HandGUI hand;
    private CodexGUI codex;
    private CollectedCollectablesGUI collectedCollectables;
    private LeaderboardGUI leaderboard;
    public GameScene() {
        super();

        content.getChildren().add(Root.GAME.getRoot());

        codex = new CodexGUI();
        codex.attachToCodex();

        hand = new HandGUI();
        hand.setCodex(codex);

        this.add(codex.getCodex());

        new DecorativeCorners(getContent());

        hand.addHandTo(getContent());

        LogsGUI logs = new LogsGUI(getContent());

        collectedCollectables = new CollectedCollectablesGUI();
        collectedCollectables.attachToCodex();
        collectedCollectables.addThisTo(getContent());

        deck = new DeckAreaGUI();
        deck.addThisTo(getContent());

        leaderboard = new LeaderboardGUI();
        leaderboard.addThisTo(getContent());
        leaderboard.attach();

        ObjectiveChoice objectiveChoice = new ObjectiveChoice(getContent());

        PawnChoice pawnChoice = new PawnChoice(getContent());
    }

}
