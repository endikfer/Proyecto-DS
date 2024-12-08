package es.deusto.sd.auctions.factory;

import es.deusto.sd.auctions.external.GoogleServiceGateway;
import es.deusto.sd.auctions.external.ServiceGateway;

public class GoogleFactory implements Factory {
    @Override
    public ServiceGateway createServiceGateway(String serverIP, int serverPort) {
        return new GoogleServiceGateway();
    }
}
