package it.polimi.ingsw.view.GUI.Components.PawnRelated;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PlateauMapping {
    private static final Map<Integer, Pair<Integer, Integer>> centerCoordinates = new HashMap<>();
    private static final Map<PawnColors, Pair<Integer, Integer>> colorOffsets = new HashMap<>();

    static {
        initCenterCoordinates();
        initColorOffsets();
    }

    private static void initCenterCoordinates() {
        centerCoordinates.put(0, new Pair<>(421, 219));
        centerCoordinates.put(1, new Pair<>(791, 219));
        centerCoordinates.put(2, new Pair<>(1163, 219));
        centerCoordinates.put(3, new Pair<>(1387, 560));
        centerCoordinates.put(4, new Pair<>(975, 560));
        centerCoordinates.put(5, new Pair<>(605, 560));
        centerCoordinates.put(6, new Pair<>(235, 560));
        centerCoordinates.put(7, new Pair<>(235, 900));
        centerCoordinates.put(8, new Pair<>(605, 900));
        centerCoordinates.put(9, new Pair<>(975, 900));
        centerCoordinates.put(10, new Pair<>(1345, 900));
        centerCoordinates.put(11, new Pair<>(1345, 1240));
        centerCoordinates.put(12, new Pair<>(975, 1240));
        centerCoordinates.put(13, new Pair<>(605, 1240));
        centerCoordinates.put(14, new Pair<>(235, 1240));
        centerCoordinates.put(15, new Pair<>(235, 1575));
        centerCoordinates.put(16, new Pair<>(605, 1575));
        centerCoordinates.put(17, new Pair<>(975, 1575));
        centerCoordinates.put(18, new Pair<>(1345, 1575));
        centerCoordinates.put(19, new Pair<>(1345, 1915));
        centerCoordinates.put(20, new Pair<>(788, 2082));
        centerCoordinates.put(21, new Pair<>(235, 1915));
        centerCoordinates.put(22, new Pair<>(235, 2250));
        centerCoordinates.put(23, new Pair<>(235, 2590));
        centerCoordinates.put(24, new Pair<>(450, 2870));
        centerCoordinates.put(25, new Pair<>(788, 2930));
        centerCoordinates.put(26, new Pair<>(1130, 2870));
        centerCoordinates.put(27, new Pair<>(1345, 2590));
        centerCoordinates.put(28, new Pair<>(1345, 2250));
        centerCoordinates.put(29, new Pair<>(788, 2517));
    }

    private static void initColorOffsets() {
        colorOffsets.put(PawnColors.RED, new Pair<>(-50, -50));
        colorOffsets.put(PawnColors.GREEN, new Pair<>(50, -50));
        colorOffsets.put(PawnColors.BLUE, new Pair<>(50, 50));
        colorOffsets.put(PawnColors.YELLOW, new Pair<>(-50, 50));
    }

    public static Pair<Integer, Integer> getCenterCoordinates(int position) {
        return centerCoordinates.get(position);
    }

    public static Pair<Integer, Integer> getColorOffset(PawnColors color) {
        return colorOffsets.get(color);
    }
}