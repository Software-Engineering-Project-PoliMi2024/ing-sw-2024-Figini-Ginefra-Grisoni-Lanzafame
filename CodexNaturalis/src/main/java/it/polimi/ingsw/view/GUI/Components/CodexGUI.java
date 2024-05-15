package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FrontierCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodexGUI implements Observer {
    private final StackPane codex = new StackPane();

    private double center_x = 0;
    private double center_y = 0;

    private double scale = 1;

    private double pastX = 0;
    private double pastY = 0;

    private final List<CardGUI> cards = new ArrayList<>();
    private final List<FrontierCardGUI> frontier = new ArrayList<>();

    private boolean isFrontierVisible = false;


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
                new Position(0, 2),
                new Position(-2, 2),
                new Position(-2, 0),
                new Position(-2, -2),
                new Position(0, -2),
                new Position(2, -2),
                new Position(2, 0),

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

        codex.setOnScroll(
                e -> {
                    double deltaY = e.getDeltaY();

                    double newScale = scale + deltaY / 1000;

                    if(newScale < Math.sqrt(GUIConfigs.codexMinScale) || newScale > Math.sqrt(GUIConfigs.codexMaxScale)){
                        return;
                    }
                    scale += deltaY / 1000;
                    this.cards.forEach(card -> card.setScale(scale * scale));
                    this.frontier.forEach(card -> card.setScale(scale * scale));
                }
        );
    }

    public Pane getCodex() {
        return codex;
    }

    private Pair<Double, Double> getCardPosition(Position position) {
        CardGUI card = new CardGUI(new LightCard(6), CardFace.FRONT);
        return new Pair<>(
                center_x + position.getX() * GUIConfigs.codexGridWith * scale,
                center_y - position.getY() * GUIConfigs.codexGridHeight * scale
        );
    }

    private Pair<Double, Double> getGridPosition(Double sceneX, Double sceneY) {
        CardGUI card = new CardGUI(new LightCard(6), CardFace.FRONT);
        return new Pair<>(
                (sceneX - center_x) / (GUIConfigs.codexGridWith) / scale,
                -(sceneY - center_y) / (GUIConfigs.codexGridHeight) / scale
        );
    }


    public void addCard(CardGUI card, Position position) {
        cards.add(card);
        codex.getChildren().add(card.getImageView());

        //Anchor the card to the center
        codex.setAlignment(Pos.CENTER);

        Pair<Double, Double> cardPosition = getCardPosition(position);
        card.setTranslation(cardPosition.first(), cardPosition.second());
//        card.getImageView().setTranslateX(cardPosition.first());
//        card.getImageView().setTranslateY(cardPosition.second());


        card.getImageView().setOnMouseDragged(
                e -> {
                    card.setTranslation(e.getSceneX() - codex.getScene().getWidth()/2, e.getSceneY() - codex.getScene().getHeight()/2);
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

        codex.getChildren().removeAll(frontier.stream().map(FrontierCardGUI::getCard).toList());
        frontier.clear();
        for(Position p : GUI.getLightGame().getMyCodex().getFrontier().frontier()){
            Pair<Double, Double> pos = this.getCardPosition(p);
            FrontierCardGUI fc = new FrontierCardGUI(p, pos.first(), pos.second());
            fc.setVisibility(isFrontierVisible);
            frontier.add(fc);
            codex.getChildren().add(fc.getCard());
        }
    }

    public Pair<Double, Double> snapToFrontier(double x, double y){
        //Find the closest frontier position
        Pair<Double, Double> gridPos = this.getGridPosition(x, y);
//        Position closest = GUI.getLightGame().getMyCodex().getFrontier().frontier().stream().min(
//                (a, b) -> {
//                    double distA = Math.sqrt(Math.pow(a.getX() - gridPos.first(), 2) + Math.pow(a.getY() - gridPos.second(), 2));
//                    double distB = Math.sqrt(Math.pow(b.getX() - gridPos.first(), 2) + Math.pow(b.getY() - gridPos.second(), 2));
//                    return Double.compare(distA, distB);
//                }
//        ).get();

        Node closestCard = this.frontier.stream().min(
                (a, b) -> {
                    double distA = Math.sqrt(Math.pow(a.getCard().getTranslateX() - x, 2) + Math.pow(a.getCard().getTranslateY() - y, 2));
                    double distB = Math.sqrt(Math.pow(b.getCard().getTranslateX() - x, 2) + Math.pow(b.getCard().getTranslateY() - y, 2));
                    return Double.compare(distA, distB);
                }
        ).get().getCard();

        return new Pair<>(closestCard.getTranslateX(), closestCard.getTranslateY());
    }

    public void toggleFrontier(){
        isFrontierVisible = !isFrontierVisible;
        for(FrontierCardGUI r : frontier){
            r.setVisibility(isFrontierVisible);
        }
    }

    public double getScale() {
        return scale * scale;
    }
}
