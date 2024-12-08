package es.deusto.sd.auctions.factory;

public class FactoryGateway {
    public static Factory getFactory(String info) {
        if (info.endsWith("@meta.com")) {
            return new MetaFactory();
        } else if (info.endsWith("@gmail.com")) {
            return new GoogleFactory();
        } else {
            throw new IllegalArgumentException("Servicio no reconocido: " + info);
        }
    }
}
