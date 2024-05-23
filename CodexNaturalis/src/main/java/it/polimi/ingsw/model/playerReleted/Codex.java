package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;

import java.io.*;
import java.util.*;

public class Codex implements Serializable {
    private int points;
    private final Map<Collectable, Integer> collectables;
    private final Map<Position, Placement> placementHistory;
    private final Frontier frontier;

    /** Constructor of the Codex class
     * initializes the points to 0
     * initializes the collectables to 0 for each collectable
     * initializes the frontier
     * initializes the placement history
     * */
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

    public Codex(Codex other){
        this.points = other.points;
        this.collectables = new HashMap<>(other.collectables);
        this.frontier = new Frontier(other.frontier);
        this.placementHistory = new LinkedHashMap<>(other.placementHistory);
    }
    /** @return points of the related codex*/
    public int getPoints() {
        return this.points;
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
        if (!Arrays.stream(Resource.values()).anyMatch(res -> res == collectable) &&
                !Arrays.stream(WritingMaterial.values()).anyMatch(res -> res == collectable))
            throw new IllegalArgumentException("collectable must be a writing material or a resource");
        if (number < 0)
            throw new IllegalArgumentException("the number of collectables " +
                    "                           in the codex cannot be less than 0");

        this.collectables.put(collectable, number);
    }
    public Frontier getFrontier() {
        return new Frontier(frontier);
    }

    /** @return all the placement history of the codex */
    public ArrayList<Placement> getPlacementHistory(){
        return new ArrayList<>(this.placementHistory.values());
    }

    /** @return the placement at a certain position, null if there is no placement at that position
     * @throws IllegalArgumentException if the position is null
     * @param position is the position of the placement to get
     * */
    public Placement getPlacementAt(Position position){
        if (position == null)
            throw new IllegalArgumentException("position cannot be null");
        Placement p = this.placementHistory.get(position);

        return p == null ? null : new Placement(p);
    }

    /** method to add a placement to the placements history in the codex
     * @param placement the placement to insert in the history
     * @throws IllegalArgumentException if placement is null*/
    private void addPlacement(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");
        placement = new Placement(placement);
        this.placementHistory.put(placement.position(), placement);
    }

    /** method that given a placement calculates the consequences on codex collectables of the placement
     * @param placement requires placement != null, the placement that adds the collectables
     * @throws IllegalArgumentException if placement == null */
    private void calculateConsequencesCollectables(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");

        CardWithCorners card = placement.card();
        // collectables from the corners
        for (CardCorner corner : CardCorner.values()){
            //Adding collectables from the corners
            if(card.isCorner(corner, placement.face())){
                Collectable cornerCollectable = card.getCollectableAt(corner, placement.face());
                if(cornerCollectable != SpecialCollectable.EMPTY)
                    this.collectables.put(cornerCollectable, this.collectables.get(cornerCollectable) + 1);
            }

            //Removing covered collectables
            Placement neighbour = this.getPlacementAt(placement.position().add(corner.getOffset()));
            if(neighbour != null){
                Collectable cornerCollectable = neighbour.card().getCollectableAt(corner.getOpposite(), neighbour.face());
                if(cornerCollectable != SpecialCollectable.EMPTY)
                    this.collectables.put(cornerCollectable, this.collectables.get(cornerCollectable) - 1);
            }
        }

        // collectables from the permanent resources
        for (Collectable c : placement.card().getPermanentResources(placement.face()))
            if(c != SpecialCollectable.EMPTY)
                this.collectables.put(c, this.collectables.get(c) + 1);

    }

    /** Method that, given a placement, calculates the points that the placement gives to the codex. The points are added only if the card is placed on the front
     * @param placement the placement that contains the card of which calculates the points
     * @throws IllegalArgumentException if placement == null */
    private void calculateConsequencesPoints(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");
        if(placement.face() == CardFace.FRONT)
            this.points += placement.card().getPoints(this);
    }

    /** method that given a placement updates the codex Collectables and Points
     * @param placement requires placement != null, the placement added to the codex
     * @throws IllegalArgumentException if placement == null */
    private void updateCodexConsequences(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");
        calculateConsequencesCollectables(placement);
        calculateConsequencesPoints(placement);
    }

    /** method that
     * 1) adds the placement to the placement history if valid
     * 2) update the frontier
     * 3) calculate and updates the consequences given a placement
     * @param placement contains the card to be played
     * @throws IllegalArgumentException if placement == null*/
    public void playCard(Placement placement){
        if (placement == null)
            throw new IllegalArgumentException("placement cannot be null");
        addPlacement(placement);
        this.frontier.updateFrontier(this, placement.position());
        updateCodexConsequences(placement);
    }
    public void pointsFromObjective(ObjectiveCard card){
        this.points += card.getPoints(this);
    }

    @Override
    public String toString(){
        return "{" + "\n" +
                "\"Codex\" : [ " + ",\n" +
                "\"points\" : " + this.points + ",\n" +
                "\"collectables\" : " + this.collectables + ",\n" +
                "\"placementHistory\" : " + this.placementHistory + "\n" +
                "]\n" +
                '}';
    }
}
