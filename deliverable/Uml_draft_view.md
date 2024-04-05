# This is a first draft of the UML diagram for the view.

```mermaid
classDiagram
    direction LR

    ViewState <|-- ServerConnectState
    ViewState <|-- JoinGameState
    ViewState <|-- LobbyState
    ViewState <|-- CreateGameState
    ViewState <|-- ChooseStartCardState
    ViewState <|-- IdleState
    ViewState <|-- SelectPrivateObjectiveState
    ViewState <|-- DrawCardState
    ViewState <|-- PlaceCardState
    ViewState <|-- PostGameState

    View <|-- TUI
    View <|-- GUI

    View --> "0..n" Action
    View --> "0..n" Visualization


    Action <|-- Connect
    Action <|-- PlaceCard
    Action <|-- ChooseDraw
    Action <|-- GoTo
    Action <|-- JoinGame
    Action <|-- LeaveLobby
    Action <|-- CreateGame
    Action <|-- SelectStartCardFace
    Action <|-- Peek
    Action <|-- SelectObjective

    GoTo --> ViewState : target

    class ViewState{
        <<Abstract>>
        + run(View view)
    }

    class ServerConnectState{

    }
    note for ServerConnectState "ACTIONS\n Add: Connect \n Remove: All"
    note for ServerConnectState "VISUALIZATIONS\nAdd: ConnectForm \n Remove: All"


    class JoinGameState{

    }
    note for JoinGameState "ACTIONS\n Add: GoTo(ServerConnectState), GoTo(CreateGameState), JoinGame \n Remove: All"
    note for JoinGameState "VISUALIZATIONS\nAdd: GameList \n Remove: All"

    class LobbyState{

    }
    note for LobbyState "ACTIONS\n Add: LeaveLobby \n Remove: All"
    note for LobbyState "VISUALIZATIONS\nAdd: ShowLobby \n Remove: All"


    class CreateGameState{

    }
    note for CreateGameState "ACTIONS\n Add: GoTo(JoinGameState) CreateGame \n Remove: All"
    note for CreateGameState "VISUALIZATIONS\nAdd: CreateGameForm \n Remove: All"

    class ChooseStartCardState{

    }
    note for ChooseStartCardState "ACTIONS\n Add: SelectStartCardFace Peek \n Remove: All"
    note for ChooseStartCardState "VISUALIZATIONS\nAdd: ShowStartCard ShowDecks ShowCodex PeekForm \n Remove: All"

    class SelectPrivateObjectiveState{

    }
    note for SelectPrivateObjectiveState "ACTIONS\n Add: SelectObjective \n Remove: SelectStartCardFace"
    note for SelectPrivateObjectiveState "VISUALIZATIONS\nAdd: ShowHand, ShowObjectiveOptions \n Remove: ShowStartCard"

    class IdleState{

    }
    note for IdleState "ACTIONS: Peek"
    note for IdleState "VISUALIZATIONS: ShowCodex, ShowDecks, ShowHand, PeekForm, ShowLog"

    class DrawCardState{

    }
    note for DrawCardState "ACTIONS: ChooseDraw, Peek"
    note for DrawCardState "VISUALIZATIONS: ShowCodex, ShowDecks, ShowHand, PeekForm, DrawForm, ShowLog"

    class PlaceCardState{

    }
    note for PlaceCardState "ACTIONS: PlaceCard, Peek"
    note for PlaceCardState "VISUALIZATIONS: ShowCodex, ShowDecks, ShowHand, PeekForm, PlacementForm, ShowLog"

    class PostGameState{

    }
    note for PlaceCardState "ACTIONS: GoTo(JoinGameState)"
    note for PlaceCardState "VISUALIZATIONS: PostGameView"


    class View{
        <<Abstract>>
        +displayCodex()
        +clearActions()
        +clearVisualizations()
        +render()
    }

    class TUI{

    }

    class GUI{

    }

    class Client{

    }

    class Visualization{
        <<Enum>>
        - final function : Function<View, Void>
        + render(View view)

        CONNECT_FORM
        GAME_LIST
        SHOW_LOBBY
        SHOW_HAND
        SHOW_OBJECTIVE_OPTIONS
        CREATE_GAME_FORM
        SHOW_START_CARD
        SHOW_DECK
        SHOW_CODEX
        PEEK_FROM
        SHOW_LOG
        DRAW_FORM
        PLACEMENT_FORM
        POST_GAME_VIEW
    }

    class Action{
        <<Abstract>>
    }

    note for Connect "Sets View\nChoose Connection\nConnects to the server sending nickname"
    class Connect{

    }

    note for GoTo "Goes to the specified state"
    class GoTo{

    }

    note for JoinGame "Joins the selected game"
    class JoinGame{

    }

    note for LeaveLobby "Leaves the Lobby"
    class LeaveLobby{

    }

    note for CreateGame "Creates a new game sending the name and max players"
    class CreateGame{

    }

    note for SelectStartCardFace "Sends to the server the chosen side of the start card"
    class SelectStartCardFace{

    }

    note for Peek "Sends to the server the chosen player to peek from"
    class Peek{

    }

    note for Peek "Sends to the server the chosen secret Objective"
    class SelectObjective{

    }

    note for ChooseDraw "Sends to the server where to draw"
    class ChooseDraw{

    }

    note for PlaceCard "Sends to the server where to place the card"
    class PlaceCard{

    }

```
