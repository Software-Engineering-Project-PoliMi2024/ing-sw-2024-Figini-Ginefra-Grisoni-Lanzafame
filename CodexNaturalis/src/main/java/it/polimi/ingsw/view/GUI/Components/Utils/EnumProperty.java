package it.polimi.ingsw.view.GUI.Components.Utils;

import javafx.beans.property.SimpleObjectProperty;

public class EnumProperty<E extends Enum<E>> extends SimpleObjectProperty<E> {
    public EnumProperty() {
        super();
    }

    public EnumProperty(E initialValue) {
        super(initialValue);
    }
}