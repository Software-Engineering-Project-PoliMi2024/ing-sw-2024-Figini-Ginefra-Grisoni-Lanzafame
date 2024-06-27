package it.polimi.ingsw;

/**
 * This class contains configuration constants for the project.
 */
public class Configs {
    /** flag for enabling debug mode. */
    public static Boolean debugMode = false;
    /** Minutes after which the gameSave is considered expired. */
    public static long gameSaveExpirationTimeMinutes = 30;
    /** label to look Up the object used to connect to the server in the RMI registry */
    public static String connectionLabelRMI = "connect";
    /** the path to the folder containing the card Json source file */
    public static String CardResourcesFolderPath = "Cards/";
    /** the name of the card Json source file from which to get the cards information*/
    public static String CardJSONFileName = "cards.json";
    /** the port on the server which runs the RMI protocol */
    public static int rmiPort = 1234;
    /** the port on the server which runs the socket protocol */
    public static int socketPort = 12345;
    /** seconds after which the connection is considered lost */
    public static int secondsTimeOut = 3;
    /** the period between heartBeat messages */
    public static int heartBeatPeriod = 3;
    /** the period after which a winner is declared if there's only one player left */
    public static int lastInGameTimerSeconds = 30;
    /** the points needed to trigger the end game phase */
    public static int pointsToStartGameEnding = 20;
    /** the id used to draw from the actual decks instead of the buffer */
    public static int actualDeckPos = 2;
    /** the number of turns after the ending condition are met before the game ends*/
    public static int numberOfEndingTurns = 2;
    /** the name of the bin file containing the gold card*/
    public static String goldCardBinFileName = "goldCards.bin";
    /** the name of the bin file containing the objective cards*/
    public static String objectiveCardBinFileName = "objectiveCards.bin";
    /** the name of the bin file containing the resource cards*/
    public static String resourceCardBinFileName = "resourceCards.bin";
    /** the name of the bin file containing the start cards*/
    public static String startCardBinFileName = "startCards.bin";
    /** the name of the bin file containing the TUI render of the card using emojis*/
    public static String CardMuseumFileName = "CardMuseum.bin";
    /** the regex used to validate the user nickname*/
    public static String invalidNicknameRegex = "^\\s*$"; // empty string or only spaces
    /** the regex used to validate the lobby name*/
    public static String invalidLobbyNameRegex = "^\\s*$"; // empty string or only spaces
    /** the name of the folder containing the files needed for the project*/
    public static String dataFolderName = "CodexNaturalisPSP49";
    /** the name of the folder containing the bin of the cards */
    public static String cardDataFolderName = "Cards";
    /** the name of the folder to which save the games and from which load the saved games */
    public static String gameSaveFolderName = "GameSaved";
    /** the max point a player can have */
    public static int maxPointsPerPlayer = 29;
    /** the seconds before loading the game saves after the server starts*/
    public static int delayBeforeLoadingGameSaves = 0;
    /** the seconds before deleting the logs on the TUI */
    public static int logDurationTUI_millis = 5000;

    public static void printStackTrace(Exception e) {
        if (debugMode) {
            e.printStackTrace();
        }
    }
}
