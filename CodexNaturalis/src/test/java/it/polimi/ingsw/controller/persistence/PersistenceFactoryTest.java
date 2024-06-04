package it.polimi.ingsw.controller.persistence;

class PersistenceFactoryTest {
/*
    @Test
    void save() {
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        PersistenceFactory.save(reception.createGame(lobby));

        File folder = new File(Configs.gameSavesDir);
        File[] saves = folder.listFiles();

        for(File gameSave : saves) {
            if (gameSave.getName().contains(lobby.getLobbyName())) {
                assert true;
                return;
            }
        }
        assert false;
    }

    @Test
    void load() {
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        PersistenceFactory.save(reception.createGame(lobby));

        Reception reception1 = new Reception();

        Game gameLoadedFromSave = reception1.getGameByName("testGame");
        System.out.println(gameLoadedFromSave);

        assert Objects.equals(gameLoadedFromSave.getName(), "testGame");
        assert gameLoadedFromSave.getGameParty().getUsersList().size() == 3;
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni");
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni1");
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni2");
    }

    @Test
    void deleteGameSave() {
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        Game gameCreated = reception.createGame(lobby);
        PersistenceFactory.save(gameCreated);
        PersistenceFactory.deleteGameSave(gameCreated);

        File file = new File("gameSaves/testGame.ser");
        assert !file.exists();
    }
    */
}