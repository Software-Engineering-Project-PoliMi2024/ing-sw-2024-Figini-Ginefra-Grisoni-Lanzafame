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
        game = new Game(4);
        user1 = new User("a");
        user2 = new User("b");
        user3 = new User("c");
        user4 = new User("d");
    }

    @Test
    public void testAddUserAndCheckNewCurrentPlayer() throws FullMatchException{
        game.addUser(user1);
        assertEquals(1, game.getUsersList().size());
        assertEquals(user1, game.getCurrentPlayer());
        game.addUser(user2);
        assertEquals(user1, game.getCurrentPlayer()); //Checking if adding player doesn't change currentPlayer
    }

    //Testing the FullMatchException exception
    @Test
    public void testAddUserToFullGame() {
        assertThrows(FullMatchException.class, () -> {
            game.addUser(user1);
            game.addUser(user2);
            game.addUser(user3);
            game.addUser(user4);
            game.addUser(new User("e"));
        });
    }

    @Test
    public void removeUserAndCheckNewCurrentPlayer() throws FullMatchException, UserNotFoundException, EmptyMatchException {
        game.addUser(user1);
        game.addUser(user2);
        game.removeUser(user1);
        assertEquals(1, game.getUsersList().size());
        assertEquals(user2, game.getCurrentPlayer());
    }

    @Test
    public void testRemoveUserNotFound(){
        assertThrows(UserNotFoundException.class, () -> {
           game.removeUser(user1);
        });
    }

    @Test
    public void testRemoveUserToEmptyMatch(){
        assertThrows(EmptyMatchException.class, () -> {
            game.addUser(user1);
            game.removeUser(user1);
        });
    }

    @Test
    public void testNextPlayer() throws EmptyMatchException, FullMatchException {
        game.addUser(user1);
        assertEquals(user1, game.getCurrentPlayer());
        game.addUser(user2);
        game.nextPlayer();
        assertEquals(user2, game.getCurrentPlayer()); //checking new CurrentPlayer
        game.addUser(user3);
        game.addUser(user4);
        game.nextPlayer();
        game.nextPlayer();
        game.nextPlayer();
        assertEquals(user1, game.getCurrentPlayer()); //checking if the "fifth" nextPlayer is actually the first
    }
}
