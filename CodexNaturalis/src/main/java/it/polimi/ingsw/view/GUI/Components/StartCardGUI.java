package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.Components.CardRelated.FlippableCardGUI;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.sql.Time;
import java.util.Arrays;

public class StartCardGUI implements Observer {
    private final Pane startCardLayer = new Pane();
    private final Rectangle background = new Rectangle();
    private LightCard startCard = null;
    private FlippableCardGUI cardGUI;

    public StartCardGUI(AnchorPane parent) {
        GUI.getLightGame().getHand().attach(this);
        startCard = Arrays.stream(GUI.getLightGame().getHand().getCards()).toList().getLast();
        if(startCard != null)
            setupAndShowCardGUI(startCard);

        background.setStyle("-fx-fill: rgba(0, 0, 0, 0.3);");

        startCardLayer.prefHeightProperty().bind(parent.heightProperty());
        startCardLayer.prefWidthProperty().bind(parent.widthProperty());

        background.widthProperty().bind(parent.widthProperty());
        background.heightProperty().bind(parent.heightProperty());

        startCardLayer.getChildren().add(background);
        parent.getChildren().add(startCardLayer);
    }

    @Override
    public void update() {
        startCard = Arrays.stream(GUI.getLightGame().getHand().getCards()).toList().getLast();
        if(startCard != null)
            setupAndShowCardGUI(startCard);
    }

    private void setupAndShowCardGUI(LightCard startCard){
        cardGUI = new FlippableCardGUI(startCard);

        cardGUI.setOnHold(e->{
            try{
                System.out.println("Placed start card");
                LightPlacement startPlacement = new LightPlacement(new Position(0,0), startCard, cardGUI.getFace());
                GUI.getControllerStatic().place(startPlacement);
            }catch (Exception exception){}
        });
        startCardLayer.getChildren().add(cardGUI.getImageView());
    }
}
