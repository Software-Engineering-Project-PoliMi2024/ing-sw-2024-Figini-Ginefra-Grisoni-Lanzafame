package it.polimi.ingsw.view.GUI.Components.Utils;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.scene.Node;

public class AnimationStuff {

    public static KeyValue createOpacityKeyValue(Node target, double value) {
        return new KeyValue(target.opacityProperty(), value, Interpolator.EASE_BOTH);
    }

    public static KeyValue createScaleKeyValue(Node target, double value) {
        return new KeyValue(target.scaleXProperty(), value, Interpolator.EASE_BOTH);
    }

    public static KeyValue createTranslateXKeyValue(Node target, double value) {
        return new KeyValue(target.translateXProperty(), value, Interpolator.EASE_BOTH);
    }

    public static KeyValue createTranslateYKeyValue(Node target, double value) {
        return new KeyValue(target.translateYProperty(), value, Interpolator.EASE_BOTH);
    }
}
