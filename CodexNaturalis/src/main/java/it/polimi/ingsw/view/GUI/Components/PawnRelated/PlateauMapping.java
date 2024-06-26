package it.polimi.ingsw.view.GUI.Components.PawnRelated;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Position;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PlateauMapping {
    private static final Map<Integer, Pair<Integer, Integer>> positionCoordinates = new HashMap<>();

    private static final Map<PawnColors, Position> colorOffset = Map.of(
        PawnColors.RED, new Position(50, 50),
        PawnColors.BLUE, new Position(50, -50),
        PawnColors.GREEN, new Position(-50, -50),
        PawnColors.YELLOW, new Position(-50, 50)
    );

    static {
        initPositionCoordinates();
    }

    //center
    private static void initPositionCoordinates() {
        positionCoordinates.put(0, new Pair<>(421, 219));
        positionCoordinates.put(1, new Pair<>(791, 219));
        positionCoordinates.put(2, new Pair<>(1163, 219));
        positionCoordinates.put(3, new Pair<>(1387, 560));
        positionCoordinates.put(4, new Pair<>(975, 560));
        positionCoordinates.put(5, new Pair<>(605, 560));
        positionCoordinates.put(6, new Pair<>(235, 560));
        positionCoordinates.put(7, new Pair<>(235, 900));
        positionCoordinates.put(8, new Pair<>(605, 900));
        positionCoordinates.put(9, new Pair<>(975, 900));
        positionCoordinates.put(10, new Pair<>(1345, 900));
        positionCoordinates.put(11, new Pair<>(1345, 1240));
        positionCoordinates.put(12, new Pair<>(975, 1240));
        positionCoordinates.put(13, new Pair<>(605, 1240));
        positionCoordinates.put(14, new Pair<>(235, 1240));
        positionCoordinates.put(15, new Pair<>(235, 1575));
        positionCoordinates.put(16, new Pair<>(605, 1575));
        positionCoordinates.put(17, new Pair<>(975, 1575));
        positionCoordinates.put(18, new Pair<>(1345, 1575));
        positionCoordinates.put(19, new Pair<>(1345, 1915));
        positionCoordinates.put(20, new Pair<>(788, 2082));
        positionCoordinates.put(21, new Pair<>(235, 1915));
        positionCoordinates.put(22, new Pair<>(235, 2250));
        positionCoordinates.put(23, new Pair<>(235, 2590));
        positionCoordinates.put(24, new Pair<>(450, 2870));
        positionCoordinates.put(25, new Pair<>(788, 2930));
        positionCoordinates.put(26, new Pair<>(1130, 2870));
        positionCoordinates.put(27, new Pair<>(1345, 2590));
        positionCoordinates.put(28, new Pair<>(1345, 2250));
        positionCoordinates.put(29, new Pair<>(788, 2517));
    }

    public static Pair<Integer, Integer> getPositionCoordinates(int position, PawnColors color , boolean isConflicting) {
        Pair<Integer, Integer> coordinates = positionCoordinates.get(position);

        if(isConflicting) {
            Position offset = colorOffset.get(color);
            return new Pair<>(coordinates.getKey() + offset.getX(), coordinates.getValue() + offset.getY());
        }

        return coordinates;
    }
}