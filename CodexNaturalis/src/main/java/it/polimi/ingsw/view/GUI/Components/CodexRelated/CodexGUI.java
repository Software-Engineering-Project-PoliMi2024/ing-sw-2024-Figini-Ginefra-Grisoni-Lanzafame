package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.CardRelated.CardGUI;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnsGui;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.*;

/**
 * This class represents the codex of the player in the GUI
 */
public class CodexGUI implements Observer {
    /** The pane that contains the codex */
    private final StackPane codex = new StackPane();
    /** The center of the codex */
    private Point2D center = new Point2D(0, 0);
    /** The scale of the codex */
    private double scale = 1;
    /** The past x position of the mouse */
    private double pastX = 0;
    /** The past y position of the mouse */
    private double pastY = 0;
    /** The list of cards in the codex */
    private final List<CardGUI> cards = new ArrayList<>();
    /** The list of frontier cards in the codex */
    private final List<FrontierCardGUI> frontier = new ArrayList<>();
    /** whether the frontier is visible or not */
    private boolean isFrontierVisible = false;
    /** The pawn of the player */
    private final ImageView pawn = new ImageView();

    /**
     * Creates a new CodexGUI. It initializes the codex and sets the background as well as all the listeners to user input.
     */
    public CodexGUI() {
        AnchorPane.setBottomAnchor(codex, 0.0);
        AnchorPane.setLeftAnchor(codex, 0.0);
        AnchorPane.setRightAnchor(codex, 0.0);
        AnchorPane.setTopAnchor(codex, 0.0);

        //Set the background
        this.updateBg();

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

                    this.updateBg();

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
                    this.setScaleAndUpdateTransformPawn(this.getScale());
                    this.updateBg();
                }
        );

        codex.getChildren().add(pawn);
    }

    /**
     * Updates the background of the codex as a grid of tiles.
     * The tiles are scaled according to the scale of the codex and are moved according to the center of the codex.
     */
    private void updateBg(){
        double width = 50 * this.getScale();
        double height = width;

        BackgroundImage bgImage = new BackgroundImage(
                AssetsGUI.bgTile,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                new BackgroundPosition(
                        null,
                        -width/2 + this.center.getX() + (codex.getWidth() - width)/2,
                        false,
                        null,
                        -height/2 + this.center.getY() + (codex.getHeight() - height)/2,
                        false
                ),
                new BackgroundSize(
                        width,
                        height,
                        false,
                        false,
                        false,
                        false
                ));
        codex.setBackground(new Background(bgImage));
    }

    /**
     * Attaches the codex to the codex of the player and to the party.
     */
    public void attachToCodex(){
        this.getLightCodex().attach(this);
        GUI.getLightGame().getLightGameParty().attach(this);
    }

    /**
     * Gets the light codex of the target player.
     * @return the codex of the target player.
     */
    protected LightCodex getLightCodex(){
        return GUI.getLightGame().getMyCodex();
    }

    /**
     * Returns the pane that contains the codex.
     * @return the pane that contains the codex.
     */
    public Pane getCodex() {
        return codex;
    }

    /**
     * Get the position of a card in the codex space given its position in the grid space.
     * The codex space is a space where the center of the codex is the origin and the x-axis is the horizontal axis of the grid space, expressed in pixels.
     * The grid space is the same coordinate system used in the model where the center of the grid is the origin and the x-axis is the horizontal axis expressed in card units.
     * @param position The position of the card in Grid space
     * @return The position of the card in the codex
     */
    private Point2D getCardPosition(Position position) {
        return new Point2D(
                center.getX() + position.getX() * GUIConfigs.codexGridWith * this.getScale(),
                center.getY() - position.getY() * GUIConfigs.codexGridHeight * this.getScale()
        );
    }

    /**
     * Get the position of a point in the grid space given its position in the codex space.
     * The codex space is a space where the center of the codex is the origin and the x-axis is the horizontal axis of the grid space, expressed in pixels.
     * The grid space is the same coordinate system used in the model where the center of the grid is the origin and the x-axis is the horizontal axis expressed in card units.
     * @param point The point in the codex space
     * @return The position of the point in the grid space
     */
    public Point2D getGridPosition(Point2D point) {
        return new Point2D(
                (point.getX() - center.getX()) / (GUIConfigs.codexGridWith) / this.getScale(),
                -(point.getY() - center.getY()) / (GUIConfigs.codexGridHeight) / this.getScale()
        );
    }

    /**
     * Adds a node to the codex. This method also takes care of the order of the nodes in the codex ensuring that the pawn is always on top of the start card but behind the other cards.
     * @param node the node to add.
     */
    public synchronized void addToCodex(Node node){
        codex.getChildren().add(node);

        if(codex.getChildren().size() >= 3 && codex.getChildren().indexOf(pawn) != 2){
            codex.getChildren().remove(pawn);
            codex.getChildren().add(2, pawn);
        }
        else if(codex.getChildren().size() < 3){
            codex.getChildren().remove(pawn);
            codex.getChildren().add(pawn);
       }

    }

    /**
     * Adds a card to the codex. The card's position and scale are set according to the codex space.
     * @param card the card to add.
     * @param position the position of the card in the grid space.
     */
    public synchronized void addCard(CardGUI card, Position position) {
        cards.add(card);

        this.addToCodex(card.getImageView());

        //Anchor the card to the center
        codex.setAlignment(Pos.CENTER);

        Point2D cardPosition = getCardPosition(position);
        card.setTranslation(cardPosition.getX(), cardPosition.getY());
        card.setScale(this.getScale());
    }

    /**
     * Sets the center of the codex.
     * @param x the x coordinate of the center.
     * @param y the y coordinate of the center.
     */
    public void setCenter(double x, double y) {
        center = new Point2D(x, y);
    }

    /**
     * Sets the center of the codex.
     * @param center the center of the codex.
     */
    public void setCenter(Point2D center) {
        this.center = center;
    }

    /**
     * Returns the nickname of the target player.
     * @return the nickname of the target player.
     */
    public String getTargetPlayer(){
        return GUI.getLightGame().getLightGameParty().getYourName();
    }

    /**
     * Updates the scale and the position of the pawn in the codex space.
     * @param scale the scale of the codex.
     */
    private void setScaleAndUpdateTransformPawn(double scale){
        pawn.setScaleX(scale);
        pawn.setScaleY(scale);
        Point2D center = this.getCardPosition(new Position(0, 0));
        pawn.setTranslateX(center.getX());
        pawn.setTranslateY(center.getY());
    }

    /**
     * Updates the codex according to the light codex. This is called by the Observer pattern.
     */
    public synchronized void update(){
        int n = this.getLightCodex().getPlacementHistory().size();

        //Checks if the codex has to be updated
        if(n > this.cards.size()){
            //Adds all the new cards
            for(int i = this.cards.size(); i < n; i++){
                LightPlacement target = this.getLightCodex().getPlacementHistory().get(i);
                this.addCard(new CardGUI(target.card(), target.face()), target.position());
            }
        }

        // The set of the positions that are still in the frontier
        Set<Position> staySet = new HashSet<>();

        List<Position> newFrontier = this.getLightCodex().getFrontier().frontier();

        Set<Position> currentFrontier = new HashSet<>(this.frontier.stream().map(FrontierCardGUI::getGridPosition).toList());

        //Add the new Elements
        for(Position newElement : newFrontier){
            staySet.add(newElement);
            //If the element is not in the current frontier add it
            if(!currentFrontier.contains(newElement)){
                //Add the new element
                Point2D cardPos = this.getCardPosition(newElement);
                FrontierCardGUI fc = new FrontierCardGUI(newElement, cardPos.getX(), cardPos.getY());
                this.addToCodex(fc.getCard());
                fc.setVisibility(isFrontierVisible);
                fc.setScale(this.getScale());
                frontier.add(fc);
            }
        }

        //Remove the old elements, the elements that are not in the new frontier
        for(int i = frontier.size() - 1; i >= 0; i--){
            FrontierCardGUI fc = frontier.get(i);
            if(!staySet.contains(fc.getGridPosition())){
                codex.getChildren().remove(fc.getCard());
                frontier.remove(fc);
            }
        }

        //Update the position of the pawn
        String myNickname = this.getTargetPlayer();
        PawnColors myColor = GUI.getLightGame().getLightGameParty().getPlayerColor(myNickname);

        //If the pawn is not yet set and the color is not null set the pawn
        if(pawn.getImage()==null && myColor != null){
            pawn.setImage(Objects.requireNonNull(PawnsGui.getPawnGui(myColor)).getImageView().getImage());
            pawn.setFitHeight(50);
            pawn.setFitWidth(50);
            Point2D center = this.getCardPosition(new Position(0, 0));
            pawn.setTranslateX(center.getX());
            pawn.setTranslateY(center.getY());
        }

    }

    /**
     * Returns the frontier card that is closest to the given point.
     * @param point the point to snap from.
     * @return the frontier card that is closest to the given point.
     */
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

    /**
     * Toggles the visibility of the frontier.
     * @param isFrontierVisible true if the frontier is visible, false otherwise.
     */
    public void toggleFrontier(boolean isFrontierVisible){
        this.isFrontierVisible = isFrontierVisible;
        for(FrontierCardGUI r : frontier){
            r.setVisibility(isFrontierVisible);
        }
    }

    /**
     * Returns the scale of the codex.
     * @return the scale of the codex.
     */
    public double getScale() {
        return scale * scale;
    }

    /**
     * Removes a card from the codex.
     * @param card the card to remove.
     */
    public synchronized void removeCard(CardGUI card){
        codex.getChildren().remove(card.getImageView());
        cards.remove(card);
    }
}
