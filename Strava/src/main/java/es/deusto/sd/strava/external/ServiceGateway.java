package es.deusto.sd.strava.external;

public interface ServiceGateway {
	boolean verifyEmail(String email);
    boolean validateLogin(String email, String password);
}
