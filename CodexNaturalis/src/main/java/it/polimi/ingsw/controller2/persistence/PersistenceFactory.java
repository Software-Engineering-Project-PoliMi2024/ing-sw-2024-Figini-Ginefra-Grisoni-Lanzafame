package it.polimi.ingsw.controller2.persistence;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class PersistenceFactory {
    private final static String _ser = ".ser";
    private final static String dateGameNameSeparator = "--";

    /**
     * @param game the game to save to file
     */
    public static void save(Game game){
        gameSaveDirectoryCheckAndCreate();
        PersistenceFactory.deleteGameSave(game);
        try{
        FileOutputStream fileOut = new FileOutputStream(
                Configs.gameSavesDir + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + dateGameNameSeparator + game.getName() + _ser);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(game);
        outStream.close();
        fileOut.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gameSaveDirectoryCheckAndCreate(){
        Path folderPath = Paths.get(Configs.gameSavesDir);
        if(!folderPath.toFile().exists())
            try {
                Files.createDirectories(folderPath);
            }catch (IOException e){
                e.printStackTrace();
            }
    }

    /**
     * @param file the file from which get the game
     * @param directory the directory in which the file is
     * @return the game saved in the file, null if the file is corrupted
     */
    private static Game getGameFromFile(File file, String directory){
        Game game = null;
        try{
            if(!file.isFile())
                throw new FileNotFoundException("the file given as input is not a valid file");

            FileInputStream fileIn = new FileInputStream(directory + file.getName());
            ObjectInputStream streamIn = new ObjectInputStream(fileIn);
            Object readObject = streamIn.readObject();
            if(readObject instanceof Game)
                game = (Game) readObject;
            else
                throw new IOException("the file given as input is not a valid game file");
        }catch (Exception e){
            System.out.println("file:" + file.getName() + " corrupted, deleting it");
            file.delete();
            e.printStackTrace();
        }
        return game;
    }

    /**
     * @return a set of all the games saved in the gameSaves directory
     */
    public static HashSet<Game> load() {
        gameSaveDirectoryCheckAndCreate();
        HashSet<Game> gameList = new HashSet<>();
        File folder = new File(Configs.gameSavesDir);

        File[] saves = folder.listFiles();

        for(File gameSave : saves) {
            if (checkTimeIsToDelete(gameSave.getName())) {
                gameSave.delete();
            } else {
                Game game = getGameFromFile(gameSave, Configs.gameSavesDir);
                if (game != null)
                    gameList.add(game);
            }
        }
        return gameList;
    }

    public static boolean checkTimeIsToDelete(String saveName){
        String date = saveName.substring(0, saveName.indexOf(dateGameNameSeparator));
        LocalDateTime saveDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

        Duration timeDifference = Duration.between(saveDate, LocalDateTime.now());
        return timeDifference.toMinutes() > Configs.gameSaveExpirationTimeMinutes;
    }

    /**
     * @param game the game to delete
     * @return true if the game was deleted, false otherwise
     */
    public static boolean deleteGameSave(Game game){
        gameSaveDirectoryCheckAndCreate();
        File folder = new File(Configs.gameSavesDir);
        File[] saves = folder.listFiles();
        for(File gameSave : saves) {
            if (gameSave.getName().contains(game.getName())) {
                return gameSave.delete();
            }
        }
        return false;
    }
}
