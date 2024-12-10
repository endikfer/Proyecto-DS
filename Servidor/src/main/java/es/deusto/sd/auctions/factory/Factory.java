package es.deusto.sd.auctions.factory;

import es.deusto.sd.auctions.external.ServiceGateway;

public interface Factory {
	ServiceGateway createServiceGateway(String serverIP, int serverPort);
}
