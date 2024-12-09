package es.deusto.ingenieria.sd.translation.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

public class DataChecker {
	
	private Map<String, String> emails = new HashMap<>();
	
	public void inicializarEmails() {
	    emails.put("usuario1@ejemplo.com", "password123");
	    emails.put("usuario2@ejemplo.com", "1234abcd");
	    emails.put("usuario3@ejemplo.com", "miContraseña");
	}

	
	public String LogIn(String email, String contraseña){
		
		if(emails.containsKey(email)) {
			if(emails.get(email).equals(contraseña)) {
				return "OK";
			}
		}
		return null;
	}
	
	public String Register(String email){
		if(emails.containsKey(email)) {
			return "OK";
		}
		
		return null;
	}
	
}
