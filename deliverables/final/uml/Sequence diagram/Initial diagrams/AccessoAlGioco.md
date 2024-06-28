```mermaid
stateDiagram-v2
  View --> VirtualControllerSocket
  VirtualControllerSocket --> ServerHandler
  ServerHandler --> ClientHandler
  ServerHandler --> VirtualControllerSocket
    ServerHandler --> View

  ClientHandler --> Controller
  Controller --> MultiGame
  MultiGame --> Controller
  Controller --> VirtualViewSocket
  VirtualViewSocket --> ClientHandler
  MultiGame --> VirtualViewSocket

```

```mermaid
sequenceDiagram
    title: Accesso al gioco
    actor Pippo

    box black Client
        participant View
        participant VirtualControllerSocket
        participant ServerHandler
    end

    box purple Server
        participant ClientHandler
        participant VirtualViewSocket
        participant Controller
        participant MultiGame
    end

    View ->> +Pippo : Enter a Command
    Pippo -->> View : Submit Connect
    View ->> Pippo : Enter IP and port
    Pippo -->> -View : Submit IP and port
    View ->> +VirtualControllerSocket : connect(ip, port)
    VirtualControllerSocket ->> -ServerHandler : connectToServer(ip, port)
    activate ServerHandler
    ServerHandler ->> +VirtualViewSocket : ping
    VirtualViewSocket -->> ServerHandler : ack
    VirtualViewSocket ->> -ClientHandler : run
    ServerHandler ->> -View : transitionTo(Login)

    View ->> +Pippo : Enter a Command
    Pippo -->> View : Submit Login
    View ->> Pippo : Enter nickname
    Pippo -->> -View : Submit nickname

    View ->> VirtualControllerSocket : login(nickname)
    VirtualControllerSocket ->> ServerHandler : sendMsg(loginMsg)
    ServerHandler ->> ClientHandler : loginMsg
    ClientHandler ->> +Controller : login(nickname)
    Controller ->> MultiGame : isUnique(nickname)
    MultiGame -->> Controller : false
    Controller ->> MultiGame : isGame(nickname)
    MultiGame -->> Controller : false
    Controller ->> MultiGame : addUser(nickame)
    Controller ->> MultiGame : games.subscribe(virtualView)

    MultiGame ->> VirtualViewSocket : updateLobbyList(modelDiffs)
    VirtualViewSocket ->> ClientHandler : sendMsg(updateLobbyListMsg)
    ClientHandler ->> ServerHandler : updateLobbyListMsg
    ServerHandler ->> View : updateLobbyList(modelDiffs)

    Controller ->> VirtualViewSocket : log("Server Joined")
    VirtualViewSocket ->> ClientHandler : sendMsg(logMsg)
    ClientHandler ->> ServerHandler : logMsg
    ServerHandler ->> View : log("Server Joined")
    View ->> Pippo : "Server Joined"

    Controller ->> -VirtualViewSocket : transitionTo(JoinLobby)
    VirtualViewSocket ->> ClientHandler : sendMsg(transitionMsg)
    ClientHandler ->> ServerHandler : transitionMsg
    ServerHandler ->> View : transitionTo(JoinLobby)

    View ->> +Pippo : Enter a Command
    Pippo -->> View : Submit Display Lobby List
    View ->> Pippo : Lobby List
    deactivate Pippo

    View ->> +Pippo : Enter a Command
    Pippo -->> View : Submit Join Game
    View ->> Pippo : Enter a lobby name
    Pippo -->> -View : Submit lobbyName
    View ->> VirtualControllerSocket : joinLobby(lobbyName)
    VirtualControllerSocket ->> ServerHandler : sendMsg(joinLobbyMsg)
    ServerHandler ->> ClientHandler : joinLobbyMsg
    ClientHandler ->> +Controller : joinLobby(lobbyName)

    Controller ->> MultiGame : addPlayerToLobby(lobbyName, nickname)

    MultiGame ->> VirtualViewSocket : updateLobby(modelDiffs)
    VirtualViewSocket ->> ClientHandler : sendMsg(updateLobbyMsg)
    ClientHandler ->> ServerHandler : updateLobbyMsg
    ServerHandler ->> View : updateLobby(modelDiffs)

    Controller ->> VirtualViewSocket : log("Lobby Joined")
    VirtualViewSocket ->> ClientHandler : sendMsg(logMsg)
    ClientHandler ->> ServerHandler : logMsg
    ServerHandler ->> View : log("Lobby Joined")
    View ->> Pippo : "Lobby Joined"

    Controller ->> VirtualViewSocket : transitionTo(Lobby)
    VirtualViewSocket ->> ClientHandler : sendMsg(transitionMsg)
    ClientHandler ->> ServerHandler : transitionMsg
    ServerHandler ->> View : transitionTo(Lobby)


    Controller ->> MultiGame : isLobbyFull(lobbyName)
    MultiGame -->> Controller : true
    Controller ->> MultiGame : joinGame(nickname)

    MultiGame ->> VirtualViewSocket : updateGame(modelDiffs)
    VirtualViewSocket ->> ClientHandler : sendMsg(updateGameMsg)
    ClientHandler ->> ServerHandler : updateGameMsg
    ServerHandler ->> View : updateGame(modelDiffs)


    Controller ->> VirtualViewSocket : log("Game started")
    VirtualViewSocket ->> ClientHandler : sendMsg(logMsg)
    ClientHandler ->> ServerHandler : logMsg
    ServerHandler ->> View : log("Game started")
    View ->> Pippo : "Game started"


    Controller ->> -VirtualViewSocket : transitionTo(ChooseStartCard)
    VirtualViewSocket ->> ClientHandler : sendMsg(transitionMsg)
    ClientHandler ->> ServerHandler : transitionMsg
    ServerHandler ->> View : transitionTo(ChooseStartCard)

    View ->> Pippo : Enter a Command
```

# Sequence Diagram Report: Game Access Flow

## Objective

The sequence diagram illustrates the flow of actions involved in accessing a game, from the initial connection to navigating through game lobbies.

## Actors

- Pippo: The player or user interacting with the game.

## Components

- Client:

  - View: The user interface that displays the game and handles user input.
  - VirtualControllerSocket: Manages communication with the actual controller. It implements a Controller interface.
  - ServerHandler: Handles the connection between the client and the server handling the Socket messages.

- Server:

  - ClientHandler: Manages the client connection on the server side.
  - VirtualViewSocket: Manages communication with the actual view. It implements a View interface.
  - Controller: The controller that processes the user input and updates the model.
  - MultiGame: The model that manages the game state and player interactions.

## Disclaimer

The diagram has been written with an optimistic approach, assuming that all the actions are successful. The diagram does not include error handling or failure cases.

## Flow:

- Initial Connection:

  - Pippo enters the IP and port to connect.
  - The View sends the IP and port to the VirtualControllerSocket.
  - The VirtualControllerSocket connects to the server using the provided IP and port.
  - The ServerHandler establishes the connection with the server.
  - The server-side components acknowledge the connection and transition the View to the login interface.

- Login Process:

  - Pippo enters a nickname.
  - The View sends the nickname to the VirtualControllerSocket.
  - The VirtualControllerSocket sends a login message to the server.
  - The server-side components process the login message.
  - The Controller checks if the nickname is unique and not already in a game.
  - If the nickname is unique, the Controller adds the player to the game and subscribes the Virtual View to the game updates.
  - The MultiGame model updates the lobby list and notifies the Virtual Views.
  - The View transitions to the lobby interface.

- Game Lobby Access:

  - Pippo requests to join a game lobby.
  - The View sends the lobby name to the VirtualControllerSocket.
  - The VirtualControllerSocket sends a join lobby message to the server.
  - The server-side components process the join lobby message.
  - The Controller adds the player to the lobby and updates the lobby model.
  - The MultiGame model updates the lobby and notifies the Virtual Views.
  - The View transitions to the lobby interface.

- Game Start:

  - The Controller checks if the lobby is full.
  - If the lobby is full, the Controller starts the game.
  - The MultiGame model updates the game state and notifies the Virtual Views.
  - The View transitions to the game interface.
