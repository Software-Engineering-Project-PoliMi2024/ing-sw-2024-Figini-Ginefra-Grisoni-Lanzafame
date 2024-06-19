package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.stream.Collectors;

public class PawnChoice implements Observer {
    private final VBox metaContainer = new VBox();
    private final VBox container = new VBox();
    private final Text label = new Text("Choose your pawn");
    private final HBox choiceBox = new HBox();
    private final AnchorPane parent;

    public PawnChoice(AnchorPane parent) {
        this.parent = parent;

        // Make it bold and centered
        label.setStyle("-fx-font-size: 40; -fx-font-weight: bold; -fx-text-fill: white;");
        label.getStyleClass().add("customFont");
        label.setTextAlignment(TextAlignment.CENTER);

        AnchorPane.setTopAnchor(metaContainer, 0.0);
        AnchorPane.setBottomAnchor(metaContainer, 0.0);
        AnchorPane.setLeftAnchor(metaContainer, 0.0);
        AnchorPane.setRightAnchor(metaContainer, 0.0);

        metaContainer.getChildren().add(container);

        metaContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        metaContainer.setAlignment(Pos.CENTER);

        container.getChildren().addAll(label, choiceBox);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("bordersCodexStyle");

        container.setMaxWidth(300);

        GUI.getLightGame().getLightGameParty().attach(this);

        choiceBox.setAlignment(Pos.CENTER);
        choiceBox.setSpacing(10);
        choiceBox.setPadding(new Insets(30));

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if (newState == StateGUI.CHOOSE_PAWN) {
                Platform.runLater(() -> parent.getChildren().add(metaContainer));
            }
            else{
                if(parent.getChildren().contains(metaContainer)){
                    Platform.runLater(() -> parent.getChildren().remove(metaContainer));
                }
            }
        });

        // Automatically assign black pawn to the first player
        if (GUI.getLightGame().getLightGameParty().getFirstPlayerName().equals(GUI.getLightGame().getLightGameParty().getYourName())) {
            try {
                //GUI.getControllerStatic().choosePawn(PawnColors.BLACK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAndShow() {
        choiceBox.getChildren().clear();

        List<PawnColors> availablePawns = Arrays.stream(PawnColors.values())
                .filter(color -> !color.equals(PawnColors.BLACK))
                .filter(color -> !GUI.getLightGame().getLightGameParty().getPlayersColor().containsValue(color))
                .collect(Collectors.toList());

        availablePawns.forEach(color -> {
            ImageView pawnView = getPawnImageView(color);
            pawnView.setFitHeight(100);
            pawnView.setFitWidth(100);
            pawnView.setOnMouseClicked(event -> choosePawn(color));
            choiceBox.getChildren().add(pawnView);
        });
    }

    private ImageView getPawnImageView(PawnColors color) {
        switch (color) {
            case BLUE:
                return new ImageView(AssetsGUI.pawnBleu);
            case GREEN:
                return new ImageView(AssetsGUI.pawnGreen);
            case YELLOW:
                return new ImageView(AssetsGUI.pawnJaune);
            case RED:
                return new ImageView(AssetsGUI.pawnRed);
        }
        return null;
    }

    private void choosePawn(PawnColors color) {
        try {
            GUI.getControllerStatic().choosePawn(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void update() {
        checkAndShow();
    }
}