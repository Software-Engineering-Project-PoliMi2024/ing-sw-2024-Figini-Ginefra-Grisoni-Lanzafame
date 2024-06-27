package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.FrontierDiff;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.*;

/**
 * This class represents the codex in the light model.
 * It contains the points, the collectables, the frontier and the placement history of the player who owns it.
 */
public class LightCodex implements Differentiable, Observed{
    /** The list of all the observers of the codex */
    private final List<Observer> observers = new LinkedList<>();
    /** The number of points "placed" in the codex via the goldCard or resources card. */
    private int points;
    /** The collectables that the player has placed */
    private Map<Collectable, Integer> collectables;
    /** The history of each placement on this codex. Each placement is associated with a position. */
    private final Map<Position, LightPlacement> placementHistory;
    /** The frontier of the codex */
    private LightFrontier frontier;

    /** Constructor of the light codex class
     * initializes the points to 0
     * initializes the collectables to 0 for each collectable
     * initializes the frontier
     * initializes the placement history
     * */
    public LightCodex(){
        this.points = 0;
        this.collectables = new HashMap<>();
        for (Collectable c : Resource.values())
            this.collectables.put(c, 0);
        for (Collectable c : WritingMaterial.values())
            this.collectables.put(c, 0);

        this.frontier = new LightFrontier(new ArrayList<Position>());

        this.placementHistory = new LinkedHashMap<>();
    }

    /**
     * @param points the points of the related codex
     * @param collectables the collectables of the related codex
     * @param frontier the frontier of the related codex
     * @param placementHistory the placement history of the related codex
     */
    public LightCodex(int points, Map<Collectable, Integer> collectables, LightFrontier frontier, Map<Position, LightPlacement> placementHistory){
        this.points = points;
        this.collectables = collectables;
        this.frontier = frontier;
        this.placementHistory = placementHistory;
    }
    /** @return points of the related codex*/
    public int getPoints() { return this.points;}

    /**
     * @return the collectables of the related codex
     */
    public Map<Collectable, Integer> getEarnedCollectables(){
        return new HashMap<>(this.collectables);
    }
    /**
     * @return the frontier of the related codex
     */
    public LightFrontier getFrontier() {
        return new LightFrontier(frontier);
    }
    /**
     * @return the placement history of the related codex
     */
    public List<LightPlacement> getPlacementHistory() {
        return new ArrayList<>(this.placementHistory.values());
    }
    /**
     * For each LightPlacement in placementDiff, add it to the placement history
     * At the end of the update, notify the observers
     * @param placementDiff the placements to be added to the placement history
     */
    public void addPlacement(List<LightPlacement> placementDiff){
        for (LightPlacement p : placementDiff) {
            p = new LightPlacement(p);
            this.placementHistory.put(p.position(), p);
        }
        this.notifyObservers();
    }

    /**
     * Set the new points of the codex and notify the observers
     * @param points the new value of the points
     */
    public void setPoints(int points) {
        this.points = points;
        this.notifyObservers();
    }

    /**
     * Set the new frontier of the codex and notify the observers
     * @param frontier the new frontier to be added
     */
    public void setFrontier(LightFrontier frontier) {
        this.frontier = new LightFrontier(frontier);
        this.notifyObservers();
    }

    /**
     * Update the collectable map with the new value for each collectable.
     * At the end of the update, notify the observers
     * @param collectables the map containing the updated value for the collectables
     */
    public void setCollectables(Map<Collectable, Integer> collectables) {
        this.collectables = new HashMap<>(collectables);
        this.notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        this.observers.add(observer);
        observer.update();
    }

    @Override
    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : this.observers)
            o.update();
    }
}
