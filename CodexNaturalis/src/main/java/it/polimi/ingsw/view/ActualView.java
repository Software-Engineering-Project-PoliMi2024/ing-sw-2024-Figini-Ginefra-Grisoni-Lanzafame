package it.polimi.ingsw.view;

import it.polimi.ingsw.controller2.ControllerInterface;

import java.rmi.RemoteException;

public interface ActualView extends ViewInterface, ControllerHandler{
    void run() throws RemoteException;
}
