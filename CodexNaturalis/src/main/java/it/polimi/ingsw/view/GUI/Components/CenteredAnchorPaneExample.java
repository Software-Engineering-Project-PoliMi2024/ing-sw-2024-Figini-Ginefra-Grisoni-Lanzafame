package it.polimi.ingsw.view.GUI.Components;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CenteredAnchorPaneExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create the parent AnchorPane
        AnchorPane parentPane = new AnchorPane();
        parentPane.setStyle("-fx-background-color: lightblue;");

        // Create the child AnchorPane
        AnchorPane childPane = new AnchorPane();
        childPane.setStyle("-fx-background-color: lightcoral;");
        childPane.setPrefSize(200, 100); // Initial preferred size for the child AnchorPane

        // Bind the maxWidthProperty to half of the parent's width
        childPane.maxWidthProperty().bind(parentPane.widthProperty().multiply(0.5));

        // Bind the prefWidthProperty to the maxWidthProperty to enforce the max width
        childPane.prefWidthProperty().bind(childPane.maxWidthProperty());

        // Center the childPane horizontally by binding its layoutXProperty
        Platform.runLater(() -> {
            childPane.translateXProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> (parentPane.getWidth() - childPane.getPrefWidth()) / 2,
                            parentPane.widthProperty(),
                            childPane.prefWidthProperty()
                    )
            );
        });

        // Ensure the childPane is always at the bottom of the parentPane with a 20 pixel margin
        AnchorPane.setBottomAnchor(childPane, 20.0);

        // Add the childPane to the parentPane
        parentPane.getChildren().add(childPane);

        Scene scene = new Scene(parentPane, 400, 300);
        primaryStage.setTitle("Centered AnchorPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
