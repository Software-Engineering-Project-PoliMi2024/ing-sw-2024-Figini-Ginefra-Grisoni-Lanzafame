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

/**
 * This class represents the GUI component responsible for housing the end game popup.
 */
public class EndGamePopUp {
    /**
     * Creates a new EndGamePopUp.
     * @param parent the parent of the popup
     * @param leaderboard the leaderboard to display
     */
    public EndGamePopUp(AnchorPane parent, LeaderboardGUI leaderboard) {
        PopUp endGamePopUp = new PopUp(parent, true);
        endGamePopUp.getContent().setStyle("-fx-background-color: transparent");

        GUI.getStateProperty().addListener((obs, oldState, newState) -> {
            Platform.runLater(() -> {
                if(newState == StateGUI.GAME_ENDING){
                    populateScene(endGamePopUp);
                    leaderboard.open();
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

    /**
     * Populates the scene of the popup.
     * @param endGamePopUp the popup to populate
     */
    private void populateScene(PopUp endGamePopUp) {
        VBox filler = new VBox();
        filler.setStyle("-fx-background-color: transparent");
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

        endGamePopUp.getContent().maxHeightProperty().bind(filler.maxHeightProperty());
        endGamePopUp.getContent().maxWidthProperty().bind(filler.maxWidthProperty());
        endGamePopUp.getContent().getChildren().add(filler);
    }

    /**
     * Initializes the text of the popup.
     * @return the text of the popup
     */
    private VBox initializeText() {
        Text title = new Text("Game Over!");
        title.setStyle("-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: white;");
        Text subTitle = new Text("Congratulations to the winners: ");
        subTitle.setStyle("-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: white;");
        VBox vbox = new VBox(title, subTitle);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        return vbox;
    }

    /**
     * Initializes the winners of the popup.
     * @return the winners of the popup
     */
    private VBox initializeWinners() {
        VBox winners = new VBox();
        winners.setAlignment(Pos.CENTER);
        winners.setSpacing(20);
        GUI.getLightGame().getWinners().forEach(winner -> {
            Text winnerText = new Text(winner);
            winnerText.setStyle("-fx-font-size: 24px; " +
                    "-fx-font-weight: bold; ");
            Color color = PawnsGui.getPawnGui(GUI.getLightGame().getLightGameParty().getPlayerColor(winner)) == null ?
                    Color.WHITE : PawnsGui.getColor(GUI.getLightGame().getLightGameParty().getPlayerColor(winner));
            winnerText.setFill(color);
            winners.getChildren().add(winnerText);
        });
        return winners;
    }

    /**
     * Initializes the button of the popup.
     * @return the button of the popup
     */
    private Button initializeButton() {
        Button disconnectButton = new Button("Disconnect");
        disconnectButton.getStyleClass().add("accent");
        disconnectButton.setOnMouseClicked(e -> goToMenu());
        return disconnectButton;
    }

    /**
     * Goes to the menu.

     */
    private void goToMenu() {
        try {
            GUI.getControllerStatic().leave();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
