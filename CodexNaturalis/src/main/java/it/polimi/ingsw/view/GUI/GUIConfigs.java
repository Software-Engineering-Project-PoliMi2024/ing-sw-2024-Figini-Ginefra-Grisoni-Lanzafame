package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.Configs;

/**
 * This class contains all the configurations for the GUI
 */
public class GUIConfigs {
    /** The time it takes a part of the logo to get big/small */
    public static int swapAnimationDuration = Configs.debugMode ? 1 : 500;

    /** The delay between each component of the logo */
    public static int swapAnimationDelay = Configs.debugMode ? 1 : 100;

    /** The time the complete logo stays big before collapsing again */
    public static int swapAnimationPause = Configs.debugMode ? 1 : 400;

    /** The width of the cards images */
    public static int cardImageWidth = 993;
    /** The height of the cards images */
    public static int cardImageHeight = 662;

    /** The default width of the cards imageView */
    public static double cardWidth = cardImageWidth * 0.3;
    /** The default height of the cards imageView */
    public static double cardHeight = cardImageHeight * 0.3;

    /** The default width of the codex grid */
    public static double codexGridWith = cardWidth * 0.75;

    /** The default height of the codex grid */
    public static double codexGridHeight = cardHeight * 0.55;

    /** The minimum scale of the codex */
    public static double codexMinScale = 0.4;
    /** The maximum scale of the codex */
    public static double codexMaxScale = 1.5;
    /** The border radius of the cards */
    public static int cardBorderRadius = 40;
    /** The number of milliseconds the user has to click on a card to trigger the holding */
    public static int holdDuration = 300;
    /** The width of the collectables in the CollectedCollectables component */
    public static int collectablesWidth = 50;

    /** The horizontal gap between the decks in the deck Area */
    public static int hDeckGap = 35;
    /** The vertical gap between the cards in each deck */
    public static int vCardGapInDeck = 10;

    /** The duration of the glow animation for the collected collectables when there is an increase/decrease */
    public static int counterGlowDuration = 3000;

    /** The duration of the Add/Remove animation of the cards in milliseconds */
    public static int cardAddRemAnimationDuration = 100;

    /**The width of the corners expressed as a percentage of the
     * minimum between the window width and height */
    public static double decorativeCornersPercentage = 0.2;

    /** The duration of the animation of the flowers for the chat button*/
    public static double flowerAnimationDuration = 2000;

    /** The width of the LogErr popup */
    public static double logErrWidth = 500;
    /** The height of the LogErr popup */
    public static double logErrHeight = 150;

    /** How many milliseconds the Log popup stays on the screen */
    public static int logHoldingDuration = 4000;
}
