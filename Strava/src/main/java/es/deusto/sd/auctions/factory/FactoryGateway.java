package es.deusto.sd.auctions.factory;

import es.deusto.sd.auctions.entity.TipoLogIn;
import es.deusto.sd.auctions.external.GoogleServiceGateway;
import es.deusto.sd.auctions.external.MetaServiceGateway;
import es.deusto.sd.auctions.external.ServiceGateway;

public class FactoryGateway {
    public ServiceGateway factoria (TipoLogIn tipoLogIn) {
        if (tipoLogIn == null) {
            throw new IllegalArgumentException("El tipo de login no puede ser nulo.");
        }

        switch (tipoLogIn) {
            case META:
                return new MetaServiceGateway();
            case GOOGLE:
                return new GoogleServiceGateway();
            default:
                throw new IllegalArgumentException("Tipo de login no soportado: " + tipoLogIn);
        }
    }
}
