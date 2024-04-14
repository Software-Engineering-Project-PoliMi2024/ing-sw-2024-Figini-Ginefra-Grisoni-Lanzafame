package it.polimi.ingsw.controller.RMI;

public enum labelAPI {
    Login("login"),
    GetMultiGames("getMultiGames");

    private final String label;

    labelAPI(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
