package it.polimi.ingsw.controller.RMI;

public enum LabelAPI {
    Login("login"),
    GetMultiGames("getMultiGames");

    private final String label;

    LabelAPI(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
