package it.polimi.ingsw.view.GUI.Components.PawnRelated;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
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

/**
 * This class represents the GUI component for choosing a pawn color.
 */
public class PawnChoice implements Observer {
    /** The main container for the pawn choice components */
    private final VBox container = new VBox();
    /** The label prompting the user to choose a pawn */
    private final Text label = new Text("Choose your pawn");
    /** The container for displaying available pawn choices */
    private final HBox choiceBox = new HBox();
    /** The parent container to which this component belongs */
    private final AnchorPane parent;
    /** The pop-up component for displaying the pawn choice */
    private final PopUp popUp;

    /**
     * Constructs a PawnChoice instance and initializes the necessary components.
     *
     * @param parent the parent AnchorPane to which this GUI component belongs.
     */
    public PawnChoice(AnchorPane parent) {
        this.parent = parent;

        // Make it bold and centered
        label.setStyle("-fx-font-size: 40; -fx-font-weight: bold; -fx-text-fill: white;");
        label.getStyleClass().add("customFont");
        label.setTextAlignment(TextAlignment.CENTER);

        popUp = new PopUp(parent, true);
        popUp.setLocked(true);

        popUp.getContent().getChildren().add(container);

        popUp.getContent().setMaxWidth(300);
        popUp.getContent().maxHeightProperty().bind(container.heightProperty());

        container.getChildren().addAll(label, choiceBox);
        container.setAlignment(Pos.CENTER);

        container.setMaxWidth(300);

        GUI.getLightGame().getLightGameParty().attach(this);

        choiceBox.setAlignment(Pos.CENTER);
        choiceBox.setSpacing(10);
        choiceBox.setPadding(new Insets(30));

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            if (newState == StateGUI.CHOOSE_PAWN) {
                Platform.runLater(popUp::open);
            }
            else{
                Platform.runLater(popUp::close);
            }
        });

        if(GUI.getStateProperty().get() == StateGUI.CHOOSE_PAWN){
            popUp.open();
        }
        else{
            popUp.close();
        }
    }

    /**
     * Checks the available pawns and updates the choice box with the available options.
     */
    private void checkAndShow() {
        choiceBox.getChildren().clear();

        List<PawnColors> availablePawns = Arrays.stream(PawnColors.values())
                .filter(color -> !color.equals(PawnColors.BLACK))
                .filter(color -> !GUI.getLightGame().getLightGameParty().getPlayersColor().containsValue(color))
                .toList();

        availablePawns.forEach(color -> {
            ImageView pawnView = getPawnImageView(color);
            pawnView.setFitHeight(100);
            pawnView.setFitWidth(100);
            pawnView.setOnMouseClicked(event -> choosePawn(color));
            choiceBox.getChildren().add(pawnView);
        });
    }

    /**
     * Returns the ImageView of the pawn based on its color.
     *
     * @param color the color of the pawn.
     * @return the ImageView of the pawn.
     */
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

    /**
     * Handles the pawn choice selection by the user.
     *
     * @param color the color of the chosen pawn.
     */
    private void choosePawn(PawnColors color) {
        try {
            GUI.getControllerStatic().choosePawn(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the GUI component when notified by the observer pattern.
     */
    @Override
    public synchronized void update() {
        checkAndShow();
    }
}