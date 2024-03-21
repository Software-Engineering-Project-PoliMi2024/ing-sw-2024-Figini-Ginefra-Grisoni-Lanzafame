package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.Collectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Codex {
    Map<Collectable, Integer> collectables;
    int points = 0;
    List<Placement> placements;

    public Codex(){
        this.collectables = null;
        this.placements = null;
    }

    public int getPoints() {
        return this.points;
    }
    public void setPoints(int p){
    }

    public Map<Collectable, Integer> getEarnedCollectables(){
        return this.collectables;
    }
    public void setEarnedCollectables(Collectable collectable, int number){

    }

    // Should return null if there is no placement at the given position
    public Placement getPlacementAt(Position position){
        return null;
    }
    public void addPlacement(Placement placement){

    }

}
