package it.polimi.ingsw;

import java.io.File;

import static java.lang.System.getenv;

public class Configs {
    public static Boolean debugMode = true;
    public static String gameSavesDir = "./gameSaves/";
    public static long gameSaveExpirationTimeMinutes = 30;
    public static String connectionLabelRMI = "connect";
    public static String CardFolder = "src/main/resources/Cards/";
    public static String CardFile = "cards.json";
    public static int rmiPort = 1234;
    public static int socketPort = 12345;
    public static int secondsTimeOut = 3;
    public static int pingPongFrequency = 3;
    public static int timerDurationSeconds = 60;
    public static int pointsToStartGameEnding = 20;
    public static int actualDeckPos = 2;
    public static String goldCardBinFileName = "goldCards.bin";
    public static String objectiveCardBinFileName = "objectiveCards.bin";
    public static String resourceCardBinFileName = "resourceCards.bin";
    public static String startCardBinFileName = "startCards.bin";
    public static String invalidNicknameRegex = "^\\s*$";
    public static String invalidLobbyNameRegex = "^\\s*$";
    public static String dataFolderName = "CodexNaturalis";
    public static String cardDataFolderName = "Cards";
    public static String gameSaveFolderName = "GameSaved";
    public static String gameDataFolderPath = getOSDataFolderPath() + dataFolderName + File.separator;
    private static String getOSDataFolderPath(){
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            return System.getenv("APPDATA") + File.separator;
        } else if (OS.contains("MAC")) {
            //well-known hardcoded paths, used in the appDirs library
            return System.getProperty("user.home") + "/Library/Application Support/";
        } else {
            //In a Unix system, data will be saved in the home directory as a hidden folder
            return System.getProperty("user.home") + File.separator + ".";
        }
    }
}
