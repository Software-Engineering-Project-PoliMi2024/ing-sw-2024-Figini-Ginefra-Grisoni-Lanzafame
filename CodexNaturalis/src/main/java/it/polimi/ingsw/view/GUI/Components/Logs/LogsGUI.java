package it.polimi.ingsw.view.GUI.Components.Logs;

import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.GUI.Components.Utils.AnchoredPopUp;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class LogsGUI implements Observer {
    private final Pane parent;
    private final AnchoredPopUp popUp;
    private final Text lastLog = new Text();
    private final Timeline waiting = new Timeline();

    public LogsGUI(Pane parent){
        GUI.getLogMemory().attach(this);
        this.parent = parent;
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
                new KeyFrame(Duration.millis(2000))
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
        System.out.println("Logging: " + lastLog);
        lastLog.setText(lastLogString);
        popUp.open();

        waiting.playFromStart();
    }
}
