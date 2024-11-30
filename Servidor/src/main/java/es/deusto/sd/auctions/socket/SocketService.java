package es.deusto.sd.auctions.socket;

import java.io.*;
import java.net.*;

public class SocketService {

	private String serverIP;
	private int serverPort;

	public SocketService(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}

	public String sendMessage(String message) {
		String response = null;
		//String mens="Hola desde el cliente";

		try (Socket socket = new Socket(serverIP, serverPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			System.out.println("Cliente iniciado.");
			
			

			// Enviar mensaje al servidor
			out.writeUTF(message);
			out.flush();

			// Leer respuesta del servidor
			response = in.readUTF();
			System.out.println("Respuesta del servidor: " + response);

		} catch (IOException e) {
			System.err.println("Error al comunicarse con el servidor: " + e.getMessage());
		}

		return response;
	}
}
