sequenceDiagram
    title: Giocare una 🃏 dalla propria 🤚
    actor Pluto

    box Client
        participant View
        participant VirtualControllerSocket
        participant ServerHandler
    end

    box Server
        participant ClientHandler
        participant Controller
        participant VirtualViewSocket
        participant GameController
        participant Game
    end

    View ->> Pluto : display Place Card Commands
    Pluto -->> View : placeCard

    View ->> VirtualControllerSocket : placeCard(nickname, lightPlacement)

    VirtualControllerSocket ->> ServerHandler : sendClientMsg(PlaceCardMsg(nickname, lightPlacement))

    ServerHandler ->> ClientHandler : placeCardMsg()

    ClientHandler ->> Controller : placeCard(nickname, LightPlacement)

    Controller ->> GameController : placeCard(nickname, 2, 0, 15)

    GameController ->> Game : placeCard(nickname, placement)

    GameController ->> VirtualViewSocket : log(YOU_PLACED)
    VirtualViewSocket ->> ClientHandler : sendServerMsg(logMsg(YOU_PLACED))
    ClientHandler ->> ServerHandler : logMsg
    ServerHandler ->> View : log(YOU_PLACED)
    View ->> Pluto : "Successfully placed a card in your Codex."

    GameController ->> VirtualViewSocket : updateGame(CodexDiffPlacement)
    VirtualViewSocket ->> ClientHandler : sendServerMsg(updateGameMsg)
    ClientHandler ->> ServerHandler : updateGameMsg
    ServerHandler ->> View : updateGame(diff)
    View ->> Pluto : "Display the updated game state"
