package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.GUI.Components.*;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CodexGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.CollectedCollectablesGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.Peeker;
import it.polimi.ingsw.view.GUI.Components.HandRelated.HandGUI;
import it.polimi.ingsw.view.GUI.Components.LogsGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.Root;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameScene extends SceneGUI{
    private DeckGUI deck;
    private HandGUI hand;
    private CodexGUI codex;
    private CollectedCollectablesGUI collectedCollectables;
    private final LogsGUI logs = new LogsGUI();

    public GameScene() {
        super();

        //Game setup
//        ModelDiffs<LightGame> diff = new GameDiffInitialization(List.of(new String[]{"Player1", "Player2"}), new GameDiffGameName("TestGame"), new GameDiffYourName("Player1"));
//        diff.apply(GUI.getLightGame());
//
//        diff = new GameDiffPlayerActivity(List.of(new String[]{"Player1", "Player2"}), new ArrayList<>());
//        diff.apply(GUI.getLightGame());


        content.getChildren().add(Root.GAME.getRoot());

        codex = new CodexGUI();
        codex.attachToCodex();

//        cardsBox.alignmentProperty().setValue(Pos.CENTER);
//        cardsBox.setSpacing(10);
        hand = new HandGUI();
        hand.setCodex(codex);

        this.add(codex.getCodex());

        //main.getChildren().add(hand.getHand());

        this.add(logs.getLogsDisplay());

//        Peeker peeker = new Peeker(getContent(), "Player2");
//        Button button = new Button("Open Codex Others");
//        button.setOnAction(e -> peeker.open());


//        this.add(button);

        /*new AnchoredPopUp(getContent(), 0.2f, 0.6f, Pos.CENTER_RIGHT, 0.25f);
        new AnchoredPopUp(getContent(), 0.2f, 0.6f, Pos.CENTER_LEFT, 0.25f);*/

        hand.addHandTo(getContent());

        deck = new DeckGUI();
        deck.setHand(hand);
        deck.addDecksTo(getContent());

        collectedCollectables = new CollectedCollectablesGUI();
        collectedCollectables.attachToCodex();
        collectedCollectables.addThisTo(getContent());

/*
        List<LightPlacement> placements = List.of(new LightPlacement[]{
                new LightPlacement(new Position(0, 0), new LightCard(81, 81), CardFace.FRONT),
                new LightPlacement(new Position(-1, -1), new LightCard(4, 1), CardFace.BACK),
                new LightPlacement(new Position(1, -1), new LightCard(20, 11), CardFace.FRONT),
                new LightPlacement(new Position(-1, 1), new LightCard(16, 11), CardFace.BACK),

        });

        List<Position> positions = List.of(new Position[]{
                new Position(1, 1),
                new Position(0, 2),
                new Position(-2, 2),
                new Position(-2, 0),
                new Position(-2, -2),
                new Position(0, -2),
                new Position(2, -2),
                new Position(2, 0),

        });

        /*

        diff = new CodexDiff("Player1", 0, new HashMap<>(), placements, positions);
        diff.apply(GUI.getLightGame());

        diff = new CodexDiff("Player2", 0, new HashMap<>(), placements, positions);
        diff.apply(GUI.getLightGame());

        //get random card
        Random random = new Random();
        diff = new HandDiffAdd(new LightCard(random.nextInt(1, 81), 1), true);
        diff.apply(GUI.getLightGame());

        diff = new HandDiffAdd(new LightCard(random.nextInt(1, 81),1 ), false);
        diff.apply(GUI.getLightGame());

        diff = new HandDiffAdd(new LightCard(random.nextInt(1, 81),1), true);
        diff.apply(GUI.getLightGame());

        diff = new HandOtherDiffAdd(new LightBack(11), "Player2");
        diff.apply(GUI.getLightGame());

        diff = new HandOtherDiffAdd(new LightBack(11), "Player2");
        diff.apply(GUI.getLightGame());

        diff = new HandOtherDiffAdd(new LightBack(11), "Player2");
        diff.apply(GUI.getLightGame());

        diff = new HandDiffSetObj(new LightCard(random.nextInt(87, 103),1));
        diff.apply(GUI.getLightGame());*/
/*
        //The deckGui will not be able to render this diff because it is applied to the LightGame
        Random random = new Random();
        LightCard[] publicObj = new LightCard[2];
        publicObj[0] = new LightCard(random.nextInt(87, 103),1);
        publicObj[1] = new LightCard(random.nextInt(87, 103),1);
        diff = new GameDiffPublicObj(publicObj);
        diff.apply(GUI.getLightGame());

        diff = new DeckDiffBufferDraw(new LightCard(random.nextInt(1, 81),1), 0, DrawableCard.RESOURCECARD);
        diff.apply(GUI.getLightGame());

        diff = new DeckDiffBufferDraw(new LightCard(random.nextInt(1, 81),1), 1, DrawableCard.RESOURCECARD);
        diff.apply(GUI.getLightGame());

        diff = new DeckDiffBufferDraw(new LightCard(random.nextInt(1, 81),1), 0, DrawableCard.GOLDCARD);
        diff.apply(GUI.getLightGame());

        diff = new DeckDiffBufferDraw(new LightCard(random.nextInt(1, 81),1), 1, DrawableCard.GOLDCARD);
        diff.apply(GUI.getLightGame());

        diff = new DeckDiffDeckDraw( DrawableCard.RESOURCECARD, new LightBack(21));
        diff.apply(GUI.getLightGame());

        diff = new DeckDiffDeckDraw( DrawableCard.GOLDCARD, new LightBack(11));
        diff.apply(GUI.getLightGame());*/
    }

}
