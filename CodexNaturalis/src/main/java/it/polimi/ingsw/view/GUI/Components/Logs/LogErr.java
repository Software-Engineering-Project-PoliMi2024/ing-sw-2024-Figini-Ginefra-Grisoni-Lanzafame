package it.polimi.ingsw.view.GUI.Components.Logs;

import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * This class represents the GUI component to display an error log.
 * It does so by opening a pop up with a red label.
 */
public class LogErr {
    /**
     * Displays the error log.
     * @param stackRoot the root of the stack
     * @param log the log to display
     */
    public static void display(AnchorPane stackRoot, String log){
        PopUp errorPopUp = new PopUp(stackRoot, true);

        Label label = getLabel(log);

        VBox filler = new VBox();
        filler.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(filler, 0.0);
        AnchorPane.setBottomAnchor(filler, 0.0);
        AnchorPane.setLeftAnchor(filler, 0.0);
        AnchorPane.setRightAnchor(filler, 0.0);
        filler.getChildren().add(label);

        //This magic numbers are because javaFx does not calculate the width and height of the label until it is displayed
        filler.setMaxWidth(GUIConfigs.logErrWidth);
        filler.setMaxHeight(GUIConfigs.logErrHeight);

        errorPopUp.getContent().maxHeightProperty().bind(filler.maxHeightProperty());
        errorPopUp.getContent().maxWidthProperty().bind(filler.maxWidthProperty());

        errorPopUp.getContent().getChildren().add(filler);
        errorPopUp.open();
    }

    /**
     * Returns the label to display the log.
     * @param log the log to display
     * @return the label to display the log
     */
    private static Label getLabel(String log) {
        Label label = new Label();
        label.setText(log);
        label.setStyle("-fx-font-size: 20px; " +
                        "-fx-text-fill: red; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px; " +
                        "-fx-text-alignment: center; " +
                        "-fx-border-color: red; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; ");
        return label;
    }
}
