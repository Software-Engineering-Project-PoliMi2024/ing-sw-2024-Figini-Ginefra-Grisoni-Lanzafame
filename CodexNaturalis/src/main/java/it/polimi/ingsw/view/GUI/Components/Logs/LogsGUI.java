package it.polimi.ingsw.view.GUI.Components.Logs;

import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class LogsGUI implements Observer {
    ListView<String> logsDisplay = new ListView<>();

    /**
     * Constructor for the LogsGUI class
     */
    public LogsGUI(){
        GUI.getLogMemory().attach(this);
        AnchorPane.setBottomAnchor(logsDisplay, 10.0);
        AnchorPane.setRightAnchor(logsDisplay, 10.0);
        logsDisplay.setPrefWidth(300);
        logsDisplay.setPrefHeight(200);
    }

    /**
     * Customizable constructor for the LogsGUI class
     * @param fromBottom pixel from the bottom
     * @param fromRight pixel from the right
     * @param width width in pixel of the list view
     * @param height height in pixel of the list view
     */
    public LogsGUI(double fromBottom, double fromRight, int width, int height){
        GUI.getLogMemory().attach(this);
        AnchorPane.setBottomAnchor(logsDisplay, fromBottom);
        AnchorPane.setRightAnchor(logsDisplay, fromRight);
        logsDisplay.setPrefWidth(width);
        logsDisplay.setPrefHeight(height);
    }

    /**
     * Updates the logs display when called by the observed
     */
    @Override
    public void update() {
        logsDisplay.getItems().clear();
        logsDisplay.getItems().addAll(GUI.getLogMemory().getLogs());
    }

    /**
     * Getter for the logs display
     * @return listView logsDisplay
     */
    public ListView<String> getLogsDisplay() {
        return logsDisplay;
    }
}
