package it.polimi.ingsw.view.GUI.Components;


import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class DecorativeCorners {
    public DecorativeCorners(Pane parent){
        for(CardCorner corner : CardCorner.values()){
            Image targetImage = AssetsGUI.loadCorner(corner);
            ImageView imageView = new ImageView(targetImage);

            //Bind size to parent
            imageView.setPreserveRatio(true);
            double aspectRation = targetImage.getWidth() / targetImage.getHeight();
            imageView.fitWidthProperty().bind(
                    Bindings.min(
                            parent.widthProperty(),
                            parent.heightProperty().multiply(aspectRation)
                    ).multiply(GUIConfigs.decorativeCornersPercentage)
            );

            //Set the right positioning
            Position offset = corner.getOffset();
            if(offset.getX() > 0)
                AnchorPane.setRightAnchor(imageView, .0);
            else
                AnchorPane.setLeftAnchor(imageView, .0);

            if(offset.getY() > 0)
                AnchorPane.setTopAnchor(imageView, .0);
            else
                AnchorPane.setBottomAnchor(imageView, .0);

            //Add to parent
            parent.getChildren().add(imageView);
        }
    }
}
