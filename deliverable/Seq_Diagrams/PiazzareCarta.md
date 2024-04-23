```mermaid
sequenceDiagram
    title: Giocare una 🃏 dalla propria 🤚
    actor Pluto

    box black Client
        participant View
        participant SocketClientController
        participant ServerHandler
    end

    box rgb(4, 43, 32) Server
        participant ClientHandler
        participant SocketServerController
        participant MultiGame
    end

    View ->> Pluto : display Commands
    Pluto -->> View : placeCard

    View ->> Pluto : Quale carta?
    Pluto -->> View : 2

    View ->> Pluto : In che verso?
    Pluto -->> View : 0

    View ->> Pluto : Donde?
    Pluto -->> View : 15

    View ->> SocketClientController : placeCard(2, 0, 15)

    SocketClientController ->> ServerHandler : sendActionMsg(placeCardMsg(2, 0, 15))

    ServerHandler ->> ClientHandler : placeCardMsg(2, 0, 15)

    ClientHandler ->> SocketServerController : placeCard(2, 0, 15)

    SocketServerController ->> MultiGame : placeCard(user, 2, 0, 15)

    MultiGame -->> SocketServerController : true

    SocketServerController ->> ClientHandler : sendServerMsg(placeCardAnswerMsg(OK))

    ClientHandler ->> ServerHandler : placeCardAnswerMsg(OK)

    ServerHandler ->> SocketClientController : placeCardAswer(OK)

    SocketClientController ->> View : transitionTo(IDLE)

# Introduction:
The sequence diagram illustrates the process of a user drawing a card from a deck. The system involves users interacting through a client interface and processing of actions within a server environment.

# Actors:

Topolino: the user initiates the card drawing process through interactions with the client interface.

# Components:
- Client:

  - View: The visual interface where the user interacts.
  - SocketClientController: It's the bridge between the view and the web communation. It has the methods to send data to the server and to react to the server's messages. The latter are inherited from the parent and are the same for both the Socket and RMI implementations.

  - ServerHandler: Manages communication with the server.

- Server:

  - ClientHandler: Handles client connections and requests.
  - SocketServerController: It's the bridge between the web communation and the model. It has the methods to send data to the client and to react to the client's messages. The latter are inherited from the parent and are the same for both the Socket and RMI implementations.

  - MultiGame: Manages multiple game instances and lobbies. There is only one MultiGame instance for the whole server.

# Flow

    Client Setup:
        The client (represented by the actor "Pippo") enters the IP and port to connect to the server.
        The client submits the IP and port to the view.
        The view sends a connection request to the SocketClientController, which then pings the server to establish the connection.

    Login Process:
        The client enters a nickname and submits it.
        The view sends the nickname to the SocketClientController, which initiates a login request to the server.
        The server checks if the nickname is unique and not already in the game.
        If the nickname is valid, the client transitions to the game list view.

    Joining a Lobby:
        The client selects a game from the list and submits it.
        The view sends a request to join the lobby for the selected game.
        The server processes the request, checks if the lobby is full, and if not, adds the client to the lobby.
        Once the lobby is full, the server starts the game.

    Game Start:
        Upon starting the game, relevant notifications are sent to all clients.
        Clients transition to the game interface to start playing.

The diagram effectively depicts the communication flow between the client-side components (View, SocketClientController) and server-side components (ServerHandler, ClientHandler, MultiGame) during the access and setup of the game.
```
