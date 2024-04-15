package it.polimi.ingsw.controller;

import it.polimi.ingsw.view.View;

public abstract class ClientImplementation implements Runnable{
    protected final View view;
    protected String nickname;
    protected boolean shallTerminate;

    /**@param nickname the nickname used by the user
     * @param view the view of user choice (TUI / GUI)*/
    public ClientImplementation(String nickname, View view)
    {
        super();
        this.nickname = nickname;
        this.view = view;

    }
    /**
     * @return the reference to the View of this Client
     */
    public View getView() {
        return view;
    }
}
