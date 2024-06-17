package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.*;

public class CodexGUI implements Observer {
    private final StackPane codex = new StackPane();

    private Point2D center = new Point2D(0, 0);

    private double scale = 1;

    private double pastX = 0;
    private double pastY = 0;

    private final List<CardGUI> cards = new ArrayList<>();
    private final List<FrontierCardGUI> frontier = new ArrayList<>();

    private boolean isFrontierVisible = false;


    public CodexGUI() {
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
                    this.setCenter(center.getX() + deltaX, center.getY() + deltaY);

                    for (int i = 0; i < codex.getChildren().size(); i++) {
                        Node target = codex.getChildren().get(i);
                        target.setTranslateX(target.getTranslateX() + deltaX);
                        target.setTranslateY(target.getTranslateY() + deltaY);
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
                    this.cards.forEach(card -> card.setScaleAndUpdateTranslation(this.getScale(), this.center));
                    this.frontier.forEach(card -> card.setScaleAndUpdateTranslation(this.getScale(), this.center));
                }
        );
    }

    public void attachToCodex(){
        this.getLightCodex().attach(this);
    }

    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getMyCodex();
    }

    public Pane getCodex() {
        return codex;
    }

    /**
     * Get the position of a card in the codex.
     * @param position The position of the card in Grid space
     * @return The position of the card in the codex
     */
    private Point2D getCardPosition(Position position) {
        return new Point2D(
                center.getX() + position.getX() * GUIConfigs.codexGridWith * this.getScale(),
                center.getY() - position.getY() * GUIConfigs.codexGridHeight * this.getScale()
        );
    }

    public Point2D getGridPosition(Point2D point) {
        return new Point2D(
                (point.getX() - center.getX()) / (GUIConfigs.codexGridWith) / this.getScale(),
                -(point.getY() - center.getY()) / (GUIConfigs.codexGridHeight) / this.getScale()
        );
    }


    public synchronized void addCard(CardGUI card, Position position) {
        cards.add(card);
        codex.getChildren().add(card.getImageView());

        //Anchor the card to the center
        codex.setAlignment(Pos.CENTER);

        Point2D cardPosition = getCardPosition(position);
        card.setTranslation(cardPosition.getX(), cardPosition.getY());
        card.setScale(this.getScale());
//        card.getImageView().setTranslateX(cardPosition.first());
//        card.getImageView().setTranslateY(cardPosition.second());

    }

    public void setCenter(double x, double y) {
        center = new Point2D(x, y);
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    public synchronized void update(){
        int n = this.getLightCodex().getPlacementHistory().size();

        if(n > this.cards.size()){
            for(int i = this.cards.size(); i < n; i++){
                LightPlacement target = this.getLightCodex().getPlacementHistory().get(i);
                this.addCard(new CardGUI(target.card(), target.face()), target.position());
            }
        }


        Set<Position> staySet = new HashSet<>();
        List<Position> newFrontier = this.getLightCodex().getFrontier().frontier();
        Set<Position> currentFrontier = new HashSet<>(this.frontier.stream().map(FrontierCardGUI::getGridPosition).toList());

        //Add the new Elements
        for(Position newElement : newFrontier){
            staySet.add(newElement);
            if(!currentFrontier.contains(newElement)){
                Point2D cardPos = this.getCardPosition(newElement);
                FrontierCardGUI fc = new FrontierCardGUI(newElement, cardPos.getX(), cardPos.getY());
                codex.getChildren().add(fc.getCard());
                fc.setVisibility(isFrontierVisible);
                fc.setScale(this.getScale());
                frontier.add(fc);
            }
        }

        //Remove the old elements
        for(int i = frontier.size() - 1; i >= 0; i--){
            FrontierCardGUI fc = frontier.get(i);
            if(!staySet.contains(fc.getGridPosition())){
                codex.getChildren().remove(fc.getCard());
                frontier.remove(fc);
            }
        }

    }

    public FrontierCardGUI snapToFrontier(Point2D point){
        //Find the closest frontier position
        return this.frontier.stream().min(
                (a, b) -> {
                    double distA = Math.pow(a.getCard().getTranslateX() - point.getX(), 2) + Math.pow(a.getCard().getTranslateY() - point.getY(), 2);
                    double distB = Math.pow(b.getCard().getTranslateX() - point.getX(), 2) + Math.pow(b.getCard().getTranslateY() - point.getY(), 2);
                    return Double.compare(distA, distB);
                }
        ).get();
    }

    public void toggleFrontier(boolean isFrontierVisible){
        this.isFrontierVisible = isFrontierVisible;
        for(FrontierCardGUI r : frontier){
            r.setVisibility(isFrontierVisible);
        }
    }

    public double getScale() {
        return scale * scale;
    }

    public synchronized void removeCard(CardGUI card){
        codex.getChildren().remove(card.getImageView());
        cards.remove(card);
    }
}
