package it.polimi.ingsw.view.GUI.Components.PawnRelated;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PlateauMapping {
    private static final Map<Integer, Map<PawnColors, Pair<Integer, Integer>>> positionCoordinates = new HashMap<>();

    static {
        initPositionCoordinates();
    }

    //center
    /*private static void initPositionCoordinates() {
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
    }*/
    private static void initPositionCoordinates() {
        // Initialize coordinates for each position and each color
        for (int i = 0; i <= 29; i++) {
            positionCoordinates.put(i, new HashMap<>());
        }
        //blue top right
        positionCoordinates.get(0).put(PawnColors.BLUE, new Pair<>(471, 269));
        positionCoordinates.get(1).put(PawnColors.BLUE, new Pair<>(841, 269));
        positionCoordinates.get(2).put(PawnColors.BLUE, new Pair<>(1213, 269));
        positionCoordinates.get(3).put(PawnColors.BLUE, new Pair<>(1437, 610));
        positionCoordinates.get(4).put(PawnColors.BLUE, new Pair<>(1025, 610));
        positionCoordinates.get(5).put(PawnColors.BLUE, new Pair<>(655, 610));
        positionCoordinates.get(6).put(PawnColors.BLUE, new Pair<>(285, 610));
        positionCoordinates.get(7).put(PawnColors.BLUE, new Pair<>(285, 950));
        positionCoordinates.get(8).put(PawnColors.BLUE, new Pair<>(655, 950));
        positionCoordinates.get(9).put(PawnColors.BLUE, new Pair<>(1025, 950));
        positionCoordinates.get(10).put(PawnColors.BLUE, new Pair<>(1395, 950));
        positionCoordinates.get(11).put(PawnColors.BLUE, new Pair<>(1395, 1290));
        positionCoordinates.get(12).put(PawnColors.BLUE, new Pair<>(1025, 1290));
        positionCoordinates.get(13).put(PawnColors.BLUE, new Pair<>(655, 1290));
        positionCoordinates.get(14).put(PawnColors.BLUE, new Pair<>(285, 1290));
        positionCoordinates.get(15).put(PawnColors.BLUE, new Pair<>(285, 1625));
        positionCoordinates.get(16).put(PawnColors.BLUE, new Pair<>(655, 1625));
        positionCoordinates.get(17).put(PawnColors.BLUE, new Pair<>(1025, 1625));
        positionCoordinates.get(18).put(PawnColors.BLUE, new Pair<>(1395, 1625));
        positionCoordinates.get(19).put(PawnColors.BLUE, new Pair<>(1395, 1965));
        positionCoordinates.get(20).put(PawnColors.BLUE, new Pair<>(838, 2132));
        positionCoordinates.get(21).put(PawnColors.BLUE, new Pair<>(285, 1965));
        positionCoordinates.get(22).put(PawnColors.BLUE, new Pair<>(285, 2300));
        positionCoordinates.get(23).put(PawnColors.BLUE, new Pair<>(285, 2640));
        positionCoordinates.get(24).put(PawnColors.BLUE, new Pair<>(500, 2920));
        positionCoordinates.get(25).put(PawnColors.BLUE, new Pair<>(838, 2980));
        positionCoordinates.get(26).put(PawnColors.BLUE, new Pair<>(1180, 2920));
        positionCoordinates.get(27).put(PawnColors.BLUE, new Pair<>(1395, 2640));
        positionCoordinates.get(28).put(PawnColors.BLUE, new Pair<>(1395, 2300));
        positionCoordinates.get(29).put(PawnColors.BLUE, new Pair<>(838, 2567));
        //rosso bottom left
        positionCoordinates.get(0).put(PawnColors.RED, new Pair<>(371, 169));
        positionCoordinates.get(1).put(PawnColors.RED, new Pair<>(741, 169));
        positionCoordinates.get(2).put(PawnColors.RED, new Pair<>(1113, 169));
        positionCoordinates.get(3).put(PawnColors.RED, new Pair<>(1337, 510));
        positionCoordinates.get(4).put(PawnColors.RED, new Pair<>(925, 510));
        positionCoordinates.get(5).put(PawnColors.RED, new Pair<>(555, 510));
        positionCoordinates.get(6).put(PawnColors.RED, new Pair<>(185, 510));
        positionCoordinates.get(7).put(PawnColors.RED, new Pair<>(185, 850));
        positionCoordinates.get(8).put(PawnColors.RED, new Pair<>(555, 850));
        positionCoordinates.get(9).put(PawnColors.RED, new Pair<>(925, 850));
        positionCoordinates.get(10).put(PawnColors.RED, new Pair<>(1295, 850));
        positionCoordinates.get(11).put(PawnColors.RED, new Pair<>(1295, 1190));
        positionCoordinates.get(12).put(PawnColors.RED, new Pair<>(925, 1190));
        positionCoordinates.get(13).put(PawnColors.RED, new Pair<>(555, 1190));
        positionCoordinates.get(14).put(PawnColors.RED, new Pair<>(185, 1190));
        positionCoordinates.get(15).put(PawnColors.RED, new Pair<>(185, 1525));
        positionCoordinates.get(16).put(PawnColors.RED, new Pair<>(555, 1525));
        positionCoordinates.get(17).put(PawnColors.RED, new Pair<>(925, 1525));
        positionCoordinates.get(18).put(PawnColors.RED, new Pair<>(1295, 1525));
        positionCoordinates.get(19).put(PawnColors.RED, new Pair<>(1295, 1865));
        positionCoordinates.get(20).put(PawnColors.RED, new Pair<>(738, 2032));
        positionCoordinates.get(21).put(PawnColors.RED, new Pair<>(185, 1865));
        positionCoordinates.get(22).put(PawnColors.RED, new Pair<>(185, 2200));
        positionCoordinates.get(23).put(PawnColors.RED, new Pair<>(185, 2540));
        positionCoordinates.get(24).put(PawnColors.RED, new Pair<>(400, 2820));
        positionCoordinates.get(25).put(PawnColors.RED, new Pair<>(738, 2880));
        positionCoordinates.get(26).put(PawnColors.RED, new Pair<>(1080, 2820));
        positionCoordinates.get(27).put(PawnColors.RED, new Pair<>(1295, 2540));
        positionCoordinates.get(28).put(PawnColors.RED, new Pair<>(1295, 2200));
        positionCoordinates.get(29).put(PawnColors.RED, new Pair<>(738, 2467));
        //Giallo  Top Left
        positionCoordinates.get(0).put(PawnColors.YELLOW, new Pair<>(371, 269));
        positionCoordinates.get(1).put(PawnColors.YELLOW, new Pair<>(741, 269));
        positionCoordinates.get(2).put(PawnColors.YELLOW, new Pair<>(1113, 269));
        positionCoordinates.get(3).put(PawnColors.YELLOW, new Pair<>(1337, 610));
        positionCoordinates.get(4).put(PawnColors.YELLOW, new Pair<>(925, 610));
        positionCoordinates.get(5).put(PawnColors.YELLOW, new Pair<>(555, 610));
        positionCoordinates.get(6).put(PawnColors.YELLOW, new Pair<>(185, 610));
        positionCoordinates.get(7).put(PawnColors.YELLOW, new Pair<>(185, 950));
        positionCoordinates.get(8).put(PawnColors.YELLOW, new Pair<>(555, 950));
        positionCoordinates.get(9).put(PawnColors.YELLOW, new Pair<>(925, 950));
        positionCoordinates.get(10).put(PawnColors.YELLOW, new Pair<>(1295, 950));
        positionCoordinates.get(11).put(PawnColors.YELLOW, new Pair<>(1295, 1290));
        positionCoordinates.get(12).put(PawnColors.YELLOW, new Pair<>(925, 1290));
        positionCoordinates.get(13).put(PawnColors.YELLOW, new Pair<>(555, 1290));
        positionCoordinates.get(14).put(PawnColors.YELLOW, new Pair<>(185, 1290));
        positionCoordinates.get(15).put(PawnColors.YELLOW, new Pair<>(185, 1625));
        positionCoordinates.get(16).put(PawnColors.YELLOW, new Pair<>(555, 1625));
        positionCoordinates.get(17).put(PawnColors.YELLOW, new Pair<>(925, 1625));
        positionCoordinates.get(18).put(PawnColors.YELLOW, new Pair<>(1295, 1625));
        positionCoordinates.get(19).put(PawnColors.YELLOW, new Pair<>(1295, 1965));
        positionCoordinates.get(20).put(PawnColors.YELLOW, new Pair<>(738, 2132));
        positionCoordinates.get(21).put(PawnColors.YELLOW, new Pair<>(185, 1965));
        positionCoordinates.get(22).put(PawnColors.YELLOW, new Pair<>(185, 2300));
        positionCoordinates.get(23).put(PawnColors.YELLOW, new Pair<>(185, 2640));
        positionCoordinates.get(24).put(PawnColors.YELLOW, new Pair<>(400, 2920));
        positionCoordinates.get(25).put(PawnColors.YELLOW, new Pair<>(738, 2980));
        positionCoordinates.get(26).put(PawnColors.YELLOW, new Pair<>(1080, 2920));
        positionCoordinates.get(27).put(PawnColors.YELLOW, new Pair<>(1295, 2640));
        positionCoordinates.get(28).put(PawnColors.YELLOW, new Pair<>(1295, 2300));
        positionCoordinates.get(29).put(PawnColors.YELLOW, new Pair<>(738, 2567));
        //Verde Bottom Right
        positionCoordinates.get(0).put(PawnColors.GREEN, new Pair<>(471, 169));
        positionCoordinates.get(1).put(PawnColors.GREEN, new Pair<>(841, 169));
        positionCoordinates.get(2).put(PawnColors.GREEN, new Pair<>(1213, 169));
        positionCoordinates.get(3).put(PawnColors.GREEN, new Pair<>(1437, 510));
        positionCoordinates.get(4).put(PawnColors.GREEN, new Pair<>(1025, 510));
        positionCoordinates.get(5).put(PawnColors.GREEN, new Pair<>(655, 510));
        positionCoordinates.get(6).put(PawnColors.GREEN, new Pair<>(285, 510));
        positionCoordinates.get(7).put(PawnColors.GREEN, new Pair<>(285, 850));
        positionCoordinates.get(8).put(PawnColors.GREEN, new Pair<>(655, 850));
        positionCoordinates.get(9).put(PawnColors.GREEN, new Pair<>(1025, 850));
        positionCoordinates.get(10).put(PawnColors.GREEN, new Pair<>(1395, 850));
        positionCoordinates.get(11).put(PawnColors.GREEN, new Pair<>(1395, 1190));
        positionCoordinates.get(12).put(PawnColors.GREEN, new Pair<>(1025, 1190));
        positionCoordinates.get(13).put(PawnColors.GREEN, new Pair<>(655, 1190));
        positionCoordinates.get(14).put(PawnColors.GREEN, new Pair<>(285, 1190));
        positionCoordinates.get(15).put(PawnColors.GREEN, new Pair<>(285, 1525));
        positionCoordinates.get(16).put(PawnColors.GREEN, new Pair<>(655, 1525));
        positionCoordinates.get(17).put(PawnColors.GREEN, new Pair<>(1025, 1525));
        positionCoordinates.get(18).put(PawnColors.GREEN, new Pair<>(1395, 1525));
        positionCoordinates.get(19).put(PawnColors.GREEN, new Pair<>(1395, 1865));
        positionCoordinates.get(20).put(PawnColors.GREEN, new Pair<>(838, 2032));
        positionCoordinates.get(21).put(PawnColors.GREEN, new Pair<>(285, 1865));
        positionCoordinates.get(22).put(PawnColors.GREEN, new Pair<>(285, 2200));
        positionCoordinates.get(23).put(PawnColors.GREEN, new Pair<>(285, 2540));
        positionCoordinates.get(24).put(PawnColors.GREEN, new Pair<>(500, 2820));
        positionCoordinates.get(25).put(PawnColors.GREEN, new Pair<>(838, 2880));
        positionCoordinates.get(26).put(PawnColors.GREEN, new Pair<>(1180, 2820));
        positionCoordinates.get(27).put(PawnColors.GREEN, new Pair<>(1395, 2540));
        positionCoordinates.get(28).put(PawnColors.GREEN, new Pair<>(1395, 2200));
        positionCoordinates.get(29).put(PawnColors.GREEN, new Pair<>(838, 2467));
    }

    public static Pair<Integer, Integer> getPositionCoordinates(int position, PawnColors color) {
        return positionCoordinates.get(position).get(color);
    }
}