package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * This class represents the Rulebook.
 * scsd  w su qdw jb  qdbs  wd
 */
public class RuleBook {
    private final ImageView rulebookView;
    private final PopUp popUp;
    private final StackPane container = new StackPane();
    private int currentPage = 1;
    private final int totalPages = 12;

    public RuleBook(AnchorPane parent) {
        this.rulebookView = new ImageView(AssetsGUI.ruleBook(currentPage));

        this.popUp = new PopUp(parent, true);

        // Bind pages View's size to height while maintaining aspect ratio
        this.rulebookView.fitWidthProperty().bind(this.rulebookView.fitHeightProperty().multiply(rulebookView.getImage().getWidth() / rulebookView.getImage().getHeight()));
        this.rulebookView.fitHeightProperty().bind(Bindings.min(parent.heightProperty().multiply(0.8), parent.widthProperty().multiply(0.9).multiply(rulebookView.getImage().getHeight() / rulebookView.getImage().getWidth())));

        container.getChildren().add(rulebookView);

        rulebookView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double clickX = e.getX();
                double imageWidth = rulebookView.getBoundsInLocal().getWidth();
                if (clickX < imageWidth / 2) {
                    showPreviousPage();
                } else {
                    showNextPage();
                }
            }
        });

        popUp.getContent().getChildren().add(container);

        popUp.getContent().maxWidthProperty().bind(rulebookView.fitWidthProperty());
        popUp.getContent().maxHeightProperty().bind(rulebookView.fitHeightProperty());
    }

    private void showPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            rulebookView.setImage(AssetsGUI.ruleBook(currentPage));
        }
    }

    private void showNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            rulebookView.setImage(AssetsGUI.ruleBook(currentPage));
        }
    }

    public void show() {
        popUp.open();
    }

    public void hide() {
        popUp.close();
    }
}