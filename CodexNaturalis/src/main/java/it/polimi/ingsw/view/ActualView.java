package it.polimi.ingsw.view;

import java.rmi.RemoteException;

public interface ActualView extends ViewInterface, ControllerHandler{
    void run() throws RemoteException;
}
