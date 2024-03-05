# This is a first draft of the UML diagram for the project.

```mermaid
---
title: Codex in Naturalis UML
---
classDiagram
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

    GoldCard --> CardPointMultiplier : pointMultiplier


    ResourceCard <|-- GoldCard

    ResourceCard --> Resource : permanetResource
    Collectable <|-- Resource
    Collectable <|-- Object
    Collectable <|-- Special

    class ObjectiveCard{

    }


    Card <|-- ObjectiveCard
    Card <|-- ResourceCard

    class CardPointMultiplier{
        <<Interface>>
        + int getMultiplier()
    }

    class ResourceCardPointMultiplier{
    }

    class TopologicalCardPointMultiplier{
    }

    CardPointMultiplier <|-- ResourceCardPointMultiplier
    CardPointMultiplier <|-- TopologicalCardPointMultiplier
    CardPointMultiplier --* ObjectiveCard : pointMultiplier

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




    class Duck~Element~{
    }


```
