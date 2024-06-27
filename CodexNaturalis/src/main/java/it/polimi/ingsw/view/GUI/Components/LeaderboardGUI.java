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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The LeaderboardGUI class is responsible for displaying the leaderboard in the GUI.
 * It implements the Observer interface to update the leaderboard dynamically based on changes in game state.
 */
public class LeaderboardGUI implements Observer {
    /** The layout container for the leaderboard elements */
    private VBox layout;

    /** The popup for displaying the leaderboard */
    private AnchoredPopUp leftAnchoredPopUp;

    /** A map to store player labels for the leaderboard */
    private Map<String, Text> labelMap = new HashMap<>();

    /** A map to store pawn images for each player */
    private Map<String, ImageView> pawnImageViewMap = new HashMap<>();

    /** The container for buttons */
    private final VBox buttonContainer = new VBox();

    /** The PlateauGUI object to display the plateau */
    private PlateauGUI plateau;

    /** The RuleBook object to display the rule book */
    private RuleBook ruleBook;

    /** The list of available pawns */
    private List<PawnsGui> availablePawns;

    /**
     * Constructs a LeaderboardGUI object and initializes its layout and available pawns.
     */
    public LeaderboardGUI() {
        this.layout = new VBox(10);
        this.layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-font-size: 16pt;");
        this.layout.setAlignment(Pos.CENTER_LEFT);
        this.availablePawns = new ArrayList<>(Arrays.asList(PawnsGui.values()));
    }

    /**
     * Attaches the observer to the necessary game components.
     */
    public void attach() {
        GUI.getLightGame().getCodexMap().values().forEach(codex -> codex.attach(this));
        GUI.getLightGame().getLightGameParty().attach(this);
    }

    /**
     * Adds this leaderboard to the specified parent layout.
     * @param parent the parent layout to which the leaderboard is added.
     */
    public void addThisTo(AnchorPane parent) {
        this.leftAnchoredPopUp = new AnchoredPopUp(parent, 0.1f, 0.2f, Pos.CENTER_LEFT, 0.25f);
        this.plateau = new PlateauGUI(parent);
        this.ruleBook = new RuleBook(parent);

        leftAnchoredPopUp.getContent().getChildren().add(layout);

        leftAnchoredPopUp.setLocked(true);
        leftAnchoredPopUp.open();

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
        plateauButton.setOnAction(event -> plateau.open());

        ImageView ruleBookIcon = new ImageView(AssetsGUI.ruleBookIcon);
        ruleBookIcon.preserveRatioProperty().set(true);
        ruleBookIcon.fitHeightProperty().bind(chatButton.getChatButton().heightProperty().multiply(0.6));
        Button ruleBookButton = new Button("", ruleBookIcon);
        ruleBookButton.setStyle("-fx-background-color: transparent;");
        ruleBookButton.setOnAction(event -> ruleBook.show());

        buttonContainer.getChildren().add(plateauButton);
        buttonContainer.getChildren().add(chatButton.getChatButton());
        buttonContainer.getChildren().add(ruleBookButton);

        layout.prefHeightProperty().bind(leftAnchoredPopUp.getContent().prefHeightProperty());
        layout.prefWidthProperty().bind(leftAnchoredPopUp.getContent().prefWidthProperty());
    }

    /**
     * Retrieves the scores of players from the game's codex map.
     * @return a map of player names to their scores.
     */
    private Map<String, Integer> getScores() {
        return GUI.getLightGame().getCodexMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPoints()));
    }

    /**
     * Creates the leaderboard and adds it to the specified parent layout.
     * @param parent the parent layout to which the leaderboard is added.
     */
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

                    StackPane pawnContainer = new StackPane();
                    pawnContainer.setAlignment(Pos.CENTER);

                    if (e.getKey().equals(GUI.getLightGame().getLightGameParty().getFirstPlayerName())) {
                        ImageView blackPawnView = new ImageView(AssetsGUI.pawnBlack);
                        blackPawnView.setFitHeight(35);
                        blackPawnView.setFitWidth(35);

                        pawnContainer.getChildren().add(blackPawnView);
                    }

                    ImageView pawnView = new ImageView();
                    pawnView.setFitHeight(30);
                    pawnView.setFitWidth(30);
                    row.setSpacing(5);

                    pawnImageViewMap.put(e.getKey(), pawnView);

                    pawnContainer.getChildren().add(pawnView);

                    row.getChildren().add(pawnContainer);
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

    /**
     * Updates the leaderboard by refreshing the scores and player information.
     */
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

            Boolean isActive = GUI.getLightGame().getLightGameParty().getPlayerActiveMap().get(name);
            if(isActive != null && !isActive){
                System.out.println("strikethrough");
                label.setStyle(label.getStyle() + "-fx-strikethrough: true;");
            }
            else{
                label.setStyle(label.getStyle().replace("-fx-strikethrough: true;", ""));
            }
        });
    }

    /**
     * Opens the leaderboard popup.
     */
    public void open() {
        leftAnchoredPopUp.open();
    }
 /** Updates the leaderboard when the observed game state changes.
     */
    @Override
    public void update() {
        updateLeaderboard();
    }
}