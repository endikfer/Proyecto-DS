package es.deusto.sd.auctions.factory;

import es.deusto.sd.auctions.external.MetaServiceGateway;
import es.deusto.sd.auctions.external.ServiceGateway;

public class MetaFactory implements Factory{
	public ServiceGateway createServiceGateway(String serverIP, int serverPort) {
        return new MetaServiceGateway(serverIP, serverPort);
    }
}
