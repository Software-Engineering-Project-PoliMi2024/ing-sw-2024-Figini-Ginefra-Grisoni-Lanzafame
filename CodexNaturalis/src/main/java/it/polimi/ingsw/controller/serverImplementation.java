package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.MultiGame;

public abstract class serverImplementation implements Runnable{
    protected final MultiGame games;


    /**
     * The constructor of the class
     * @param games the class that handle all the games
     *              which are running on these Server
     */
    public serverImplementation(MultiGame games) {
        this.games = games;
    }
    public void run() {
    }
}
