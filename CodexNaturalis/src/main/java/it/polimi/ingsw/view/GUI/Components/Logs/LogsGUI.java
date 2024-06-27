package it.polimi.ingsw.view.GUI.Components.Logs;

import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * This class represents the GUI component to display the logs of the game.
 * It does so by opening a pop up anchored to the top of the screen.
 * The log lasts for a certain amount of time and then the pop up closes.

 */
public class LogsGUI implements Observer {
    /** The pop up that contains the logs. */
    private final AnchoredPopUp popUp;

    /** The log to display. */
    private final Text lastLog = new Text();

    /** The timeline used to wait before closing the pop up. */
    private final Timeline waiting = new Timeline();


    /**
     * Creates a new LogsGUI.
     * @param parent the parent of the pop up.
     */
    public LogsGUI(Pane parent){
        GUI.getLogMemory().attach(this);
        this.popUp = new AnchoredPopUp((AnchorPane)parent, 0.2f, 0.16f, Pos.TOP_CENTER, 0.25f);
        popUp.setLocked(true);
        this.popUp.getContent().getChildren().add(lastLog);

        AnchorPane.setTopAnchor(lastLog, 70.0);
        AnchorPane.setLeftAnchor(lastLog, 0.0);
        AnchorPane.setRightAnchor(lastLog, 0.0);

        lastLog.setTextAlignment(TextAlignment.CENTER);
        lastLog.setStyle("-fx-font-size: 16;");
        lastLog.setWrappingWidth(200);

        popUp.getContent().setPadding(new Insets(20));


        waiting.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(GUIConfigs.logHoldingDuration))
        );

        waiting.setOnFinished(e -> popUp.close());

        popUp.close();
    }

    /**
     * Updates the logs display when called by the observed
     */
    @Override
    public void update() {
        String lastLogString = GUI.getLogMemory().getLastLog();
        lastLog.setText(lastLogString);
        popUp.open();

        waiting.playFromStart();
    }
}
