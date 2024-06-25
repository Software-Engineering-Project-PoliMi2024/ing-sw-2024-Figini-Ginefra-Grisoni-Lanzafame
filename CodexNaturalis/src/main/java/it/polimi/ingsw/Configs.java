package it.polimi.ingsw;

public class Configs {
    public static Boolean debugMode = false;
    public static long gameSaveExpirationTimeMinutes = 30;
    public static String connectionLabelRMI = "connect";
    public static String CardResourcesFolderPath = "Cards/";
    public static String CardJSONFileName = "cards.json";
    public static int rmiPort = 1234;
    public static int socketPort = 12345;
    public static int secondsTimeOut = 5;
    public static int pingPongFrequency = 3;
    public static int lastInGameTimerSeconds = 5;
    public static int pointsToStartGameEnding = 20;
    public static int actualDeckPos = 2;
    public static int numberOfEndingTurns = 2;
    public static String goldCardBinFileName = "goldCards.bin";
    public static String objectiveCardBinFileName = "objectiveCards.bin";
    public static String resourceCardBinFileName = "resourceCards.bin";
    public static String startCardBinFileName = "startCards.bin";
    public static String CardMuseumFileName = "CardMuseum.bin";
    public static String invalidNicknameRegex = "^\\s*$";
    public static String invalidLobbyNameRegex = "^\\s*$";
    public static String dataFolderName = "CodexNaturalis";
    public static String cardDataFolderName = "Cards";
    public static String gameSaveFolderName = "GameSaved";
    public static int maxPointsPerPlayer = 29;

    public static void clearTerminal(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
