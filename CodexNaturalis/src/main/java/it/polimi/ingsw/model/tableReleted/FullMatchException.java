package it.polimi.ingsw.model.tableReleted;

import java.io.Serializable;

public class FullMatchException extends Exception implements Serializable {
    public FullMatchException(String errorMessage){
        super(errorMessage);
    }
}
