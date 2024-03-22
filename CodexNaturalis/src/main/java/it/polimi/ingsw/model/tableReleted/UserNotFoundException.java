package it.polimi.ingsw.model.tableReleted;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
