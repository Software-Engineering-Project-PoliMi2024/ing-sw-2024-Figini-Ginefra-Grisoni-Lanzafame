### State machine for the game

```mermaid
---
title: Game state machine
---
stateDiagram-v2
    [*] --> CHOOSE_START_CARD : The game starts
    CHOOSE_START_CARD --> CHOOSE_PAWN : <center>Every active player has chosen\ntheir Start Card</center>
    CHOOSE_PAWN --> CHOOSE_SECRET_OBJECTIVE : <center>Every active player has chosen\ntheir Pawn</center>
    CHOOSE_SECRET_OBJECTIVE --> ACTUAL_GAME : <center>Every active player has chosen\ntheir Secret Objective</center>
    ACTUAL_GAME --> LAST_TURNS : <center>One player has collected at least 20 points \n or the decks are empty</center>
    LAST_TURNS --> END_GAME : <center>The last turn ended</center>
    END_GAME --> [*] : All the player left the game
```

This state machine represents the game flow. The game starts with the players choosing their Start Card, then their Pawn, and finally their Secret Objective. All these phases are done in parallel.

After that, the actual game starts. The game ends when one player has collected at least 20 points or the decks are empty. The game then enters the last turns phase, where the players play their last turns. The game ends when the last turn ends.
When all the players leave the game, it is destroyed.
