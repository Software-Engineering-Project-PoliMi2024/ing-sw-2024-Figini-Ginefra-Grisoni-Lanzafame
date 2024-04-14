package it.polimi.ingsw.model.playerReleted;

import java.io.Serializable;

public class cardNotFoundException extends Exception implements Serializable {
    public cardNotFoundException(String errorMessage){
        super(errorMessage);
    }
}

