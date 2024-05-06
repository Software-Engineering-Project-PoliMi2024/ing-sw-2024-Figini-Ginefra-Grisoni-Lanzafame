package it.polimi.ingsw.controller2.ConnectionLayer;

public interface ConnectionLayerServerRMI extends ConnectionLayerServer{
    void setConnectionClient(ConnectionLayerClient connectionClient);
}
