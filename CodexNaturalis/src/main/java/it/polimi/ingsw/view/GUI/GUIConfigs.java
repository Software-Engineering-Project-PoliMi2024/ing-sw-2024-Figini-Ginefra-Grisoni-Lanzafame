package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.Configs;

public class GUIConfigs {
    public static int swapAnimationDuration = Configs.debugMode ? 1 : 500;
    public static int swapAnimationDelay = Configs.debugMode ? 1 : 100;
    public static int swapAnimationPause = Configs.debugMode ? 1 : 400;

    public static int cardImageWidth = 993;
    public static int cardImageHeight = 662;

    public static double cardWidth = cardImageWidth * 0.3;
    public static double cardHeight = cardImageHeight * 0.3;

    public static double codexGridWith = cardWidth * 0.75;
    public static double codexGridHeight = cardHeight * 0.55;

    public static double codexMinScale = 0.4;
    public static double codexMaxScale = 1.5;

    public static int cardBorderRadius = 40;

    public static int holdDuration = 300; //in milliseconds

    public static int collectablesWidth = 50;
    public static int hDeckGap = 35;
    public static int vCardGapInDeck = 10;

    public static int counterGlowDuration = 3000;

    public static int cardAddRemAnimationDuration = 100;

    /**The width of the corners expressed as a percentage of the
     * minimum between the window width and height */
    public static double decorativeCornersPercentage = 0.2;

    public static double flowerAnimationDuration = 2000;

    public static double logErrWidth = 500;
    public static double logErrHeight = 150;
}
