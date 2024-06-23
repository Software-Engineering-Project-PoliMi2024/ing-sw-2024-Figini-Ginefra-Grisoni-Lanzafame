package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.CodexRelated.Peeker;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PawnsGui;
import it.polimi.ingsw.view.GUI.Components.PawnRelated.PlateauGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardGUI implements Observer {
    private VBox layout;

    private AnchoredPopUp popUp;

    private Map<String, Text> labelMap = new HashMap<>();
    private Map<String, ImageView> pawnImageViewMap = new HashMap<>();
    private final HBox buttonContainer = new HBox();
    private PlateauGUI plateau;
    private List<PawnsGui> availablePawns;

    public LeaderboardGUI() {
        this.layout = new VBox(10);
        this.layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-font-size: 16pt;");
        this.layout.setAlignment(Pos.CENTER_LEFT);
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
        popUp.setLocked(false);

        createLeaderboard(parent);

        layout.getChildren().add(buttonContainer);
        buttonContainer.setAlignment(Pos.CENTER);

        ChatButton chatButton = new ChatButton();
        chatButton.addThisTo(parent);
        chatButton.attach();

        ImageView plateauIcon = new ImageView(AssetsGUI.plateauIcon);
        plateauIcon.preserveRatioProperty().set(true);
        plateauIcon.fitHeightProperty().bind(chatButton.getChatButton().heightProperty().multiply(0.8));
        Button plateauButton = new Button("", plateauIcon);
        plateauButton.setStyle("-fx-background-color: transparent;");
        plateauButton.setOnAction(event -> plateau.show());

        buttonContainer.getChildren().add(plateauButton);
        buttonContainer.getChildren().add(chatButton.getChatButton());



        layout.prefHeightProperty().bind(popUp.getContent().prefHeightProperty());
        layout.prefWidthProperty().bind(popUp.getContent().prefWidthProperty());
    }

    private Map<String, Integer> getScores() {
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

                    Text label = new Text(e.getKey() + ": " + e.getValue());

                    label.setStyle("-fx-font-size: 16pt;");

                    label.scaleXProperty().bindBidirectional(label.scaleYProperty());

                    if (e.getKey().equals(GUI.getLightGame().getLightGameParty().getFirstPlayerName())) {
                        ImageView blackPawnView = new ImageView(AssetsGUI.pawnBlack);
                        blackPawnView.setFitHeight(30);
                        blackPawnView.setFitWidth(30);
                        row.getChildren().add(blackPawnView);
                    }

                    ImageView pawnView = new ImageView();
                    pawnView.setFitHeight(30);
                    pawnView.setFitWidth(30);
                    row.setSpacing(5);

                    pawnImageViewMap.put(e.getKey(), pawnView);
                    row.getChildren().add(pawnView);
                    row.getChildren().add(label);

                    labelMap.put(e.getKey(), label);

                    if(!e.getKey().equals(GUI.getLightGame().getLightGameParty().getYourName())){
                        Peeker peeker = new Peeker(parent, e.getKey());
                        ImageView openedEyeIcon = new ImageView(AssetsGUI.eye);
                        openedEyeIcon.setFitHeight(25);
                        openedEyeIcon.setFitWidth(25);

                        ImageView closedEyeIcon = new ImageView(AssetsGUI.closedEye);
                        closedEyeIcon.setFitHeight(25);
                        closedEyeIcon.setFitWidth(25);

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

            Boolean isActive = GUI.getLightGame().getLightGameParty().getPlayerActiveList().get(name);
            if(isActive != null && !isActive){
                System.out.println("strikethrough");
                label.setStyle(label.getStyle() + "-fx-strikethrough: true;");
            }
            else{
                label.setStyle(label.getStyle().replace("-fx-strikethrough: true;", ""));
            }
        });
    }

    @Override
    public void update() {
        updateLeaderboard();
    }
}