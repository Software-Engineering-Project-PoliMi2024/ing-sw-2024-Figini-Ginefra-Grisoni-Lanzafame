package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.view.ViewInterface;
import javafx.event.ActionEvent;

public class ControllerGUI {
    private static VirtualController controller;
    private static ViewInterface view;

    private static GUI applicationGUI;

    public static void setController(VirtualController controller) {
        ControllerGUI.controller = controller;
    }

    public static void setView(ViewInterface view) {
        ControllerGUI.view = view;
    }

    public static void setApplicationGUI(GUI applicationGUI) {
        ControllerGUI.applicationGUI = applicationGUI;
    }

    public void connect(ActionEvent actionEvent) {
        System.out.println("Connecting to server...");
        try {
            controller.connect("0.0.0.0", 1234, view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
