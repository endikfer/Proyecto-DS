package es.deusto.ingenieria.sd.translation.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MetaService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;
	private Map<String, String> emails = new HashMap<>();
	
	private static String DELIMITER = "#";

	public MetaService(Socket socket) {
		try {
			this.tcpSocket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.err.println("# MetaService - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		try {
			String data = this.in.readUTF();
			String respuesta;
			System.out.println("   - MetaService - Datos recibidos de '" + tcpSocket.getInetAddress().getHostAddress()
					+ ":" + tcpSocket.getPort() + "' -> '" + data + "'");

			if (data.contains("#")) {
				// Login
				StringTokenizer tokenizer = new StringTokenizer(data, DELIMITER);
				String email = tokenizer.nextToken();
				String password = tokenizer.nextToken();
				respuesta = LogIn(email, password); // llama al metodo necesario aqui
				this.out.writeUTF(respuesta);

			} else if (data.matches("[^@]+@[^@]+\\.[^@]+")) {
				// registro
				respuesta = Register(data); // llama al metodo necesario aqui
				this.out.writeUTF(respuesta);
			} else {
				this.out.writeUTF("Error: Formato de datos no válido.");
				System.out.println("   - MetaService - Formato de datos no válido: " + data);
			}

			this.out.writeUTF(data);
			System.out.println("   - MetaService - datos enviados a " + tcpSocket.getInetAddress().getHostAddress()
					+ ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");

			/*
			 * if (loginWithMeta(data)) {
			 * this.out.writeUTF("Login exitoso. Tu token de sesión es: TOKEN");
			 * System.out.println("   - MetaService - Login exitoso. Token enviado: TOKEN");
			 * } else { this.out.writeUTF("Error de autenticación con Meta."); System.out.
			 * println("   - MetaService - Error de autenticación para el email: " + data);
			 * }
			 */
		} catch (EOFException e) {
			System.err.println("   # MetaService - TCPConnection EOF error" + e.getMessage());
		} catch (IOException e) {
			System.err.println("   # MetaService - TCPConnection IO error:" + e.getMessage());
		} finally {
			try {
				System.out.println("Cliente cerrado.");
				tcpSocket.close();
			} catch (IOException e) {
				System.err.println("   # MetaService - TCPConnection IO error:" + e.getMessage());
			}
		}

	}

	/*
	 * private boolean loginWithMeta(String email/*, String password) { String
	 * accessToken = authenticateWithMetaAPI(email/*, password); return accessToken
	 * != null && !accessToken.isEmpty(); }
	 * 
	 * 
	 * private String authenticateWithMetaAPI(String email/*, String password) { if
	 * (email.contains("@meta.com")/* && password.equals("12345")){ return
	 * "fake_facebook_access_token"; } return null; }
	 */

	private String LogIn(String email, String contraseña) {
		String respuesta = null;

		if (email != null && contraseña != null) {
			
			DataChecker DC = new DataChecker();
			try {
				respuesta = DC.LogIn(email, contraseña);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (respuesta == null) ? "ERR" : "OK";

	}

	private String Register(String email) {
		String respuesta = null;

		if (email != null) {
			
			DataChecker DC = new DataChecker();
			try {
				respuesta = DC.Register(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (respuesta == null) ? "ERR" : "OK";
	}
}