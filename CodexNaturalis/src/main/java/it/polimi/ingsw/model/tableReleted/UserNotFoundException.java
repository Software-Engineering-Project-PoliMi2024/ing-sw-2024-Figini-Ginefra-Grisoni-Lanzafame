package it.polimi.ingsw.model.tableReleted;

import java.io.Serializable;

public class UserNotFoundException extends Exception implements Serializable {
    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
