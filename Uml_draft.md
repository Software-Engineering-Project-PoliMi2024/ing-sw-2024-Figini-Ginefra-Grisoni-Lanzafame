# This is a first draft of the UML diagram for the project.

```mermaid
---
title: Codex in Naturalis UML
---
classDiagram
    direction LR
    Card <|-- ResourceCard
    Card <|-- ObjectiveCard

    ResourceCard <|-- GoldCard

    GoldCard --> CardPointMultiplier : pointMultiplier

    ResourceCard --> Resource : permanetResource


    CardPointMultiplier <|-- ResourceCardPointMultiplier
    CardPointMultiplier <|-- TopologicalCardPointMultiplier
    CardPointMultiplier --* ObjectiveCard : pointMultiplier

    Collectable <|-- Resource
    Collectable <|-- Object
    Collectable <|-- Special

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

        class GoldCard{
            - HashMap~Resource, int~ requirements
            + getRequiredResources() : HashMap~Resource, int~
        }

        class ObjectiveCard{

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
            + getCollectableAt(CardCorner corner, CardFace face) : Collectable
            + isVisible(CardCorner corner, CardFace face) : boolean
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




```
