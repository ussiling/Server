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
 * Servern skapar en egen session till varje användare som kopplar på sig. 
 * Från den session läggs användarnament och en User i en hashmap i denna klassen.
 * 
 * @author Ussi
 */

public class Server {
	private int PORT = 24000;
	private ServerSocket socket;
	private User user;
	
	//Tanken är just nu att man har användarnamn som nyckel och hela objektet som värde.
	//printMap iterear hela - finns längst ner.
	public static HashMap<String, User> userMap = new HashMap<String, User>();

	/**
	 * Startar tråden och sätter igång lyssnaren
	 */
	public Server() {
		startSocket();
		ConnectionListener listener = new ConnectionListener();
		new Thread(listener).start();
		new DatabaseConnector();
		
	}

	/**
	 * Skapar en socket som lyssnar pÂ en specifik port.
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
	 * Lyssnar pÂ inkommande enheter som vill koppla upp sig och hanterar varje anslutning i en egen tråd/session..
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
						//Läser av vad som skickas
						if ((inputLine = d.readLine()) != null) {
							System.out.println("SERVER fˆrsta som skickas = " + inputLine);
							
							/**
							 * Om det servern får in är CONNECT ska den öppna en ny anslutning med en viss användare. 
							 */
							if(inputLine.equals(Protocol.CONNECT)){
								System.out.println("SERVER CONNECT");
								Session conn2 = new Session(clientSocket);
								new Thread(conn2).start();
							}
							/**
							 * Reconnect får även ett användarnamn och hittar den i hashmapen och skickar 
							 * även USER objektet till den användaren med i den skapade session
							 */
							else if(inputLine.equals(Protocol.RECONNECT)){
								//Efter den har fått RECONNECT måste den få användarnamnet.
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