package it.polimi.ingsw.utils;

import it.polimi.ingsw.Configs;

import java.io.File;
import java.io.IOException;

public class OSRelated {
    public static String dataFolderPath = getOSDataFolderPath() + Configs.dataFolderName + File.separator;
    public static String cardFolderDataPath = getOSDataFolderPath() + Configs.dataFolderName + File.separator + Configs.cardDataFolderName + File.separator;
    public static String gameDataFolderPath = getOSDataFolderPath() + Configs.dataFolderName + File.separator + Configs.gameSaveFolderName + File.separator;

    /**
     * Get the path of the data folder based on the OS (Windows, macOS, Linux)
     * data folder is the folder where the game data is saved both cards and game saved
     * @return the path of the data folder depending on the OS
     */
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

    /**
     * Check if the data folder exists on the server, if not create it
     * data folder is the folder where the game data is saved both cards and game saved
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    /**
     * Check if the data folder exists on the client, if not create it
     * data folder is the folder where the game data is saved both cards and game saved
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    public static synchronized void clearTerminal() {
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print("\033[H\033[J");
            System.out.flush();
        }
    }
}
