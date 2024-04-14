package it.polimi.ingsw.model.tableReleted;

import java.io.Serializable;

public class EmptyMatchException extends Exception implements Serializable {
    public EmptyMatchException(String errorMessage){
        super(errorMessage);
    }
}
