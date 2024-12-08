package es.deusto.sd.auctions.factory;

import es.deusto.sd.auctions.external.GoogleServiceGateway;
import es.deusto.sd.auctions.external.ServiceGateway;

public class GoogleFactory {
	public ServiceGateway createServiceGateway() {
        return new GoogleServiceGateway();
    }
}
