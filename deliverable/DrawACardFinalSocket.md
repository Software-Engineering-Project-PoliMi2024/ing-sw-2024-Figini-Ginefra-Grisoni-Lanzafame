sequenceDiagram
    title: Pescare una 🃏 e aggiurgerla propria 🤚
    actor Topolino

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

    View ->> Topolino : display draw Card Commands
    Topolino -->> View : drawCard(deckID, cardID)

    View ->> VirtualControllerSocket : drawCard(deckID, cardID)

    VirtualControllerSocket ->> ServerHandler : sendClientMsg(DrawMsg(deckId, cardID))

    ServerHandler ->> ClientHandler : drawCardMsg

    ClientHandler ->> Controller : draw(deckId, cardID)

    Controller ->> GameController : drawCard(nickname, deckId, cardID)

    GameController ->> Game : drawAndGetReplacement(deckId, cardId)

    alt DrawCard Is Null
        VirtualViewSocket ->> ClientHandler : sendServerMsg(LogErrMsg(EMPTY_DECK_POSITION))
        ClientHandler ->> ServerHandler : logErrMsg
        ServerHandler ->> View : logErrMsg(EMPTY_DECK_POSITION)
        View ->> Topolino : "There are no cards here."
    end

    GameController ->> Game : addCard(DrawCard)
    GameController ->> VirtualViewSocket : log(YOU_DRAW)
    VirtualViewSocket ->> ClientHandler : sendServerMsg(logMsg(YOU_DRAW))
    ClientHandler ->> ServerHandler : logMsg
    ServerHandler ->> View : log(YOU_DRAW)
    View ->> Topolino : "Successfully drew a card to your Hand."

    GameController ->> VirtualViewSocket : updateGame(HandDiffAdd)
    VirtualViewSocket ->> ClientHandler : sendServerMsg(updateGameMsg(HandDiffAdd))
    ClientHandler ->> ServerHandler : updateGameMsg
    ServerHandler ->> View : updateGame(diff)
    View ->> Topolino : "Display the new DrawCard"