package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.Peeker;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaderboardGUI implements Observer {
    private VBox layout;

    private AnchoredPopUp popUp;

    private Map<String, Label> labelMap = new HashMap<>();


    public LeaderboardGUI() {
        this.layout = new VBox(10);
        this.layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-font-size: 16pt;");
    }

    public void attach() {
        GUI.getLightGame().getCodexMap().values().forEach(codex -> codex.attach(this));
    }

    public void addThisTo(AnchorPane parent) {
        this.popUp = new AnchoredPopUp(parent, 0.1f, 0.2f, Pos.CENTER_LEFT, 0.25f);

        popUp.getContent().getChildren().add(layout);

        popUp.open();
        popUp.setLocked(true);

        createLeaderboard(parent);

        layout.prefHeightProperty().bind(popUp.getContent().prefHeightProperty());
        layout.prefWidthProperty().bind(popUp.getContent().prefWidthProperty());
    }

    private Map<String, Integer> getScores(){
        return GUI.getLightGame().getCodexMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPoints()));
    }

    private void createLeaderboard(AnchorPane parent) {
        Map<String, Integer> scores = this.getScores();

        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    HBox row = new HBox();

                    Label label = new Label(e.getKey() + ": " + e.getValue());

                    label.setStyle("-fx-text-fill: white; -fx-font-size: 16pt;");

                    row.getChildren().add(label);

                    labelMap.put(e.getKey(), label);

                    if(!e.getKey().equals(GUI.getLightGame().getLightGameParty().getYourName())){
                        System.out.println("Adding peeker for " + e.getKey());
                        Peeker peeker = new Peeker(parent, e.getKey());

                        label.setOnMouseClicked(event -> peeker.open());

                        // make label underlined
                        label.setStyle("-fx-underline: true;" + label.getStyle());
                    }
                    layout.getChildren().add(row);
                });

    }

    private void updateLeaderboard() {
        Map<String, Integer> scores = this.getScores();

        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    labelMap.get(e.getKey()).setText(e.getKey() + ": " + e.getValue());
                });
    }

    @Override
    public void update() {
        updateLeaderboard();
    }
}