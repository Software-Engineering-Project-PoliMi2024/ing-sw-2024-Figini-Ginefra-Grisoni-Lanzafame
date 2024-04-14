package it.polimi.ingsw.model.playerReleted;

import java.io.Serializable;

public class toManyCardException extends Exception implements Serializable {
    public toManyCardException(String errorMessage){
        super(errorMessage);
    }
}
