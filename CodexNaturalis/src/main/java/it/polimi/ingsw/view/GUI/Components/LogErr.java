package it.polimi.ingsw.view.GUI.Components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LogErr {
    static void display(String log){
        Stage logStage = new Stage();

        logStage.initModality(Modality.APPLICATION_MODAL);
        logStage.setTitle("Error");
        logStage.setMinWidth(250);

        Label label = new Label();
        label.setText(log);

        Button coleButton = new Button("Close");
        coleButton.setOnAction(e -> logStage.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, coleButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        logStage.setScene(scene);
        logStage.showAndWait();
    }
}
