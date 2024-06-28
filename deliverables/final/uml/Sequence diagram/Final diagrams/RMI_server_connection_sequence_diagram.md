sequenceDiagram
    title: Establish Connection with RMI
    actor Pippo

    box Client
        participant View
        participant RMIVirtualController
    end

    participant RMI LocateRegisrty

    box Server
        participant ServerConnectionRMI
        participant Registry
        participant RMIVirtualView
        participant Server
    end

    Server ->> Server : set server rmi.hostname
    Server ->> Server : createRegistry
    Server ->> Server : create ServerConnectionRMI
    Server ->> Registry : rebind("ServerConnectionRMI", ServerConnectionRMIStub)

    View ->> +Pippo : Display Connection Form
    Pippo -->> -View : Submit IP and port
    View ->> +RMIVirtualController : connectToServer(ip, port, view)
    RMIVirtualController ->> RMIVirtualController : set view
    RMIVirtualController ->> +RMIVirtualController : get client ip
    alt client is offline
        RMIVirtualController ->> +View : logErr(UNABLE_TO_GET_IP)
        RMIVirtualController ->> View : transitionTo(SERVER_CONNECTION)
        View ->> -Pippo : Display Connection Form
    else clienserverReferencet is online
        RMIVirtualController ->> -RMIVirtualController : set client rmi.hostname
        RMIVirtualController ->> +RMI LocateRegisrty: getRegistry(ip, port)
        RMIVirtualController ->> +RMIVirtualController: start countDown
        alt doesn't find a registry at the specified ip or port
            RMIVirtualController ->> -RMIVirtualController: the getRegistry times out 
            RMIVirtualController ->> +View : logErr(CONNECTION_ERROR)
            RMIVirtualController ->> View : transitionTo(SERVER_CONNECTION)
            View ->> -Pippo : Display Connection Form
        else the search fails before countDown times out
            RMI LocateRegisrty ->> RMIVirtualController : error registry not found
            RMIVirtualController ->> +View : logErr(CONNECTION_ERROR)
            RMIVirtualController ->> View : transitionTo(SERVER_CONNECTION)
            View ->> -Pippo : Display Connection Form
        else registry found
            RMI LocateRegisrty ->> -RMIVirtualController : return Registry

            RMIVirtualController ->> +Registry : lookUp(ServerConnectionRMI)
            Registry ->> -RMIVirtualController : returns the ServerConnectionRMI stub
            RMIVirtualController ->> +ServerConnectionRMI : connect(virtualControllerStub, viewStub)
            ServerConnectionRMI ->> ServerConnectionRMI : new RMIVirtualView(viewStub)
            ServerConnectionRMI ->> RMIVirtualView : setController(controllerStub)
            ServerConnectionRMI ->> RMIVirtualView : setHeartBeat(citrualControllerStub)
            ServerConnectionRMI ->> ServerConnectionRMI : controllerOnServer = new Controller()
            ServerConnectionRMI ->> RMIVirtualController : setController(controllerOnServer)
            ServerConnectionRMI ->> RMIVirtualController : setHeartBeat(RMIVirtualViewStub) 
            ServerConnectionRMI ->> RMIVirtualView : startHeartBeat()
            ServerConnectionRMI ->> RMIVirtualController : startHeartBeat()
            loop
                RMIVirtualController ->> RMIVirtualView: checkEmpty()
                RMIVirtualView ->> RMIVirtualController: checkEmpty()
            end
            ServerConnectionRMI ->> +View : log(CONNECTION_SUCCESS)
            ServerConnectionRMI ->> -View : transitionTo(LOGIN_FORM)
            View ->> -Pippo : Display Login Form
        end

    end