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

import org.json.JSONArray;

public class DataChecker {

	
	public String LogIn(String email, String contraseña) throws Exception {
		
		String url = "https://api.meta.com/login?email=" + email + "&password="+contraseña;
		
		URI uri = null;
		URL obj = null;
		HttpURLConnection con = null;
		
		try {
			uri = new URI(url);
			obj = uri.toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
		if (uri != null && obj != null) {
			con = (HttpURLConnection) obj.openConnection();
		}
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
		
		return parseResult(response.toString());
		
	}
	
	public String Register(String email) throws Exception {
	    String url = "https://api.meta.com/register";
	    URI uri = null;
	    URL obj = null;
	    HttpURLConnection con = null;
	    
	    try {
	        uri = new URI(url);
	        obj = uri.toURL();
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new Exception("Error al construir la URL.");
	    }

	    if (uri != null && obj != null) {
	        con = (HttpURLConnection) obj.openConnection();
	    }

	    // Configuración de la conexión HTTP
	    con.setRequestMethod("POST"); // Método HTTP POST
	    con.setRequestProperty("Content-Type", "application/json");
	    con.setRequestProperty("User-Agent", "Mozilla/5.0");
	    con.setDoOutput(true); // Habilita el envío de datos en el cuerpo de la solicitud

	    // Crea el cuerpo de la solicitud en formato JSON
	    String requestBody = String.format("{\"email\":\"%s\"}", email);

	    // Envía los datos al servidor
	    try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
	        wr.writeBytes(requestBody);
	        wr.flush();
	    }

	    // Lee la respuesta del servidor
	    int responseCode = con.getResponseCode();
	    if (responseCode != 200) {
	        throw new Exception("Error en la solicitud: " + responseCode);
	    }

	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();

	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }

	    in.close();

	    // Devuelve el resultado procesado
	    return parseResult(response.toString());
	}
	
	private String parseResult(String inputJson) throws Exception {
		/*
		 * inputJson for "sentence" translated from langFrom to langTo
		 * [[["result","sentence",,,1]],,"langFrom"] We have to get 'translated
		 * sentence' from this json.
		 */
		JSONArray jsonArray = new JSONArray(inputJson);
		JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
		JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);

		return jsonArray3.get(0).toString();
	}
}
