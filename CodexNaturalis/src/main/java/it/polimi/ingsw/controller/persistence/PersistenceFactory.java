package it.polimi.ingsw.controller.persistence;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.utils.logger.LoggerLevel;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ServerLogger;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PersistenceFactory {
    private final String _ser = ".ser";
    private final String dateGameNameSeparator = "--";
    private final DateTimeFormatter windowSucks = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private final String gameDataFolderPath;
    private final ExecutorService fileIOExecutor = Executors.newSingleThreadExecutor();
    private final ServerLogger logger = new ServerLogger(LoggerSources.PERSISTENCE, "");

    public PersistenceFactory(String gameDataFolderPath) {
        this.gameDataFolderPath = gameDataFolderPath;

    }

    /**
     * Save a game in the gameDataFolderPath.
     * If a game with the same name is already saved update the save with the new one
     * and delete the old save
     * logs the result of the save operation
     * @param game the game to save
     */
    public synchronized void save(Game game) {
        fileIOExecutor.submit(()-> {
            File oldSave = latestGameSave(game);
            File newSave = null;
            ObjectOutputStream outStream = null;
            try {
                newSave = new File(gameDataFolderPath + LocalDateTime.now().format(windowSucks) + dateGameNameSeparator + game.getName() + _ser);
                newSave.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(newSave);
                outStream = new ObjectOutputStream(fileOut);
                outStream.writeObject(game);
                outStream.flush();
                outStream.close();
                fileOut.close();
                logger.log(LoggerLevel.INFO, "Game: " + game.getName() + " saved successfully");
            } catch (IOException e) {
                e.printStackTrace();
                //I can't delete the new save if the stream is not closed
                if (outStream != null) {
                    try {
                        outStream.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                delete(newSave);
                newSave = null;
                if (oldSave != null) {
                    logger.log(LoggerLevel.WARNING, "Error while saving the game. Keeping the previous save: " + oldSave.getName());
                } else {
                    logger.log(LoggerLevel.SEVERE, "Error while saving the game. No previous save found");
                }
            }
            if (oldSave != null && newSave != null && !oldSave.getName().equals(newSave.getName())) {
                boolean successfulDeletion = oldSave.delete();
                if (successfulDeletion) {
                    logger.log(LoggerLevel.INFO, "Old game save: " + oldSave.getName() + " deleted successfully");
                } else {
                    logger.log(LoggerLevel.WARNING, "Old game save: " + oldSave.getName() + " could not be deleted");
                }
            }
        });
    }

    /**
     * Load all the game saves in the gameDataFolderPath.
     * If a gameSave is expired, delete it
     * logs the result of the load operation
     * @return a Future linking to the result of the parallel execution of the method;
     * when the method is done, the Future will contain the non-expired loaded games
     */
    public synchronized Future<HashSet<Game>> load() {
        Future<HashSet<Game>> load = fileIOExecutor.submit(()-> {
            HashSet<Game> gameList = new HashSet<>();
            File dataFolder = new File(gameDataFolderPath);

            File[] saves = dataFolder.listFiles();
            if (saves == null) {
                throw new IllegalArgumentException("The gameDataFolderPath is not a valid directory, check the path in the OSRelated class");
            } else if (saves.length == 0) {
                logger.log(LoggerLevel.INFO, "No games saves found");
            } else {
                for (File gameSave : saves) {
                    if (checkTimeIsToDelete(gameSave)) {
                        delete(gameSave); //delete the expired saves
                    } else {
                        String gameName = getGameNameFromFile(gameSave);
                        Game game = latestGame(Arrays.stream(saves).filter(file -> getGameNameFromFile(file).equals(gameName)).toList());
                        saves = dataFolder.listFiles(); //update the list of saves after possible deletion from latestGame
                        if (game != null) {
                            gameList.add(game);
                            logger.log(LoggerLevel.INFO, "Game: " + game.getName() + " loaded successfully");
                        }
                    }
                }
            }
            return gameList;
        });

        return load;
    }

    /**
     * Check if the gameSave file is expired. A file is expired if the time
     * difference between the current time and the timeStamp in the file's name
     * is greater than the expiration time in set in the Configs
     * @param gameSave the file to check
     * @return true if the file is expired, false otherwise
     */
    private boolean checkTimeIsToDelete(File gameSave){
        String date = gameSave.getName().split(dateGameNameSeparator)[0];
        LocalDateTime saveDate = LocalDateTime.parse(date, windowSucks);

        Duration timeDifference = Duration.between(saveDate, LocalDateTime.now());
        return timeDifference.toMinutes() > Configs.gameSaveExpirationTimeMinutes;
    }

    /**
     * Load a game from his saveState. Check if the file is a valid file and if the object in the file is a game
     * @param file the file from which the method gets the game
     * @return the game saved in the file, null if the file is corrupted and subsequently deleted
     */
    private Game getGameFromFile(File file) {
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
            //System.out.println("file path: " + file.getAbsoluteFile());
            logger.log(LoggerLevel.WARNING, "file:" + file.getName() + " corrupted, deleting it");
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
     * If more gameSaves of the same game are found (e.g., there was a problem during deletion),
     * return the latest one and try to delete the others
     * @param game the game of which the method is looking for the save
     * @return the latest gameSave file of the given game if it exists, null otherwise
     */
    private File latestGameSave(Game game) {
        File dataFolder = new File(gameDataFolderPath);
        File[] saves = dataFolder.listFiles();
        Queue<File> savesOfGame = new PriorityQueue<>(gameSaveComparator);
        if (saves != null) {
            for (File gameSave : saves) {
                if (getGameNameFromFile(gameSave).equals(game.getName())) {
                    savesOfGame.add(gameSave);
                }
            }
        }

        if (savesOfGame.isEmpty()) {
            return null;
        } else if (savesOfGame.size() == 1) {
            logger.log(LoggerLevel.INFO, "Game: " + game.getName() + " loaded successfully");
            return savesOfGame.poll();
        } else {
            logger.log(LoggerLevel.WARNING, "Multiple saves of the same game found");
            File latestGameSave = savesOfGame.poll();
            deleteMultipleGameSave(savesOfGame);
            logger.log(LoggerLevel.INFO, "Game: " + game.getName() + " loaded from the latest save");
            return latestGameSave;
        }
    }

    /**
     * Return the latest game from a list of gameSaves of that game
     * @param gamesSave the list of gameSaves of the game that need to be loaded
     * @return the game loaded from the latest gameSave file in the list or null if the list is empty
     */
    private Game latestGame(List<File> gamesSave) {
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
    private void deleteMultipleGameSave(Queue<File> gamesSave) {
        for (File gameSave : gamesSave) {
            delete(gameSave);
        }
    }

    /**
     * Comparator to compare two gameSave files by their date-time in the name
     * File are compared in descending order
     */
    private final Comparator<File> gameSaveComparator = (File1, File2) -> {
        String dateTimeFile1 = File1.getName().split(dateGameNameSeparator)[0];
        String dateTimeFile2 = File2.getName().split(dateGameNameSeparator)[0];

        LocalDateTime dateTime1 = LocalDateTime.parse(dateTimeFile1, windowSucks);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateTimeFile2, windowSucks);

        // Compare the LocalDateTime objects in descending order
        return dateTime2.compareTo(dateTime1);
    };

    /**
     * Delete a gameSave file contained in the gameDataFolderPath
     * that has the same name as the gameName
     * @param gameName the name of the game to delete
     */
    public void delete(String gameName){
        fileIOExecutor.submit(()-> {
            File dataFolder = new File(gameDataFolderPath);
            File[] saves = dataFolder.listFiles();
            File gameToDelete = null;
            if (saves != null) {
                gameToDelete = Arrays.stream(saves).filter(file -> getGameNameFromFile(file).equals(gameName)).findFirst().orElse(null);
            }
            if (gameToDelete != null) {
                delete(gameToDelete);
            } else {
                logger.log(LoggerLevel.WARNING, "No game save found for the game: " + gameName);
            }
        });
    }

    /**
     * Delete a file and log the result of the operation
     * @param file the file to delete
     */
    private void delete(File file) {
        if(!file.delete()) {
            logger.log(LoggerLevel.WARNING, "file:" + file.getName() + " could not be deleted");
        }else{
            logger.log(LoggerLevel.INFO, "file:" + file.getName() + " deleted successfully");
        }
    }

    /**
     * delete all the gameSave files in the gameDataFolderPath
     */
    public void eraseAllSaves() {
        fileIOExecutor.submit(() -> {
            File dataFolder = new File(gameDataFolderPath);
            File[] saves = dataFolder.listFiles();
            if (saves != null) {
                for (File gameSave : saves) {
                    delete(gameSave);
                }
            }
        });
    }

    /**
     * Get the game name from the file name removing the date and the .ser extension
     * @param file the file from which the method gets the game name
     * @return the game name
     */
    private String getGameNameFromFile(File file) {
        return file.getName().split(dateGameNameSeparator)[1].split(_ser)[0];
    }
}
