package it.polimi.ingsw.model.tableReleted;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.playerReleted.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class GameTest {
    private Game game;
    User user1;
    User user2;
    User user3;
    User user4;

    @BeforeEach
    public void setUp() {
        game = new Game("Yolo", 4);
        user1 = new User("a");
        user2 = new User("b");
        user3 = new User("c");
        user4 = new User("d");
    }

    @Test
    public void testAddUserAndCheckNewCurrentPlayer() throws FullMatchException{
        game.getGameParty().addUser(user1);
        assertEquals(1, game.getGameParty().getUsersList().size());
        assertEquals(user1, game.getGameParty().getCurrentPlayer());
        game.getGameParty().addUser(user2);
        assertEquals(user1, game.getGameParty().getCurrentPlayer()); //Checking if adding player doesn't change currentPlayer
    }

    //Testing the FullMatchException exception
    @Test
    public void testAddUserToFullGame() {
        assertThrows(FullMatchException.class, () -> {
            game.getGameParty().addUser(user1);
            game.getGameParty().addUser(user2);
            game.getGameParty().addUser(user3);
            game.getGameParty().addUser(user4);
            game.getGameParty().addUser(new User("e"));
        });
    }

    @Test
    public void removeUserAndCheckNewCurrentPlayer() throws FullMatchException, UserNotFoundException, EmptyMatchException {
        game.getGameParty().addUser(user1);
        game.getGameParty().addUser(user2);
        game.getGameParty().removeUser(user1);
        assertEquals(1, game.getGameParty().getUsersList().size());
        assertEquals(user2, game.getGameParty().getCurrentPlayer());
    }

    @Test
    public void testRemoveUserNotFound(){
        assertThrows(UserNotFoundException.class, () -> {
           game.getGameParty().removeUser(user1);
        });
    }

    @Test
    public void testRemoveUserToEmptyMatch(){
        assertThrows(EmptyMatchException.class, () -> {
            game.getGameParty().addUser(user1);
            game.getGameParty().removeUser(user1);
        });
    }

    @Test
    public void testNextPlayer() throws EmptyMatchException, FullMatchException {
        game.getGameParty().addUser(user1);
        assertEquals(user1, game.getGameParty().getCurrentPlayer());
        game.getGameParty().addUser(user2);
        game.getGameParty().nextPlayer();
        assertEquals(user2, game.getGameParty().getCurrentPlayer()); //checking new CurrentPlayer
        game.getGameParty().addUser(user3);
        game.getGameParty().addUser(user4);
        game.getGameParty().nextPlayer();
        game.getGameParty().nextPlayer();
        game.getGameParty().nextPlayer();
        assertEquals(user1, game.getGameParty().getCurrentPlayer()); //checking if the "fifth" nextPlayer is actually the first
    }
}
