package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnsGui;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class EndGamePopUp {

    public EndGamePopUp(AnchorPane parent) {
        PopUp endGamePopUp = new PopUp(parent, true);

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            Platform.runLater(() -> {
                if(newState == StateGUI.GAME_ENDING) {
                    populateScene(endGamePopUp);
                    endGamePopUp.open();
                    endGamePopUp.setLocked(true);
                }
                else{
                    endGamePopUp.close();
                    endGamePopUp.setLocked(false);
                }
            });
        });

    }

    private void populateScene(PopUp endGamePopUp) {
        VBox filler = new VBox();
        filler.setAlignment(Pos.CENTER);
        filler.setSpacing(30);

        AnchorPane.setTopAnchor(filler, 0.0);
        AnchorPane.setBottomAnchor(filler, 0.0);
        AnchorPane.setLeftAnchor(filler, 0.0);
        AnchorPane.setRightAnchor(filler, 0.0);

        VBox text = initializeText();
        VBox winners = initializeWinners();
        Button disconnectButton = initializeButton();

        filler.getChildren().addAll(text, winners, disconnectButton);
        filler.setMaxWidth(700);
        filler.setMaxHeight(400);

        endGamePopUp.getContent().maxHeightProperty().bind(filler.maxHeightProperty());
        endGamePopUp.getContent().maxWidthProperty().bind(filler.maxWidthProperty());
        endGamePopUp.getContent().getChildren().add(filler);
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
            System.out.println(winner + " has won the game!");
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

    private Button initializeButton() {
        Button disconnectButton = new Button("Disconnect");
        disconnectButton.getStyleClass().add("accent");
        disconnectButton.setOnMouseClicked(e -> goToMenu());
        return disconnectButton;
    }

    private void goToMenu() {
        try {
            GUI.getControllerStatic().leave();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
