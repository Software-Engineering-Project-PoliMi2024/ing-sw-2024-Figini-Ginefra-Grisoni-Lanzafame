package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.RMI.VirtualRMI.VirtualControllerRMI;
import it.polimi.ingsw.connectionLayer.Socket.VirtualSocket.VirtualControllerSocket;
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

/**
 * This class is the controller for the connection form SceneBuilder scene.
 */
public class ConnectionFormControllerGUI implements Initializable {
    /** The view */
    public static ViewInterface view;

    @FXML
    private ChoiceBox<String> protocolChoice;

    @FXML
    private TextField ipText;

    @FXML
    private TextField portText;

    @FXML
    private ImageView image;

    /**
     * This method is called when the connect button is pressed.
     * It creates the correct controller based on the protocol selected.
     * Then it fetches the ip and port from the text fields and connects to the server using the correct protocol.
     * @param actionEvent the event
     */
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

    /**
     * This method is called when the controller is initialized.
     * It adds the two possible protocols to the choice box and sets the default value to RMI.
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        protocolChoice.getItems().add("Socket");
        protocolChoice.getItems().add("RMI");
        protocolChoice.setValue("RMI");

        //make The font bigger
        protocolChoice.setStyle("-fx-font-size: 20px;");

        //image.setImage(CardMuseumGUI.loadCardFront(100));
    }
}
