package it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.Updater;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.LoggerInterface;

import java.io.Serializable;
import java.util.HashMap;
 import java.util.Map;

public abstract class LoggerAndUpdaterMediator<updaterType extends Updater, DiffType extends Differentiable> implements Serializable{
    protected final Map<String, Pair<updaterType, LoggerInterface>> subscribers = new HashMap<>();

    protected void subscribe(String nickname, updaterType updater, LoggerInterface logger){
        subscribers.put(nickname, new Pair<>(updater, logger));
    }
    protected void unsubscribe(String nickname){
        subscribers.remove(nickname);
    }

}
