package es.deusto.sd.strava.external;

import java.io.*;
import java.net.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MetaServiceGateway implements ServiceGateway {
    private String serverIP = "127.0.0.1";
    private int serverPort = 7600;
	private static final Logger logger = LoggerFactory.getLogger(MetaServiceGateway.class);
	
    private String sendMessage(String message) {
        String response = null;

        try (Socket socket = new Socket(serverIP, serverPort);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
        	
			logger.info("Cliente iniciado.");
            out.writeUTF(message);
            out.flush();

            response = in.readUTF();
			logger.info("Respuesta del servidor: " + response);

        } catch (IOException e) {
			logger.error("Error al comunicarse con el servidor: " + e.getMessage());	
        }

        return response;
    }

    @Override
    public boolean verifyEmail(String email) {
        String response = sendMessage(email);

        if ("OK".equalsIgnoreCase(response.trim())) {
            return true;
        } else {
			logger.error("Error al verificar email: " + response);
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
			logger.error("Error al validar login: " + response);
            
            return false;
        }
    }
}
