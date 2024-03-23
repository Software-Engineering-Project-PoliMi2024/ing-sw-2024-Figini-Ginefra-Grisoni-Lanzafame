package it.polimi.ingsw.model.playerReleted;

public class toManyCardException extends Exception{
    public toManyCardException(String errorMessage){
        super(errorMessage);
    }
}
