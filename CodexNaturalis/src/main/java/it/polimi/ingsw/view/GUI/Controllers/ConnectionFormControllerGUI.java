package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualRMI.VirtualControllerRMI;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.ViewInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionFormControllerGUI implements Initializable {

    public static ViewInterface view;

    @FXML
    private ChoiceBox<String> protocolChoice;

    @FXML
    private TextField ipText;

    @FXML
    private TextField portText;

    @FXML
    private ImageView image;

    public void connect(ActionEvent actionEvent) {
        VirtualController controller = protocolChoice.getValue().equals("Socket") ? new VirtualControllerSocket() : new VirtualControllerRMI();
        GUI.setControllerStatic(controller);

        String ip = ipText.getText();
        int port = Integer.parseInt(portText.getText());

        try {
            controller.connect(ip, port, view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        protocolChoice.getItems().add("Socket");
        protocolChoice.getItems().add("RMI");
        protocolChoice.setValue("RMI");


        //image.setImage(CardMuseumGUI.loadCardFront(100));
    }
}
