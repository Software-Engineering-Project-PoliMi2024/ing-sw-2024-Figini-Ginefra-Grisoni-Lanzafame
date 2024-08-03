package it.polimi.ingsw.model.playerReleted.AgentRelated;

public class AgentNames {
    public static String getRandomName() {
        return "Agent" + (int)(Math.random() * 10000.0D);
    }
}
