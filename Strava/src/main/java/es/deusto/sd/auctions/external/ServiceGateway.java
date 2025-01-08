package es.deusto.sd.auctions.external;

public interface ServiceGateway {
	boolean verifyEmail(String email);
    boolean validateLogin(String email, String password);
}
