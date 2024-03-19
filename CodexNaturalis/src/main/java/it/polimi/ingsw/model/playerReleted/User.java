package it.polimi.ingsw.model.playerReleted;

public class User {
    private final String nickname;
    private final Codex userCodex;
    private final Frontier userFrontier;
    private final Hand userHand;

    public User(String nickname){
        this.nickname = nickname;
        this.userCodex = new Codex();
        this.userFrontier = new Frontier();
        this.userHand = new Hand();
    }

    public String getNickname(){
        return this.nickname;
    }
    public Codex getUserCodex(){
        return this.userCodex;
    }
    public Frontier getUserFrontier(){
        return this.userFrontier;
    }
    public Hand getUserHand(){
        return this.userHand;
    }
}
