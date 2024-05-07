package it.polimi.ingsw.controller2.persistence;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.*;
import java.util.HashSet;

public class PersistenceFactory {
    private final static String _ser = ".ser";

    /**
     * @param game the game to save to file
     */
    public static void save(Game game){
        try{
        FileOutputStream fileOut = new FileOutputStream(
                Configs.gameSavesDir + game.getName()+ _ser);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(game);
        outStream.close();
        fileOut.close();
        }catch (Exception e) {
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
     * @return a list of all the games saved in the gameSaves directory
     */
    public static HashSet<Game> load() {
        HashSet<Game> gameList = new HashSet<>();
        File folder = new File(Configs.gameSavesDir);

        File[] saves = folder.listFiles();

        for(File gameSave : saves){
            Game game = getGameFromFile(gameSave, Configs.gameSavesDir);
            if(game != null)
                gameList.add(game);
        }
        return gameList;
    }

    /**
     * @param game the game to delete
     * @return true if the game was deleted, false otherwise
     */
    public static boolean deleteGameSave(Game game){
        File saveToDelete = new File(Configs.gameSavesDir + game.getName() + _ser);
        return saveToDelete.delete();
    }
}
