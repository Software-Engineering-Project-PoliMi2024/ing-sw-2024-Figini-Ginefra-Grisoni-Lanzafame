# This is a first draft of the UML diagram for the project.

```mermaid
---
title: Codex in Naturalis UML
---
classDiagram
    direction TB
    Card <|-- CardWithCorners
    Card <|-- ObjectiveCard
    CardWithCorners <|-- ResourceCard
    CardWithCorners <|-- StartCard
    CardWithCorners --> CornerModel

    ResourceCard <|-- GoldCard

    GoldCard --> CardPointMultiplier

    ResourceCard --> Resource


    CardPointMultiplier <|-- ResourceCardPointMultiplier
    CardPointMultiplier <|-- TopologicalCardPointMultiplier
    CardPointMultiplier --* ObjectiveCard

    Collectable <|-- Resource
    Collectable <|-- Object
    Collectable <|-- Special

    CornerModel --> Collectable
    CornerModel --> CardCorner
    CornerModel --> CardFace



    namespace CardRelated{
        class Card{
            <<Abstract>>
            - int points
            +int getPoints()
        }

        class Collectable{
            <<Interface>>
        }

        class Resource{
            <<Enumeration>>
            PLANT
            ANIMAL
            FUNGI
            INSECT
        }

        class Object{
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
            - CornerModel cornerModel
            + getCollectableAt(CardCorner corner, CardFace face) : Collectable
        }


        class CornerModel{
            - HashMap~CardFace,HashMap~CardCorner, Collectable~~ cornerMap
            + getCollectableAt(CardCorner corner, CardFace face) : Collectable
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
            + int getMultiplier()
        }

        class ResourceCardPointMultiplier{
        }

        class TopologicalCardPointMultiplier{
        }

        class ResourceCard{
            - permanetResource : ResourceCard
            + isVisible(CardCorner corner, CardFace face) : boolean
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

        class Deck~Element~{
        }
    }

    Placement --> Position
    Placement --> CardWithCorners
    Placement --> CardFace
    Placement --> Collectable

    Codex --> Placement
    Codex --> Collectable

    Frontier --> Placement
    Frontier --> CardFace

    namespace PlayerRelated{
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
        }

        class Frontier{
            - HashMap ~CardFace, List~Placement~~ frontier
            + updateFrontier(Codex codex, Frontier frontier, Placement placement) : void$
            + getFrontier() : HashMap ~CardFace, List~Placement~~
            + getFrontier(CardFace face) : List~Placement~
            + addPlacement(Placement placement) : void
            + removePlacement(Placement placement) : void
        }
    }

    User --> Codex
    User --> Frontier
    GameParty --> User

    namespace GameLoopRelated{
        class User{
            - String nickname
            - Codex codex
            - Frontier frontier
            + getNickname() : String
        }

        class GameParty{
            - int numberOfPlayers
            - List~User~ users

            + addUser(User user) : void
            + removeUser(User user) : void
        }

    }
```
