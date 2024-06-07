package it.polimi.ingsw.controller.persistence;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PersistenceFactory {
    private static final String _bin = ".bin";
    private static final String dateGameNameSeparator = "--";
    private static final DateTimeFormatter windowSucks = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String gameDataFolderPath = OSRelated.gameDataFolderPath;

    public static void save(Game game) {
        File oldSave = latestGameSave(game);
        File newSave = null;
        ObjectOutputStream outStream = null;
        try {
            newSave= new File(gameDataFolderPath + LocalDateTime.now().format(windowSucks) + dateGameNameSeparator + game.getName() + _bin);
            outStream = new ObjectOutputStream(new FileOutputStream(newSave));
            outStream.writeObject(game);
            outStream.close();
            System.out.println("Game: " + game.getName() + " saved successfully");
            if (oldSave != null) {
                boolean successfulDeletion = oldSave.delete();
                if (successfulDeletion) {
                    System.out.println("Old game save: " + oldSave.getName() + " deleted successfully");
                } else {
                    System.out.println("Old game save: " + oldSave.getName() + " could not be deleted");
                }
            }
        } catch (IOException e) {
            //I can't delete the new save if the stream is not closed
            if(outStream != null){
                try {
                    outStream.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            delete(newSave);
            if (oldSave != null) {
                System.out.println("Error while saving the game. Keeping the previous save: " + oldSave.getName());
            } else {
                System.out.println("Error while saving the game. No previous save found");
            }
            e.printStackTrace();
        }
    }

    /**
     * Load all the game saves in the gameDataFolderPath. If a gameSave is expired, delete it
     * @return a HashSet containing all the non-expired games loaded from the gameDataFolderPath
     */
    public static HashSet<Game> load() {
        HashSet<Game> gameList = new HashSet<>();
        File dataFolder = new File(gameDataFolderPath);

        File[] saves = dataFolder.listFiles();
        if(saves == null){
            throw new IllegalArgumentException("The gameDataFolderPath is not a valid directory, check the path in the OSRelated class");
        }else if (saves.length == 0) {
            System.out.println("No games saves found");
        } else {
            for (File gameSave : saves) {
                if (checkTimeIsToDelete(gameSave)) {
                    delete(gameSave); //delete the expired saves
                } else {
                    String gameName = getGameNameFromFile(gameSave);
                    Game game = latestGame(Arrays.stream(saves).filter(file -> file.getName().contains(gameName)).toList());
                    saves = dataFolder.listFiles(); //update the list of saves after possible deletion from latestGame
                    if (game != null ){
                        gameList.add(game);
                    }
                }
            }
        }
        return gameList;
    }

    /**
     * Check if the gameSave file is expired. A file is expired if the time difference between the current time and the
     * timeStamp in the file's name is greater than the expiration time in set in the Configs
     * @param gameSave the file to check
     * @return true if the file is expired, false otherwise
     */
    private static boolean checkTimeIsToDelete(File gameSave){
        String date = gameSave.getName().split(dateGameNameSeparator)[0];
        LocalDateTime saveDate = LocalDateTime.parse(date, windowSucks);

        Duration timeDifference = Duration.between(saveDate, LocalDateTime.now());
        return timeDifference.toMinutes() > Configs.gameSaveExpirationTimeMinutes;
    }

    /**
     * Load a game from his saveState. Check if the file is a valid file and if the object in the file is a game
     *
     * @param file the file from which the method gets the game
     * @return the game saved in the file, null if the file is corrupted and subsequently deleted
     */
    private static Game getGameFromFile(File file) {
        Game game = null;
        FileInputStream fileIn = null;
        try {
            if (!file.isFile()) {
                //check if the file is not a directory
                throw new IllegalArgumentException("the file given as input is not a valid file");
            }
            fileIn = new FileInputStream(gameDataFolderPath + file.getName());
            ObjectInputStream streamIn = new ObjectInputStream(fileIn);
            Object readObject = streamIn.readObject();
            if (readObject instanceof Game){
                game = (Game) readObject;
            } else {
                throw new IllegalArgumentException("the file given as input is not a valid game file");
            }
            fileIn.close();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("file path: " + file.getAbsoluteFile());
            System.out.println("file:" + file.getName() + " corrupted, deleting it");
            if (fileIn != null) {
                try {
                    fileIn.close();
                    delete(file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return game;
    }

    /**
     * Return the latest gameSave file of a given game if it exists.
     * If more gameSaves of the same game are found (e.g., there was a problem during deletion), return the latest one and try to delete the others
     * @param game the game of which the method is looking for the save
     * @return the latest gameSave file of the given game if it exists, null otherwise
     */
    private static File latestGameSave(Game game) {
        File dataFolder = new File(PersistenceFactory.gameDataFolderPath);
        File[] saves = dataFolder.listFiles();
        Queue<File> savesOfGame = new PriorityQueue<>(gameSaveComparator);
        if (saves != null) {
            for (File gameSave : saves) {
                if (gameSave.getName().contains(game.getName())) {
                    savesOfGame.add(gameSave);
                }
            }
        }

        if (savesOfGame.isEmpty()) {
            return null;
        } else if (savesOfGame.size() == 1) {
            System.out.println("Game: " + game.getName() + " loaded successfully");
            return savesOfGame.poll();
        } else {
            System.out.println("Multiple saves of the same game found");
            File latestGameSave = savesOfGame.poll();
            deleteMultipleGameSave(savesOfGame);
            System.out.println("Game: " + game.getName() + " loaded successfully");
            return latestGameSave;
        }
    }

    /**
     * Return the latest game from a list of gameSaves of that game
     * @param gamesSave the list of gameSaves of the gamen that need to be loaded
     * @return the game loaded from the latest gameSave file in the list or null if the list is empty
     */
    private static Game latestGame(List<File> gamesSave) {
        Queue<File> sortedGameSaves = new PriorityQueue<>(gameSaveComparator);
        sortedGameSaves.addAll(gamesSave);
        File latestGameSave = sortedGameSaves.poll();
        if(!sortedGameSaves.isEmpty()){
            deleteMultipleGameSave(sortedGameSaves);
        }
        return getGameFromFile(latestGameSave);
    }

    /**
     * Delete all the gameSave files in the queue
     * @param gamesSave the queue of gameSave files to delete
     */
    private static void deleteMultipleGameSave(Queue<File> gamesSave) {
        for (File gameSave : gamesSave) {
            delete(gameSave);
        }
    }

    /**
     * Comparator to compare two gameSave files by their date-time in the name
     * File are compared in descending order
     */
    private static final Comparator<File> gameSaveComparator = (File1, File2) -> {
        String dateTimeFile1 = File1.getName().split(dateGameNameSeparator)[0];
        String dateTimeFile2 = File2.getName().split(dateGameNameSeparator)[0];

        LocalDateTime dateTime1 = LocalDateTime.parse(dateTimeFile1, windowSucks);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateTimeFile2, windowSucks);

        // Compare the LocalDateTime objects in descending order
        return dateTime2.compareTo(dateTime1);
    };

    private static void delete(File file) {
        if(!file.delete()) {
            System.out.println("file:" + file.getName() + " could not be deleted");
        }
    }

    public static void eraseAllSaves() {
        File dataFolder = new File(gameDataFolderPath);
        File[] saves = dataFolder.listFiles();
        if (saves != null) {
            for (File gameSave : saves) {
                delete(gameSave);
            }
        }
    }

    private static String getGameNameFromFile(File file) {
        return file.getName().split(dateGameNameSeparator)[1].split(_bin)[0];
    }
}
