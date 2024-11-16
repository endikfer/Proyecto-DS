package es.deusto.sd.auctions.entity;

import java.util.ArrayList;
import java.util.Map;

public class ServiciosExternos {
	private static Map<String, ArrayList<String>> emails;

	public ServiciosExternos(Map<String,  ArrayList<String>> emails) {
		super();
		this.emails = emails;
	}

	public static Map<String,  ArrayList<String>> getEmails() {
		return emails;
	}

	public void setEmails(Map<String,  ArrayList<String>> emails) {
		this.emails = emails;
	}
}
