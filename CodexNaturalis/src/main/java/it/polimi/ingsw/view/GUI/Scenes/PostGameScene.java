package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnsGui;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.Root;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        VBox titleText = initializeText();
        AnchorPane.setTopAnchor(titleText, 50.0);
        AnchorPane.setLeftAnchor(titleText, 0.0);
        AnchorPane.setRightAnchor(titleText, 0.0);

        VBox winners = initializeWinners();
        AnchorPane.setTopAnchor(winners, 100.0);
        AnchorPane.setBottomAnchor(winners, 100.0);
        AnchorPane.setLeftAnchor(winners, 0.0);
        AnchorPane.setRightAnchor(winners, 0.0);

        VBox buttons = initializeButtons();
        AnchorPane.setBottomAnchor(buttons, 20.0); // Margin from the bottom
        AnchorPane.setLeftAnchor(buttons, 0.0);
        AnchorPane.setRightAnchor(buttons, 0.0);

        content.getChildren().addAll(titleText, winners, buttons);
    }

    private VBox initializeText() {
        Text title = new Text("Game Over! Congratulations to the winners: ");
        title.setStyle("-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;");
        VBox vbox = new VBox(title);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private VBox initializeWinners() {
        VBox winners = new VBox();
        winners.setAlignment(Pos.CENTER);
        winners.setSpacing(20);
        GUI.getLightGame().getWinners().forEach(winner -> {
            Text winnerText = new Text(winner);
            winnerText.setStyle("-fx-font-size: 16px; " +
                                "-fx-font-weight: bold;");
            Color color = PawnsGui.getPawnGui(GUI.getLightGame().getLightGameParty().getPlayerColor(winner)) == null ?
                    Color.BLACK : PawnsGui.getColor(GUI.getLightGame().getLightGameParty().getPlayerColor(winner));
            winnerText.setFill(color);
            winners.getChildren().add(winnerText);
        });
        return winners;
    }

    private VBox initializeButtons() {
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        playAgainButton.getStyleClass().add("accent");
        menuButton.getStyleClass().add("accent");
        menuButton.setOnMouseClicked(e -> goToMenu());
        buttons.getChildren().addAll(playAgainButton, menuButton);
        return buttons;
    }

    private void goToMenu() {
        try {
            GUI.getControllerStatic().leave();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setView(ActualView view) {
        PostGameScene.view = view;
    }
}