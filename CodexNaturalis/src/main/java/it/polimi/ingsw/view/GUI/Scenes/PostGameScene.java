package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.Root;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PostGameScene extends SceneGUI {
    private static ActualView view;
    private final Button playAgainButton = new Button("Play Again");
    private final Button menuButton = new Button("Main Menu");

    public PostGameScene() {
        super();
        content.getChildren().add(Root.POST_GAME.getRoot());
        populateScene();
    }

    private void populateScene() {
        VBox filler = new VBox();
        AnchorPane.setTopAnchor(filler, 0.0);
        AnchorPane.setBottomAnchor(filler, 0.0);
        AnchorPane.setLeftAnchor(filler, 0.0);
        AnchorPane.setRightAnchor(filler, 0.0);
        filler.setAlignment(Pos.CENTER);
        filler.getChildren().addAll(inizializeText(), initializeWinners(), inizializeButtons());
        content.getChildren().add(filler);
    }

    private Text inizializeText() {
        return new Text("Game Over! Congratulations to the winners: ");
    }

    private VBox initializeWinners() {
        VBox winners = new VBox();
        winners.setAlignment(Pos.CENTER);
        GUI.getLightGame().getWinners().forEach(winner -> winners.getChildren().add(new Text(winner)));
        return winners;
    }

    private VBox inizializeButtons() {
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(playAgainButton, menuButton);
        return buttons;
    }

    public void setView(ActualView view) {
        PostGameScene.view = view;
    }
}