# This is a first draft of the UML diagram for the project.

```mermaid
---
title: Codex in Naturalis UML - Model
---
classDiagram
    direction TB
    Card <|-- CardWithCorners
    Card <|-- ObjectiveCard
    CardWithCorners <|-- CardInHand
    CardWithCorners <|-- StartCard

    CardInHand <|--ResourceCard
    CardInHand <|--GoldCard

    GoldCard --> CardPointMultiplier

    CardPointMultiplier <|-- ResourceCardPointMultiplier
    CardPointMultiplier <|-- TopologicalCardPointMultiplier
    CardPointMultiplier --* ObjectiveCard


    Collectable <|-- Resource
    Collectable <|-- WritingMaterials
    Collectable <|-- Special



    namespace CardRelated{
        class Card{
            <<Abstract>>
            - int points
            +int getPoints()
        }

        class Collectable{
            <<Abstract>>
        }

        class Resource{
            <<Enumeration>>
            PLANT
            ANIMAL
            FUNGI
            INSECT
        }

        class WritingMaterials{
            <<Enumeration>>
            QUILL
            INKWELL
            MANUSCPRIPT
        }

        class Special{
            <<Enumeration>>
            EMPTY
        }

        class CardWithCorners{
            <<Abstract>>
            + getCollectableAt(CardCorner corner, CardFace face) : Collectable
            + isVisible(CardCorner corner, CardFace face) : boolean
            + getCollectableAt(CardCorner corner, CardFace face) : Collectable
        }

        class CardInHand{
            <<Abstract>>
        }

        class GoldCard{
            - pointMultiplier : CardPointMultiplier
            - HashMap~Resource, int~ requirements
            + getRequiredResources() : HashMap~Resource, int~
        }

        class ObjectiveCard{
            - pointMultiplier : CardPointMultiplier
        }

        class CardPointMultiplier{
            <<Interface>>
            + getMultiplier() : int
        }

        class ResourceCardPointMultiplier{
        }

        class TopologicalCardPointMultiplier{
        }

        class ResourceCard{
            - permanetResource : ResourceCard

        }

        class StartCard{
            - Set~Resource~ permanentResources
            + getPermanentResource(CardFace) : Set~Resource~
        }

        class CardCorner{
            <<Enumeration>>
            TL
            TR
            BR
            BL
        }

        class CardFace{
            <<Enumeration>>
            FRONT
            BACK
        }
    }

    Placement --> Position
    Placement --> CardWithCorners

    Codex --> Placement

    Frontier --> Position

    namespace PerPlayerRelated{
        class Position{
            <<Final>>
            - x:int
            - y:int
            + getPosition() : int[]
        }

        class Placement{
            - card:CardWithCorners
            - position: Position
            - face: CardFace
            - consequences: HashMap~Collectable, int~
            + getCard() : Card
            + getPosition() : Position
            + getFace() : CardFace
        }

        class Codex{
            - HashMap~Collectable, int~ collectables
            - Queue~Placement~ placementsHistory
            - int points
            + addPlacement(Placement placement) : void
            + getPlacementAt(Position position) : Placement
        }

        class Frontier{
            - frontier: List ~Position~
            + updateFrontier(Codex codex, Frontier frontier, Placement placement) : void$
            + getFrontier() : List ~Position~
            + addPosition(Position position) : void
            + removePosition(Position position) : void
        }

        class Hand{
            - secrectObjective : ObjectiveCard
            - actualHand : Set ~CardInHand~
            + addCard(card: CardInHand)
            + getHand() : Set ~CardInHand~
            + getSecretObjective() : ObjectiveCard
            + removeCard(card: CardInHand)
        }
    }

    User --> Codex
    User --> Frontier
    User --> Hand
    GameParty --> User

    Hand --> ObjectiveCard
    Hand --> CardInHand

    Game --> Deck
    Game --> GameParty


    namespace TableRelated{
        class User{
            - String nickname
            - Codex codex
            - Frontier frontier
            - Hand hand
            + getNickname() : String
        }

        class GameParty{
            - int numberOfPlayers
            - List~User~ users

            + addUser(User user) : void
            + removeUser(User user) : void
        }

        class Deck~Element~{
            final bufferSize : int
            buffer : Set~Element~
            actualDeck : QueueSet~Element~
            - shuffle()
            + getBuffer() : Set~Element~
            + drawFromBuffer(Element element) : Element
            + drawFromDeck() : Element
        }

        class Game{
            - gameParty : GameParty
            - objectiveCardDeck : Deck~ObjectiveCard~
            - resourceCardDeck : Deck~resourceCardDeck~
            - goldCardDeck : Deck~goldCardDeck~
            - startCardDeck : Deck~startCardDeck~
        }

    }
```
