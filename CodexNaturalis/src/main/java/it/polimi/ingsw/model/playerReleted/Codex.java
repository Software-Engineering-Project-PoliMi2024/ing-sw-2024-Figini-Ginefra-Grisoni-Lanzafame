package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.Collectable;
import it.polimi.ingsw.model.cardReleted.Resource;
import it.polimi.ingsw.model.cardReleted.WritingMaterial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Codex {
    private int points;
    private final HashMap<Collectable, Integer> collectables;
    private final LinkedHashMap<Position, Placement> placementHistory;
    private final Frontier frontier;


    public Codex(){
        this.points = 0;

        this.collectables = new HashMap<>();
        for (Collectable c : Resource.values())
            this.collectables.put(c, 0);
        for (Collectable c : WritingMaterial.values())
            this.collectables.put(c, 0);

        this.frontier = new Frontier();

        this.placementHistory = new LinkedHashMap<>();
    }
    /** @return points of the related codex*/
    public int getPoints() {
        return this.points;
    }
    /** set the points related to the codex
     * @param p the points value to set */
    public void setPoints(int p){
        this.points = p;
    }

    /** returns the map describing for each collectable (writing material or resource)
     * the amount present in the codex
     * @return th map of collectables */
    public Map<Collectable, Integer> getEarnedCollectables(){
        return this.collectables;
    }

    /** update the hash with the current writing material or resources contained in the codex
     * @param collectable determines what collectable's value to update
     * @throws IllegalArgumentException if the collectable is not int ones present in the key
     * @param number define the amount of that type of collectable present in the codex
     * @throws IllegalArgumentException if tries to set a collectable to less than 0
     * */
    public void setEarnedCollectables(Collectable collectable, int number){
        if (Arrays.stream(Resource.values()).anyMatch(res -> res == collectable) ||
                Arrays.stream(WritingMaterial.values()).anyMatch(res -> res == collectable))
            throw new IllegalArgumentException("collectable must be a writing material or a resource");
        if (number < 0)
            throw new IllegalArgumentException("the number of collectables in the codex cannot be less than 0");

        this.collectables.put(collectable, number);
    }

    // Should return null if there is no placement at the given position
    /** @return the placement at a certain position
     * @throws IllegalArgumentException if the position is null
     * @param position is the position of the placement to get
     * */
    public Placement getPlacementAt(Position position){
        if (position == null)
            throw new IllegalArgumentException("position cannot be null");
        return this.placementHistory.get(position);
    }

    /** method to add a placement to the placements history in the codex
     * @param placement the placement to insert in the history
     * @throws IllegalArgumentException if placement is null*/
    private void addPlacement(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");
        this.placementHistory.put(placement.position(), placement);
    }

    /** requires placement != null,
     * */
    private void calculateConsequencesCollectables(Placement placement){

    }

    private void calculateConsequencesPoints(Placement placement){

    }

    /** method that given a placement updates the codex Collectables and Points
     * @
     * */
    private void updateCodex(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");

    }

    /** method that
     * 1) adds the placement to the placement history if valid
     * 2) update the frontier
     * 3) calculate and updates the consequences given a placement*/
    public void playCard(Placement placement){

    }

}
