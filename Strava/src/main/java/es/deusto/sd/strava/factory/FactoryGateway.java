package es.deusto.sd.strava.factory;

import org.springframework.stereotype.Component;

import es.deusto.sd.strava.entity.Login;
import es.deusto.sd.strava.external.GoogleServiceGateway;
import es.deusto.sd.strava.external.MetaServiceGateway;
import es.deusto.sd.strava.external.ServiceGateway;

@Component
public class FactoryGateway {
    public ServiceGateway factoria (Login tipoLogIn) {
        if (tipoLogIn.getName() == null || tipoLogIn.getName().isEmpty()) {
            throw new IllegalArgumentException("El tipo de login no puede ser nulo.");
        }

        switch (tipoLogIn.getName().toUpperCase()) {
            case "META":
                return new MetaServiceGateway();
            case "GOOGLE":
                return new GoogleServiceGateway();
            default:
                throw new IllegalArgumentException("Tipo de login no soportado: " + tipoLogIn);
        }
    }
}
