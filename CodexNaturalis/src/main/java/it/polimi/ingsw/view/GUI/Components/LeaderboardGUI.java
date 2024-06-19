package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.Peeker;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardGUI implements Observer {
    private VBox layout;

    private AnchoredPopUp popUp;

    private Map<String, Label> labelMap = new HashMap<>();
    private Map<String, ImageView> pawnImageViewMap = new HashMap<>();
    private static Map<String, PawnsGui> playerpawnMap = new HashMap<>();
    private PlateauGUI plateau;
    private List<PawnsGui> availablePawns;

    public LeaderboardGUI() {
        this.layout = new VBox(10);
        this.layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-font-size: 16pt;");
        this.availablePawns = new ArrayList<>(Arrays.asList(PawnsGui.values()));
    }

    public void attach() {
        GUI.getLightGame().getCodexMap().values().forEach(codex -> codex.attach(this));
        GUI.getLightGame().getLightGameParty().attach(this);
    }

    public void addThisTo(AnchorPane parent) {
        this.popUp = new AnchoredPopUp(parent, 0.1f, 0.2f, Pos.CENTER_LEFT, 0.25f);
        this.plateau = new PlateauGUI(parent);

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
                    row.setAlignment(Pos.CENTER_LEFT);


                    ImageView pawnView = new ImageView();
                    pawnView.setFitHeight(16);
                    pawnView.setFitWidth(16);
                    row.setSpacing(10);

                    pawnImageViewMap.put(e.getKey(), pawnView);

                    Label label = new Label(e.getKey() + ": " + e.getValue());

                    label.setStyle("-fx-text-fill: white; -fx-font-size: 16pt;");

                    label.scaleXProperty().bindBidirectional(label.scaleYProperty());
                    row.getChildren().add(pawnView);
                    row.getChildren().add(label);

                    labelMap.put(e.getKey(), label);

                    if(!e.getKey().equals(GUI.getLightGame().getLightGameParty().getYourName())){
                        Peeker peeker = new Peeker(parent, e.getKey());
                        ImageView openedEyeIcon = new ImageView(AssetsGUI.eye);
                        openedEyeIcon.setFitHeight(16);
                        openedEyeIcon.setFitWidth(16);

                        ImageView closedEyeIcon = new ImageView(AssetsGUI.closedEye);
                        closedEyeIcon.setFitHeight(16);
                        closedEyeIcon.setFitWidth(16);

                        Button peekButton = new Button("", closedEyeIcon);
                        peekButton.setStyle("-fx-background-color: transparent;");

                        peekButton.setOnMouseEntered(event -> {
                            peekButton.setGraphic(openedEyeIcon);
                            event.consume();
                        });

                        peekButton.setOnMouseExited(event -> {
                            peekButton.setGraphic(closedEyeIcon);
                            event.consume();
                        });

                        peekButton.setOnAction(event -> {
                            peeker.open();
                            event.consume();
                        });

                        row.getChildren().add(peekButton);
                    }
                    layout.getChildren().add(row);
                });

        // Add event handler to show plateau
        layout.setOnMouseClicked(event -> {
            Window owner = parent.getScene().getWindow();
            plateau.show();
        });
    }

    private void updateLeaderboard() {
        Map<String, Integer> scores = this.getScores();

        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    if(!labelMap.containsKey(e.getKey())){
                        return;
                    }

                    labelMap.get(e.getKey()).setText(e.getKey() + ": " + e.getValue());
                    PawnColors playerColor = GUI.getLightGame().getLightGameParty().getPlayerColor(e.getKey());

                    if(playerColor != null){
                        pawnImageViewMap.get(e.getKey()).setImage(Objects.requireNonNull(PawnsGui.getPawnGui(playerColor)).getImageView().getImage());
                        plateau.setScore(playerColor, e.getValue());
                    }
                });

        labelMap.forEach((name, label) -> {
            if(GUI.getLightGame().getLightGameParty().getCurrentPlayer().equals(name)){
                //Make the label underlined
                label.setStyle(label.getStyle() + "-fx-underline: true;");
            }
            else{
                label.setStyle(label.getStyle().replace("-fx-underline: true;", ""));
            }
        });
    }

    @Override
    public void update() {
        updateLeaderboard();
    }
}