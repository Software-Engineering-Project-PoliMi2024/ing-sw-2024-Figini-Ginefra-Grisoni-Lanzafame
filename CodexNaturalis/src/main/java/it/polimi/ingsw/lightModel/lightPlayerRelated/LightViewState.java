package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.view.ViewState;

public class LightViewState {
    private ViewState state;

    public LightViewState(ViewState state) {
        this.state = state;
    }

    public ViewState getState() {
        return state;
    }

    public void setState(ViewState state) {
        this.state = state;
    }
}
