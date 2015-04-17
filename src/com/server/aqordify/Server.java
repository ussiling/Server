package com.server.aqordify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Servern skapar en egen session till varje anv�ndare som kopplar p� sig. 
 * Fr�n den session l�ggs anv�ndarnament och en User i en hashmap i denna klassen.
 * 
 * @author Ussi
 */

public class Server {
	private int PORT = 24000;
	private ServerSocket socket;
	private User user;
	
	//Tanken �r just nu att man har anv�ndarnamn som nyckel och hela objektet som v�rde.
	//printMap iterear hela - finns l�ngst ner.
	public static HashMap<String, User> userMap = new HashMap<String, User>();

	/**
	 * Startar tr�den och s�tter ig�ng lyssnaren
	 */
	public Server() {
		startSocket();
		ConnectionListener listener = new ConnectionListener();
		new Thread(listener).start();
		new DatabaseConnector();
		
	}

	/**
	 * Skapar en socket som lyssnar p� en specifik port.
	 */
	public void startSocket() {
		try {
			socket = new ServerSocket(PORT); // Server socket - port
			System.out.println("SERVER Server started. Listening to the port " + PORT);
		} catch (IOException e) {
			System.out.println("SERVER Could not listen on port: " + PORT + " androidSocket");
		}
	}

	/**
	 * Lyssnar p� inkommande enheter som vill koppla upp sig och hanterar varje anslutning i en egen tr�d/session..
	 *
	 */
	private class ConnectionListener implements Runnable {

		public ConnectionListener() {
			run();
		}

		public void run() {
			while (true) {
				Socket clientSocket = null;
				try {
					clientSocket = socket.accept();
					clientSocket.getLocalAddress();
					InetAddress inet = clientSocket.getLocalAddress();
					SocketAddress soc = clientSocket.getRemoteSocketAddress();
					System.out.println("SERVER Socket Accepted. Connection established");
//					System.out.println(inet.getHostName());
//					System.out.println(inet.getHostAddress());
					System.out.println(soc);

					String inputLine = null;
					BufferedReader d = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
//					Session conn = new Session(clientSocket);
//					new Thread(conn).start();
						//L�ser av vad som skickas
						if ((inputLine = d.readLine()) != null) {
							System.out.println("SERVER f�rsta som skickas = " + inputLine);
							
							/**
							 * Om det servern f�r in �r CONNECT ska den �ppna en ny anslutning med en viss anv�ndare. 
							 */
							if(inputLine.equals(Protocol.CONNECT)){
								System.out.println("SERVER CONNECT");
								Session conn2 = new Session(clientSocket);
								new Thread(conn2).start();
							}
							/**
							 * Reconnect f�r �ven ett anv�ndarnamn och hittar den i hashmapen och skickar 
							 * �ven USER objektet till den anv�ndaren med i den skapade session
							 */
							else if(inputLine.equals(Protocol.RECONNECT)){
								//Efter den har f�tt RECONNECT m�ste den f� anv�ndarnamnet.
								if ((inputLine = d.readLine()) != null) {
									System.out.println("SERVER RECONNECT username = " + inputLine);
									User user = userMap.get(inputLine);
									Session conn2 = new Session(clientSocket, user);
									new Thread(conn2).start();
								}
							}
					}

				} catch (IOException e) {
					System.out.println("SERVER Accept failed: " + PORT + ", " + e);
					System.exit(1);
				}
			}
		}
	}
	
	public static void printMap(HashMap<String, User> mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public static void main(String[] args) {

		new Server();

	}
}