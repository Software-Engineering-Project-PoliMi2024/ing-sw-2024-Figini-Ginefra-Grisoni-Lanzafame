package it.polimi.ingsw.view.GUI.Components.Utils;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.scene.Node;

/**
 * A utils class that contains methods to create KeyValues for animations.

 */
public class AnimationStuff {
    /**
     * Creates a KeyValue that animates the opacity of a node.
     * @param target the node to animate
     * @param value  the value to animate to
     * @return the KeyValue
     */
    public static KeyValue createScaleXKeyValue(Node target, double value) {
        return new KeyValue(target.scaleXProperty(), value, Interpolator.EASE_BOTH);
    }

    /**
     * Creates a KeyValue that animates the opacity of a node.
     * @param target the node to animate
     * @param value the value to animate to
     * @return the KeyValue
     */
    public static KeyValue createScaleYKeyValue(Node target, double value) {
        return new KeyValue(target.scaleYProperty(), value, Interpolator.EASE_BOTH);
    }

    /**
     * Creates a KeyValue that animates the opacity of a node.
     * @param target the node to animate
     * @param value the value to animate to
     * @return the KeyValue
     */
    public static KeyValue createTranslateXKeyValue(Node target, double value) {
        return new KeyValue(target.translateXProperty(), value, Interpolator.EASE_BOTH);
    }

    /**
     * Creates a KeyValue that animates the opacity of a node.
     * @param target the node to animate
     * @param value the value to animate to
     * @return the KeyValue
     */
    public static KeyValue createTranslateYKeyValue(Node target, double value) {
        return new KeyValue(target.translateYProperty(), value, Interpolator.EASE_BOTH);
    }
}
