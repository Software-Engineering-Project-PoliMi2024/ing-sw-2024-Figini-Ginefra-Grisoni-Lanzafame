package it.polimi.ingsw.model.playerReleted;

import java.io.Serializable;

public class User implements Serializable {
    private final String nickname;
    private Codex userCodex;
    private Hand userHand;

    public User(String nickname){
        this.nickname = nickname;
        this.userCodex = new Codex();
        this.userHand = new Hand();
    }

    /** @return the user nickname*/
    public String getNickname(){
        return this.nickname;
    }
    /** @return the user codex address*/
    public Codex getUserCodex(){
        return this.userCodex;
    }

    public void setUserCodex(Codex codex){
        this.userCodex = codex;
    }
    /** @return the user hand address*/
    public Hand getUserHand(){
        return this.userHand;
    }

    public void setUserHand(Hand hand){
        this.userHand=hand;
    }
}
