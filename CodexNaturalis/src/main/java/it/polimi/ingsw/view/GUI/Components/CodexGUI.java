package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodexGUI implements Observer {
    static private final int cardWidth = 100;
    static private final int cardHeight = 150;

    private final StackPane codex = new StackPane();

    private double center_x = 0;
    private double center_y = 0;

    private double pastX = 0;
    private double pastY = 0;


    public CodexGUI() {
        GameDiff diff = new GameDiffInitialization(List.of(new String[]{"Player1"}), new GameDiffGameName("TestGame"), new GameDiffYourName("Player1"));
        diff.apply(GUI.getLightGame());

        diff = new GameDiffPlayerActivity(List.of(new String[]{"Player1"}), new ArrayList<>());
        diff.apply(GUI.getLightGame());

        diff = new GameDiffGameName("TestGame");
        diff.apply(GUI.getLightGame());

        diff = new GameDiffYourName("Player1");
        diff.apply(GUI.getLightGame());


        GUI.getLightGame().getMyCodex().attach(this);



        List<LightPlacement> placements = List.of(new LightPlacement[]{
                new LightPlacement(new Position(0, 0), new LightCard(81), CardFace.FRONT),
                new LightPlacement(new Position(-1, -1), new LightCard(4), CardFace.BACK),
                new LightPlacement(new Position(1, -1), new LightCard(20), CardFace.FRONT),
                new LightPlacement(new Position(-1, 1), new LightCard(16), CardFace.BACK),

        });

        List<Position> positions = List.of(new Position[]{
                new Position(1, 1),
        });


        diff = new CodexDiff("Player1", 0, new HashMap<>(), placements, positions);
        diff.apply(GUI.getLightGame());



        AnchorPane.setBottomAnchor(codex, 0.0);
        AnchorPane.setLeftAnchor(codex, 0.0);
        AnchorPane.setRightAnchor(codex, 0.0);
        AnchorPane.setTopAnchor(codex, 0.0);

        codex.setOnMousePressed(
                e -> {
                    pastX = e.getSceneX();
                    pastY = e.getSceneY();
                }
        );

        codex.setOnMouseDragged(
                e -> {
                    double deltaX = e.getSceneX() - pastX;
                    double deltaY = e.getSceneY() - pastY;
                    this.setCenter(center_x + deltaX, center_y + deltaY);

                    for (int i = 0; i < codex.getChildren().size(); i++) {
                        codex.getChildren().get(i).setTranslateX(codex.getChildren().get(i).getTranslateX() + deltaX);
                        codex.getChildren().get(i).setTranslateY(codex.getChildren().get(i).getTranslateY() + deltaY);
                    }

                    pastX = e.getSceneX();
                    pastY = e.getSceneY();
                }
        );
    }

    public Pane getCodex() {
        return codex;
    }

    private Pair<Double, Double> getCardPosition(Position position) {
        CardGUI card = new CardGUI(new LightCard(6), CardFace.FRONT);
        return new Pair<>(
                center_x + position.getX() * card.getImageView().getImage().getWidth() * 0.3 * 0.75,
                center_y + position.getY() * card.getImageView().getImage().getHeight() * 0.3 * 0.6);
    }


    public void addCard(CardGUI card, Position position) {
        codex.getChildren().add(card.getImageView());

        //Anchor the card to the center
        codex.setAlignment(Pos.CENTER);

        Pair<Double, Double> cardPosition = getCardPosition(position);
        card.getImageView().setTranslateX(cardPosition.first());
        card.getImageView().setTranslateY(cardPosition.second());


        card.getImageView().setOnMouseDragged(
                e -> {
                    //Discretize using the card size
//                    double width = card.getImageView().getImage().getWidth() * 0.3 * 0.75;
//                    double height = card.getImageView().getImage().getHeight() * 0.3 * 0.6;
//                    card.getImageView().setTranslateX(Math.round((e.getSceneX()) / width - 0.5) * width);
//                    card.getImageView().setTranslateY(Math.round(e.getSceneY() / height - 0.5) * height);
                    card.getImageView().setTranslateX(e.getSceneX() - codex.getScene().getWidth()/2);
                    card.getImageView().setTranslateY(e.getSceneY() - codex.getScene().getHeight()/2);
                    e.consume();
                }
        );
    }

    public void setCenter(double x, double y) {
        this.center_x = x;
        this.center_y = y;
    }

    public void update(){
        int n = GUI.getLightGame().getMyCodex().getPlacementHistory().size();

        if(n > this.codex.getChildren().size()){
            for(int i = this.codex.getChildren().size(); i < n; i++){
                LightPlacement target = GUI.getLightGame().getMyCodex().getPlacementHistory().values().toArray(new LightPlacement[0])[i];
                this.addCard(new CardGUI(target.card(), target.face()), target.position());
            }
        }
    }
}
