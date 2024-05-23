package it.polimi.ingsw.controller3.mediators;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.Updater;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.LoggerInterface;
import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Mediator <updaterType extends Updater, DiffType extends Differentiable> {
    protected final Map<String, Pair<updaterType, LoggerInterface>> subscribers = new HashMap<>();

    public void subscribe(String nickname, updaterType updater, LoggerInterface logger){
        subscribers.put(nickname, new Pair<>(updater, logger));
    }
    public void unsubscribe(String nickname){
        subscribers.remove(nickname);
    }

}
