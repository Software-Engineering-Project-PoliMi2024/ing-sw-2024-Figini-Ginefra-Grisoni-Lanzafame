sequenceDiagram
    title: Accesso al gioco
    actor Pippo

    box Client
        participant View
        participant VirtualControllerSocket
        participant ServerHandler
    end

    box Server
        participant ClientHandler
        participant VirtualViewSocket
        participant Controller
        participant LobbyGameListsController
        participant LobbyController
        participant GameController
    end

    View ->> Pippo : "Display Server Connection Commands"
    Pippo -->> View : Submit Connect
    View ->> Pippo : Enter IP and port
    Pippo -->> View : Submit IP and port
    
    
    View ->> VirtualControllerSocket : connect(ip, port, view)
    VirtualControllerSocket ->> Server : "new Socket"
    
    alt Socket not successfully open
        VirtualControllerSocket ->> View : logErr(Failed to connect to the server.)
        VirtualControllerSocket ->> View : transitionTo(SERVER_CONNECTION)
        View ->> Pippo : "Display Server Connection Commands"
    end

    Server -->> VirtualControllerSocket : "Socket successfully open"
    VirtualControllerSocket ->> ServerHandler : run
    Server ->> VirtualViewSocket : setController
    Server ->> ClientHandler : run
    
    Server ->> VirtualViewSocket : log(SERVER_JOINED)
    VirtualViewSocket ->> ClientHandler : sendServerMessage(logMsg)
    ClientHandler ->> ServerHandler : logMsg
    ServerHandler ->> View : log(SERVER_JOINED)
    View ->> Pippo : "Joined the server."
    Server ->> VirtualViewSocket : transitionTo(LOGIN_FORM)
    VirtualViewSocket ->> ClientHandler : sendServerMessage(transitionToMsg)
    ClientHandler ->> ServerHandler : transitionToMsg
    ServerHandler ->> View : transitionTo(LOGIN_FORM)
    View ->> Pippo : "Display Login Commands"
    Pippo -->> View : Submit Login
    View ->> Pippo : Enter nickname
    Pippo -->> View : Submit nickname

    View ->> VirtualControllerSocket : login(nickname)
    VirtualControllerSocket ->> ServerHandler : sendClientMessage(loginMsg)
    ServerHandler ->> ClientHandler : loginMsg

    ClientHandler ->> Controller : login(nickname)
    Controller ->> LobbyGameListsController : login(nickname)
    LobbyGameListsController ->> LobbyGameListsController : containsKey(nickname)
    alt the nickname is in the server
        LobbyGameListsController ->> VirtualViewSocket : logErr(NAME_TAKEN)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(logErrMsg)
        ClientHandler ->> ServerHandler : logErrMsg
        ServerHandler ->> View : logErr(NAME_TAKEN)
        View ->> Pippo : "The chosen nickname is already in use."
        LobbyGameListsController ->> VirtualViewSocket : transitionTo(SERVER_CONNECTION) 
        VirtualViewSocket ->> ClientHandler : sendServerMessage(transitionToMsg)
        ClientHandler ->> ServerHandler : transitionToMsg
        ServerHandler ->> View : transitionTo(LOGIN_FORM)
        View ->> Pippo : "Display login commands"
    else the nickname is not in the server
        LobbyGameListsController ->> LobbyGameListsController : nickname.matches(invalidNicknameRegex)
        alt the nickname is not valid
            LobbyGameListsController ->> VirtualViewSocket : logErr(NOT_VALID_NICKNAME)
            VirtualViewSocket ->> ClientHandler : sendServerMessage(logErrMsg)
            ClientHandler ->> ServerHandler : logErrMsg
            ServerHandler ->> View : logErr(NOT_VALID_NICKNAME)
            View ->> Pippo : "The chosen nickname is not valid."
            LobbyGameListsController ->> VirtualViewSocket : transitionTo(LOGIN_FORM) 
            VirtualViewSocket ->> ClientHandler : sendServerMessage(transitionToMsg)
            ClientHandler ->> ServerHandler : transitionToMsg
            ServerHandler ->> View : transitionTo(LOGIN_FORM)
            View ->> Pippo : "Show login commands"
        end
    end
    
    LobbyGameListsController ->> VirtualViewSocket : updateLobbyList(DiffGenerator(lobbyHistory))
    VirtualViewSocket ->> ClientHandler : sendServerMessage(UpdateLobbyListMsg)
    ClientHandler ->> ServerHandler : UpdateLobbyListMsg
    ServerHandler ->> View : updateLobbyList(diff)

    LobbyGameListsController ->> VirtualViewSocket : transitionTo(JOIN_LOBBY) 
    VirtualViewSocket ->> ClientHandler : sendServerMessage(transitionToMsg)
    ClientHandler ->> ServerHandler : transitionToMsg
    ServerHandler ->> View : transitionTo(JOIN_LOBBY)

    View ->> Pippo : Display Lobby List
    Pippo -->> View : Submit Join Game
    View ->> Pippo : Enter a lobby name
    Pippo -->> View : Submit lobbyName
    View ->> VirtualControllerSocket : joinLobby(lobbyName)
    VirtualControllerSocket ->> ServerHandler : sendClientMsg(joinLobbyMsg)
    ServerHandler ->> ClientHandler : joinLobbyMsg
    ClientHandler ->> Controller : joinLobby(lobbyName)
    Controller ->> LobbyGameListsController : joinLobby(lobbyName)

    alt lobbyToJoin is null
        LobbyGameListsController ->> VirtualViewSocket : logErr(LOBBY_NONEXISTENT)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(logErrMsg)
        ClientHandler ->> ServerHandler : logErrMsg
        ServerHandler ->> View : logErr(LOBBY_NONEXISTENT)
        View ->> Pippo : "The queried lobby does not exist."
    end

    LobbyGameListsController ->> LobbyController : addPlayer(nickName)
    LobbyController ->> VirtualViewSocket : updateLobby(DiffGenerator(lobby))
    VirtualViewSocket ->> ClientHandler : sendServerMsg(UpdateLobbyMsg)
    ClientHandler ->> ServerHandler : UpdateLobbyMsg
    ServerHandler ->> View : updateLobby(diff)

    LobbyGameListsController ->> LobbyController : lobbyToJoin.isLobbyFull()
    alt lobby is not full
        LobbyController -->> LobbyGameListsController : false
        LobbyGameListsController ->> VirtualViewSocket : transitionTo(LOBBY)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(transitionToMsg)
        ClientHandler ->> ServerHandler : transitionToMsg
        ServerHandler ->> View : transitionTo(LOBBY)
        View ->> Pippo : "Display the Lobby" 
    else lobby is full
        LobbyController -->> LobbyGameListsController : true

        LobbyController ->> VirtualViewSocket : logGame(GAME_CREATED)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(logGameMsg)
        ClientHandler ->> ServerHandler : logGameMsg
        ServerHandler ->> View : logGame(GAME_CREATED)
        View ->> Pippo : "Lobby is full, the game is starting."

        LobbyController ->> LobbyController : startGame(lobby)
        LobbyController ->> GameController : GameController()

        GameController ->> VirtualViewSocket : updateLobby(LittleBoyLobby())
        VirtualViewSocket ->> ClientHandler : sendServerMessage(updateLobbyMsg)
        ClientHandler ->> ServerHandler : updateLobbyMsg
        ServerHandler ->> View : updateLobby(diff)

        LobbyController ->> GameController : join(nickname, view) 
        
        GameController ->> VirtualViewSocket : updateGame(updateJoinStartCard)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(updateGame)
        ClientHandler ->> ServerHandler : updateGameMsg
        ServerHandler ->> View : updateGame(diff)
        GameController ->> VirtualViewSocket : logGame(GAME_JOINED)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(logGameMsg)
        ClientHandler ->> ServerHandler : logGameMsg
        ServerHandler ->> View : logGameMsg(GAME_JOINED)
        View ->> Pippo: "Joined game."

        GameController ->> VirtualViewSocket : transitionTo(CHOOSE_START_CARD)
        VirtualViewSocket ->> ClientHandler : sendServerMessage(transitionToMsg)
        ClientHandler ->> ServerHandler : transtionToMsg
        ServerHandler ->> View : transitionTo(CHOOSE_START_CARD)
        View ->> Pippo: "Display Choose Start Card Option"
    end