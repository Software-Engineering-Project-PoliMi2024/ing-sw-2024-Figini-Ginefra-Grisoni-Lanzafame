package it.polimi.ingsw.utils;

import it.polimi.ingsw.Configs;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CardChecks {
    public static BiFunction<Integer, Integer, Boolean> objectiveCardsCheck = (Integer frontId, Integer backId) ->{
        return backId == 87 && frontId >= 87 && frontId <= 102;
    };

    public static BiFunction<Integer, Integer, Boolean> resourceCardCheck = (Integer frontId, Integer backId) ->{
        return (backId == 1 || backId == 11 || backId == 21 || backId == 31) && frontId >= 1 && frontId <= 40;
    };

    public static BiFunction<Integer, Integer, Boolean> goldCardCheck = (Integer frontId, Integer backId) ->{
        return (backId == 41 || backId == 51 || backId == 61 || backId == 71) && frontId >= 41 && frontId <= 80;
    };

    public static BiFunction<Integer, Integer, Boolean> startCardCheck = (Integer frontId, Integer backId) ->{
        return backId >= 81 && backId <= 86 && frontId >= 81 && frontId <= 86;
    };

    public static Function<Integer, Boolean> validDeckPosition = (Integer position) ->{
        return position >= 0 && position <= Configs.actualDeckPos;
    };
}
