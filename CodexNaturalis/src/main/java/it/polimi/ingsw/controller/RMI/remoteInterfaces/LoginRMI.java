package it.polimi.ingsw.controller.RMI.remoteInterfaces;

import it.polimi.ingsw.model.MultiGame;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class LoginRMI implements Remote, Serializable {
    /**@param nickname the nickname prompted by the user
     * @param games all the games and users active
     * @return true if the nickname isn't already taken, false otherwise*/
    private static Boolean isFree(String nickname, MultiGame games) {
        if (games.getUsernames().contains(nickname)) {
            return false;
        }
        return true;
    }
    /**@param nickname the nickname prompted by the user
     * @param games all the games and users active
     * @return true if the login has been successful, false otherwise*/
    public static Boolean login(String nickname, MultiGame games) throws RemoteException{
        //System.out.println("User " + nickname + " is trying to log in");
        if (isFree(nickname, games)) {
            games.addUser(nickname);
            //System.out.println("User " + nickname + " logged in");
            return true;
        }else{
            //System.out.println("! User " + nickname + " already exists !");
            return false;
        }
    }
}
