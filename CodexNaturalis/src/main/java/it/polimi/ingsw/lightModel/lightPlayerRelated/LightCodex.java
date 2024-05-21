package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.designPatterns.Observed;
import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.game.FrontierDiff;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.*;

//a
public class LightCodex implements Differentiable, Observed{
    private final List<Observer> observers = new LinkedList<>();
    private int points;
    private Map<Collectable, Integer> collectables;
    private final Map<Position, LightPlacement> placementHistory;
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
    public Map<Position, LightPlacement> getPlacementHistory() {
        return new HashMap<>(placementHistory);
    }
    /**
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
     * @param addFrontier the frontier changes to be added to the related codex
     * @param rmvFrontier the frontier changes to be removed from the related codex
     */
    public void difFrontier(List<Position> addFrontier, List<Position> rmvFrontier){
        new FrontierDiff(addFrontier, rmvFrontier).apply(this.frontier);
    }

    /**
     * @param addCollectables the change in collectables to be added to the related codex
     * @param removeCollectables the change in collectables to be removed from the related codex
     */
    public void difCollectables(Map<Collectable,Integer> addCollectables, Map<Collectable,Integer> removeCollectables) {
        for (Collectable c : addCollectables.keySet())
            this.collectables.put(c, this.collectables.get(c) + addCollectables.get(c));
        for (Collectable c : removeCollectables.keySet())
            this.collectables.put(c, this.collectables.get(c) - removeCollectables.get(c));
    }

    public void setPoints(int points) {

        this.points = points;
        this.notifyObservers();

    }

    public void setFrontier(LightFrontier frontier) {
        this.frontier = new LightFrontier(frontier);
        this.notifyObservers();
    }

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
