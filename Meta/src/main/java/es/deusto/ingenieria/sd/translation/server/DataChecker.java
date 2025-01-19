package es.deusto.ingenieria.sd.translation.server;

import java.util.HashMap;
import java.util.Map;

public class DataChecker {
	
	public Map<String, String> emails = new HashMap<>();	
	
	public void inicializarEmails() {
	    emails.put("paco@meta.com", "password123");
	    emails.put("maria@meta.com", "1234abcd");
	    emails.put("help@meta.com", "ABCD1234");
	    emails.put("contact@meta.com", "1a2b3c4d");
	}

	
	public String logIn(String email, String contraseña){
		
		if(emails.containsKey(email)) {
			if(emails.get(email).equals(contraseña)) {
				return "OK";
			}
		}
		return "";
	}
	
	public String register(String email){
		if(emails.containsKey(email)) {
			return "OK";
		}
		return "";
	}
	
}
