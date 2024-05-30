package it.polimi.ingsw;

import java.io.File;

public class OSRelated {
    public static String dataFolderPath = getOSDataFolderPath() + Configs.dataFolderName + File.separator;
    public static String cardFolderDataPath = getOSDataFolderPath() + Configs.dataFolderName + File.separator + Configs.cardDataFolderName + File.separator;
    public static String gameDataFolderPath = getOSDataFolderPath() + Configs.dataFolderName + File.separator + Configs.gameSaveFolderName + File.separator;
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

    public static void checkOrCreateDataFolderServer() {
        File dataFolder = new File(OSRelated.dataFolderPath);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        // Create Cards folder
        File cardsFolder = new File(OSRelated.cardFolderDataPath);
        if (!cardsFolder.exists()) {
            cardsFolder.mkdirs();
        }
        // Create gameSaved folder
        File gameSavedFolder = new File(OSRelated.gameDataFolderPath);
        if (!gameSavedFolder.exists()) {
            gameSavedFolder.mkdirs();
        }
    }

    public static void checkOrCreateDataFolderClient() {
        File dataFolder = new File(OSRelated.dataFolderPath);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        // Create Cards folder
        File cardsFolder = new File(OSRelated.cardFolderDataPath);
        if (!cardsFolder.exists()) {
            cardsFolder.mkdirs();
        }
    }
}
