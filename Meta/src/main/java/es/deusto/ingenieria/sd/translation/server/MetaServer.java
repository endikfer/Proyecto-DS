package es.deusto.ingenieria.sd.translation.server;

import java.io.IOException;
import java.net.ServerSocket;

public class MetaServer {
	
	private static int numClients = 0;
	
	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println(" # Usage: MetaServer [PORT]");
			System.exit(1);
		}
		
		//args[1] = Server socket port
		int serverPort = Integer.parseInt(args[0]);
		
		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println("Servidor abierto.");
			System.out.println(" - MetaServer: esperando conexiones '" + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort() + "' ...");
			
			while (true) {
				new MetaService(tcpServerSocket.accept());
				System.out.println(" - MetaServer: Nueva conexión de cliente aceptada. Número de cliente: " + ++numClients);
			}
		} catch (IOException e) {
			System.err.println("# MetaServer: IO error:" + e.getMessage());
		}
	}
}