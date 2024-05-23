package it.polimi.ingsw.view.GUI.Components.Logs;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LogErr {
    public static void display(String log){
        Stage logStage = new Stage();
        VBox layout = new VBox(10);
        Scene scene = new Scene(layout);

        Label label = new Label();
        Button closeButton = new Button("Close");

        logStage.initModality(Modality.APPLICATION_MODAL);
        logStage.setTitle("Error");
        logStage.setMinWidth(250);

        label.setText(log);
        closeButton.setOnAction(e -> logStage.close());

        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        logStage.setScene(scene);
        logStage.showAndWait();
    }
}
