package it.polimi.ingsw;

public class Configs {
    public static Boolean debugMode = true;
    public static String gameSavesDir = "./gameSaves/";
    public static long gameSaveExpirationTimeMinutes = 30;
    public static String connectionLabelRMI = "connect";
    public static String CardFolder = "./cards/";
    public static String CardFile = "cards.json";
    public static int rmiPort = 1234;
    public static int socketPort = 12345;
    public static int secondsTimeOut = 3;
    public static int pingPongFrequency = 3;
    public static int timerDurationSeconds = 60;
    public static String goldCardBinFileName = "goldCards.bin";
    public static String objectiveCardBinFileName = "objectiveCards.bin";
    public static String resourceCardBinFileName = "resourceCards.bin";
    public static String startCardBinFileName = "startCards.bin";
}
