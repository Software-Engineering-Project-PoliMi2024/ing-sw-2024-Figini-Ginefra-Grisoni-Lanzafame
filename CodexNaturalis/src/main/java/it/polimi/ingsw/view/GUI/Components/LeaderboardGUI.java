package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.stream.Collectors;

public class LeaderboardGUI {
    private LightGame lightGame;
    private VBox layout;

    public LeaderboardGUI(LightGame lightGame) {
        this.lightGame = lightGame;
        this.layout = new VBox(10);
        this.layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-font-size: 16pt;");
        updateLeaderboard();
    }

    private void updateLeaderboard() {
        layout.getChildren().clear();
        Map<String, Integer> scores = lightGame.getCodexMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPoints()));

        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> layout.getChildren().add(new Label(e.getKey() + ": " + e.getValue())));
    }

    public VBox getContent() {
        return layout;
    }

    public void refresh() {
        updateLeaderboard();
    }
}