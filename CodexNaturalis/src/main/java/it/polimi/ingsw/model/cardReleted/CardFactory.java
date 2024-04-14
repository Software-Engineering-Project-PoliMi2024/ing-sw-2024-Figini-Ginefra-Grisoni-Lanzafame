package it.polimi.ingsw.model.cardReleted;

/*Implementation of the "parameterized factory method".
 "https://www.baeldung.com/java-factory-pattern-generics" for everyone who is wondering if this code contains some bad practices
 I am open to implementing anyone's ideas and avoiding this casting nonsense. However, I have already tried:
 ○Declaring all concrete factories as static, but I can't due to them extending AbstractCardFactory and its methods
 ○Declaring getCards() in AbstractCardFactory as static, but I can't because it is an abstract class.
 ○Trying to use AbstractCardFactory<> in its raw form (without specifying the <Element>), but the return of getCards(),
 called in the concrete factories, is erased because:
 "Reason: 'abstractCardFactory' has raw type, so the result of getCards is erased" as stated by the compiler.
 ○Declaring 4 abstractCardFactory<> and instead of declaring them with the generic <Element>,
 I created them with their specific <TypeOfCard>. This method works, but it basically negates
 any advantage created by the factory design pattern.

 *The warnings are due to the fact that the compiler cannot guarantee that ResourceCardFactory/GoldCardFactory ecc
 * actually implements CardFactoryInterface<Element> because Element is a generic type parameter,
 * and its specific type is not known at compile-time.

 * Sorry for the rant but I wasted too many hours working in this classes*/

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;

public record CardFactory(String filePath) {
    /**
     * The constructor of the class
     *
     * @param filePath the path of the card.json file
     */
    public CardFactory {
    }

    /**
     * Return the concrete factory that produce card of the "c-Class"
     *
     * @param c the Class of card that need to be produced
     * @return the concreteFactory of the card
     * @throws IllegalArgumentException if the Class c is not one of the 4 card-Class type
     */
    @SuppressWarnings("unchecked")
    public <Element> AbstractCardFactory<Element> getFactoryOf(Class<Element> c) {
        if (c == ResourceCard.class) {
            return (AbstractCardFactory<Element>) new ResourceCardFactory(filePath);
        }
        if (c == ObjectiveCard.class) {
            return (AbstractCardFactory<Element>) new ObjectiveCardFactory(filePath);
        }
        if (c == GoldCard.class) {
            return (AbstractCardFactory<Element>) new GoldCardFactory(filePath);
        }
        if (c == StartCard.class) {
            return (AbstractCardFactory<Element>) new StartCardFactory(filePath);
        }
        throw new IllegalArgumentException();
    }

    /**
     * @return the path of the card.json file
     */
    @Override
    public String filePath() {
        return filePath;
    }
}
