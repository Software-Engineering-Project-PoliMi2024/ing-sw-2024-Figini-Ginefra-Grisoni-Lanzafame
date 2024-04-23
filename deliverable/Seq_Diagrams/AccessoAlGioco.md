```mermaid
sequenceDiagram
    title: Accesso al gioco
    actor Pippo

    box black Client
        participant View
        participant SocketClientController
        participant ServerHandler
    end

    box purple Server
        participant ClientHandler
        participant SocketServerController
        participant MultiGame
    end

    View ->> +Pippo : Enter IP and port
    Pippo -->> -View : Submit IP and port
    View ->> + SocketClientController : connectToServer(ip, port)
    activate SocketClientController
    SocketClientController ->> +SocketServerController : ping
    SocketServerController ->> SocketClientController : ack

    SocketServerController ->> ClientHandler : contruct(MultiGame)
    activate ClientHandler
    SocketServerController ->> -ClientHandler : run()
    deactivate ClientHandler

    SocketClientController ->> +ServerHandler : construct(View)
    SocketClientController ->> ServerHandler : run()
    deactivate SocketClientController
    SocketClientController ->> -View : transitionTo(Login)

    View ->> Pippo : Enter nickname
    Pippo -->> View : Submit nickname

    View ->> SocketClientController : login(nickname)

    SocketClientController ->> ServerHandler : sendActionMsg(loginMsg)

    ServerHandler ->> ClientHandler : loginMsg

    ClientHandler ->> SocketServerController : login(nickname)

    SocketServerController ->> MultiGame : isNickUnique(nickname)

    MultiGame -->> SocketServerController : true

    SocketServerController ->> MultiGame : isNickInGame(nickname)

    MultiGame -->> SocketServerController : false

    SocketServerController ->> ClientHandler : sendServerMsg(loginAnswerMsg(OK))

    ClientHandler ->> ServerHandler : loginAnswerMsg(OK)
    ServerHandler ->> SocketClientController : loginAnswerMsg(OK)

    SocketClientController ->> View : transitionTo(GameList)

    SocketServerController ->> ClientHandler : sendServerMsg(gameListNotification(String[] gameList))

    ClientHandler ->> ServerHandler : gameListNotification(String[] gameList)
    ServerHandler ->> SocketClientController : gameListNotification(String[] gameList)
    SocketClientController ->> View : updateGameList(gameList)

    View ->> Pippo : Display Game List

    View ->> Pippo : Enter a game name

    Pippo -->> View : Submit selected game name

    View ->> SocketClientController : joinLobby(gameName)


    SocketClientController ->> ServerHandler : sendActionMsg(joinLobbyMsg(gameName))

    ServerHandler ->> ClientHandler : joinLobbyMsg(gameName)

    ClientHandler ->> SocketServerController : joinLobby(gameName)

    SocketServerController ->> MultiGame : joinLobby(gameName)

    MultiGame -->> SocketServerController : true

    SocketServerController ->> ClientHandler : sendServerMsg(joinLobbyAnswerMsg(OK))

    ClientHandler ->> ServerHandler : joinLobbyAnswerMsg(OK)

    ServerHandler ->> SocketClientController : joinLobbyAnswerMsg(OK)

    SocketClientController ->> View : transitionTo(Lobby)

    SocketServerController ->> ClientHandler : sendServerMsg(lobbyNotification(String[] nicks))

    ClientHandler ->> ServerHandler : lobbyNotification(nicks)
    ServerHandler ->> SocketClientController : lobbyNotification(nicks)
    SocketClientController ->> View : updateLobby(nicks)

    View ->> Pippo : Display Lobby

    SocketServerController ->> MultiGame : isLobbyFull()

    MultiGame -->> SocketServerController : true

    SocketServerController ->> MultiGame : startGame(gameName)

    SocketServerController ->> ClientHandler : sendServerMsg(gameStartedNotification)

    ClientHandler ->> ServerHandler : gameStartedNotification
    ServerHandler ->> SocketClientController : gameStartedNotification
    SocketClientController ->> View : transitionTo(ChooseStartCard))

    View ->> Pippo : Display Game stuff

```

# Sequence Diagram Report: Game Access Flow

## Objective

The sequence diagram illustrates the flow of actions involved in accessing a game, from the initial connection to navigating through game lobbies.

## Actors

- Pippo: The player or user interacting with the game.

## Components

- Client:

  - View: The visual interface where the user interacts.
  - SocketClientController: It's the bridge between the view and the web communation. It has the methods to send data to the server and to react to the server's messages. The latter are inherited from the parent and are the same for both the Socket and RMI implementations.

  - ServerHandler: Manages communication with the server.

- Server:

  - ClientHandler: Handles client connections and requests.
  - SocketServerController: It's the bridge between the web communation and the model. It has the methods to send data to the client and to react to the client's messages. The latter are inherited from the parent and are the same for both the Socket and RMI implementations.

  - MultiGame: Manages multiple game instances and lobbies. There is only one MultiGame instance for the whole server.

## Disclaimer

The diagram has been written with an optimistic approach, assuming that all the actions are successful. The diagram does not include error handling or failure cases.

## Flow:

- Initial Connection:

  - Pippo enters the IP and port to connect.
  - The View forwards this to the SocketClientController, which initiates the connection.
  - SocketClientController pings the SocketServerController for acknowledgment.
  - If successful, the SocketServerController constructs a ClientHandler to handle the connection.

- Login Process:

  - Pippo submits a nickname.
  - The View sends this to the SocketClientController, which then logs in using the provided nickname.
  - Server-side components check if the nickname is unique and not already in use in any ongoing game.
  - If the nickname is unique and not in use, Pippo is logged in successfully and transitions to the game list interface.

- Game Lobby Access:

  - Pippo selects a game from the displayed list.
  - The chosen game name is sent to the SocketClientController, initiating the process to join the lobby.
  - The server-side components ensure that the lobby is available and not full.
  - If the lobby is available, Pippo successfully joins and transitions to the lobby interface.

- Game Start:
  - Once the lobby is full, the game starts.
  - Players receive notifications, and the interface transitions to the game interface.
