package it.polimi.ingsw.model.cardReleted;

public class CardFactory {
    /*
     *The warnings are due to the fact that the compiler cannot guarantee that ResourceCardFactory
     * actually implements CardFactoryInterface<Element> because Element is a generic type parameter,
     * and its specific type is not known at compile-time.*/
    /**
     * Return the concrete factory that produce card of the "c-Class"
     * @param c the Class of card that need to be produced
     * @return the concreteFactory of the card
     * @throws IllegalArgumentException if the Class c is not one of the 4 card-Class type
     */
    @SuppressWarnings("unchecked")
    public <Element> AbstractCardFactory<Element> getFactoryOf(Class<Element> c) {
        if (c == ResourceCard.class) {
            return (AbstractCardFactory<Element>) new ResourceCardFactory();
        }
        if (c == ObjectiveCard.class) {
            return (AbstractCardFactory<Element>) new ObjectiveCardFactory();
        }
        if (c== GoldCard.class){
            return (AbstractCardFactory<Element>) new GoldCardFactory();
        }
        if (c== StartCard.class){
            return (AbstractCardFactory<Element>) new StartCardFactory();
        }
        throw new IllegalArgumentException();
    }
}
