package it.polimi.ingsw.view.GUI.Components.Utils;

import javafx.beans.property.SimpleObjectProperty;

/**
 * This class represents a property that can be used to bind an Enum to a JavaFX component.
 * It is used to bind the component to the State of the GUI
 * @param <E>
 */
public class EnumProperty<E extends Enum<E>> extends SimpleObjectProperty<E> {
    public EnumProperty() {
        super();
    }
}