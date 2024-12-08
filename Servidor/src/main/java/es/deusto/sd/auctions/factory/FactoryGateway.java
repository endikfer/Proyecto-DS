package es.deusto.sd.auctions.factory;

import es.deusto.sd.auctions.external.ServiceGateway;

public class FactoryGateway {
	public static ServiceGateway createServiceGateway(String info, String serverIP, int serverPort) {
        if (info.contains("meta")) {
            return new MetaFactory().createServiceGateway(serverIP, serverPort);
        } else if (info.contains("google")) {
            return new GoogleFactory().createServiceGateway();
        } else {
            throw new IllegalArgumentException("No se reconoce el servicio de google");
        }
    }
}
