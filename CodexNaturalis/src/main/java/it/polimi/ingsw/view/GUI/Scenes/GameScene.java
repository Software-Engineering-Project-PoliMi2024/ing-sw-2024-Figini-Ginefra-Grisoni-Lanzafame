package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Components.*;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CollectedCollectablesGUI;
import it.polimi.ingsw.view.GUI.Components.DeckRelated.DeckAreaGUI;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.Root;

public class GameScene extends SceneGUI{
    private DeckAreaGUI deck;
    private HandGUI hand;
    private CodexGUI codex;
    private CollectedCollectablesGUI collectedCollectables;
    private LeaderboardGUI leaderboard;
    private ObjectiveChoice objectiveChoice;

    private PawnChoice pawnChoice;
    private ChatButton chatButton;

    public GameScene() {
        super();

        content.getChildren().add(Root.GAME.getRoot());

        codex = new CodexGUI();
        codex.attachToCodex();

        hand = new HandGUI();
        hand.setCodex(codex);

        this.add(codex.getCodex());

        hand.addHandTo(getContent());

        deck = new DeckAreaGUI();
        deck.addThisTo(getContent());

        leaderboard = new LeaderboardGUI();
        leaderboard.addThisTo(getContent());
        leaderboard.attach();

        collectedCollectables = new CollectedCollectablesGUI();
        collectedCollectables.attachToCodex();
        collectedCollectables.addThisTo(getContent());

        objectiveChoice = new ObjectiveChoice(getContent());

        pawnChoice = new PawnChoice(getContent());

        chatButton = new ChatButton();
        chatButton.addThisTo(getContent());
        chatButton.attachToChat();
    }

}
