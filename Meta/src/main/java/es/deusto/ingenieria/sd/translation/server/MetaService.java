package es.deusto.ingenieria.sd.translation.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class MetaService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;

	private static String DELIMITER = "#";
	DataChecker dC;

	public MetaService(Socket socket) {
		dC = new DataChecker();
		dC.inicializarEmails();
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
				respuesta = logIn(email, password); // llama al metodo necesario aqui
				System.out.println(
						"   - MetaService - Datos enviados de login '" + tcpSocket.getInetAddress().getHostAddress()
								+ ":" + tcpSocket.getPort() + "' -> '" + respuesta + "'");
				this.out.writeUTF(respuesta);

			} else if (data.matches("[^@]+@[^@]+\\.[^@]+")) {
				// registro
				respuesta = register(data); // llama al metodo necesario aqui
				System.out.println(
						"   - MetaService - Datos enviados de registro '" + tcpSocket.getInetAddress().getHostAddress()
								+ ":" + tcpSocket.getPort() + "' -> '" + respuesta + "'");
				this.out.writeUTF(respuesta);
			} else {
				this.out.writeUTF("Error: Formato de datos no válido.");
				System.out.println("   - MetaService - Formato de datos no válido: " + data);
			}
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

	private String logIn(String email, String contraseña) {
		String respuesta = "";
		String comprobacion = "OK";

		if (email != null && contraseña != null) {

			try {
				respuesta = dC.logIn(email, contraseña);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (respuesta.equals(comprobacion)) {
			return "OK";
		}
		return "ERR";
	}

	private String register(String email) {
		String respuesta = "";
		String comprobacion = "OK";

		if (email != null) {

			try {
				respuesta = dC.register(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (respuesta.equals(comprobacion)) {
			return "OK";
		}
		return "ERR";
	}
}