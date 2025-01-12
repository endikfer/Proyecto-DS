package es.deusto.sd.strava.external;

import java.io.*;
import java.net.*;

import org.springframework.stereotype.Component;

@Component
public class MetaServiceGateway implements ServiceGateway {
    private String serverIP = "127.0.0.1";
    private int serverPort = 7600;

    private String sendMessage(String message) {
        String response = null;

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

    @Override
    public boolean verifyEmail(String email) {
        String response = sendMessage(email);

        if ("OK".equalsIgnoreCase(response.trim())) {
            return true;
        } else {
            System.err.println("Error al verificar email: " + response);
            return false;
        }
    }

    @Override
    public boolean validateLogin(String email, String password) {
        String message = email + "#" + password;
        String response = sendMessage(message);

        if ("OK".equalsIgnoreCase(response.trim())) {
            return true;
        } else {
            System.err.println("Error al validar login: " + response);
            return false;
        }
    }
}
