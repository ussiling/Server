package com.server.aqordify;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Session tar hand om allting varje användare vill och kan göra. Hanterar inlogg, registrering och hantering av listorna.
 * 
 * @author Ussi
 */


public class Session implements Runnable {
	private Socket client;
	private DataInputStream datainputstream = null;
	private PrintStream printstream; 
	private BufferedReader buffer;
	private Object correctUser = false;


	public final int PORT = 24000;

	private User user;


	/**
	 * Connect kallar på denna
	 * @param clientSocket - kopplingen
	 */
	public Session(Socket clientSocket) {
		this.client = clientSocket;
		try {
			buffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
			datainputstream = new DataInputStream(new BufferedInputStream(client.getInputStream()));	
			printstream = new PrintStream(new BufferedOutputStream(client.getOutputStream(), 1024), false);

		}catch(IOException e){

			System.out.println(e.getMessage() + " getMessage");

			System.out.println(e.getCause() + " getCause");

		}
	}

	/**
	 * Reconnect ska veta vem användaren är därav en till konstruktor med ett helt User objekt
	 * @param clientSocket - kopplingen
	 * @param user - användarens objekt
	 */
	public Session(Socket clientSocket, User userinfo) {
		this.client = clientSocket;
		this.user = userinfo;
		try {
			buffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
			datainputstream = new DataInputStream(new BufferedInputStream(client.getInputStream()));	
			printstream = new PrintStream(new BufferedOutputStream(client.getOutputStream(), 1024), false);
			System.out.println("SESSION  trying to connect");

		}catch(IOException e){
			System.out.println(e.getMessage() + " getMessage");
			System.out.println(e.getCause() + " getCause");
		}
	}


	@Override
	public void run() {

		/**
		 * Kollar så att den är connectad, om den är så skickar den en bekräftelse till Appen
		 */
		try {
			String inputLine,outputLine;
			System.out.println("SESSION  isConnected = "+client.isConnected());
			if(client.isConnected()){
				printstream.println(Protocol.isCONNECT);
				printstream.flush();
			}

			String userPass[];	   
			String user, pass, code;

			while ((inputLine = buffer.readLine()) != null) {
				System.out.println("SESSION  MSG FROM USER/DEVICE  before IF = " + inputLine);

				/**
				 * Om anvŠndaren ska logga in sŒ  delar den upp strŠngen och kollar med databasen.
				 * Ifall den Šr godkŠnd så returneras ett objekt som ska lŠggas i HashMapen med anvŠndarnamn som key.
				 */
				if(inputLine.startsWith(Protocol.SIGNIN)){
		
					if ((inputLine = buffer.readLine()) != null) {
						System.out.println("SESSION  if SIGNIN = " + inputLine);
						userPass = inputLine.split(",");
						user = userPass[1];
						pass = userPass[2];
						System.out.println("SESSION information" + " " + user + " " + pass); 	

						//correctUser = (boolean) new DatabaseConnector().checkLogin(user, pass); 
						//System.out.println("SESSION did u get it right? = " +correctUser); 	
						//if(correctUser.equals("-1")){

						/**
						 * Om anvŠndaren finns i databasen sŒ returneras all info och jag lŠgger det i 
						 * ett User objekt och sparar det i Servern och skickar det till Appen
						 * 
						 */
						//User u = new User("ussi", "Usman", "Sheikh", "Malmö", "SE", false);
						//Server.userMap.put(user, u);
						//printstream.println(SKICKA USERN);
						//printstream.flush();
						/**
						 * Annars fŒr jag -1 eller nŒgot annat vilket jag vet att user finns inte eller nŒgot Šr fel.
						 */
						//printstream.println("TESTAIGEN);
						//printstream.flush();

						

						//			    		  }
					}
				}
				/**
				 * Om anvŠndaren ska registrera fŒr jag alla parametrar, skickar databasens svar (godkŠnd,anv busy, osv) 
				 * tillbaka till applikationen.
				 */
				if(inputLine.startsWith(Protocol.REGISTER)){
//					if ((inputLine = buffer.readLine()) != null) {
						String registerInfo = inputLine;
						System.out.println("SESSION  REGISTER = " + registerInfo);
						
						/**
						 * Se till att allting Šr ifylld
						 */
						String [] registerUser = registerInfo.split(",");
						String firstName = registerUser[1];
						String lastName = registerUser[2];
						String email = registerUser[3];
						String password = registerUser[4];
						String part = registerUser[5];						
						
						System.out.println(firstName + " " + lastName + " " + email + " " + password + " " + part);
							//Skicka till databasen med en try sats. 
							printstream.println(Protocol.isREGISTER);
							printstream.flush();		
							getuserStartscreen();
//					}
				}

				if(inputLine.startsWith(Protocol.SEARCH)){
					String search = inputLine;
					//Skicka det till servern och vŠnta pŒ svar som du sen skickar vidare till appen med
					//printstream.println(Protocol.isREGISTER);
					//printstream.flush();	
					
				}
				if(inputLine.startsWith(Protocol.GETPLAYLIST)){

				}
				if(inputLine.startsWith("1")){
					System.out.println("ONU");
					send();
				}
				if(inputLine.startsWith("2")){
					System.out.println("DOS ");
					recieve();
				}
				else{
					
					System.out.println("ELSE SEND");
			
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Är ej kopplad "+client.isConnected());
		}
	}
	


	private void getuserStartscreen() {
		
		//HŠmta och skicka en lista pŒ vilka konserer anvŠndaren Šr med i fšr att visa upp det i
		//printstream.println(Protocol.isREGISTER);
		//printstream.flush();
		
	}
	

	/**
	 * Om nu appen har kopplat ifrŒn sŒ kopplar den pŒ igen genom att titta om anvŠndaren finns med i listan som inloggad. 
	 */
	public void reconnectUser(String username){
				System.out.println("SESSION  RECONNECT = " + username);
	
				User u = Server.userMap.get(username);
				System.out.println(u.getFirstName() + " " + u.getLastName());

	}
	
/**
 * 
 * SIGN IN
 * Ska skšta inlogget och "reconnecta" anvŠndaren om den finns med i hashmapen som finns pŒ Server.java
 * Ska lŠgga in anvŠndaren om den loggar in.
 * 
 * REGISTER
 * Kolla sŒ det Šr okej, om okej sŠg till appen. LŠgg INTE in i hashmapen fšr anvŠndaren mŒste sjŠlv logga in igen
 *
 * SEARCH
 * Skicka bara det du fŒr vidare till databasen
 * 
 * 
 * 
 */
	private void send() {
		// TODO Auto-generated method stub
		 // sendfile
		try {
        File myFile = new File ("C:\\Users\\Usman\\Desktop\\WorkSpaceWindow\\Aqordify\\src\\mario_bros.mp3");

        System.out.println("HOSSI + " + " ifexist " + myFile.exists()+ " " + myFile.length());

        byte [] mybytearray  = new byte [(int)myFile.length()];
        System.out.println("HOSSI + " + " size " + mybytearray.length );
        FileInputStream fis = new FileInputStream(myFile);

        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(mybytearray,0,mybytearray.length);
        OutputStream os;

			os = client.getOutputStream();
	
        System.out.println("Sending...");
        os.write(mybytearray,0,mybytearray.length);
        os.flush();
        System.out.println("HOSSI + " + " FLUSHED " );
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public void recieve(){
		 ServerSocket serverSocket = null;

		    try {
		        serverSocket = new ServerSocket(24001);
		    } catch (IOException ex) {
		        System.out.println("Can't setup server on this port number. ");
		    }

		    Socket socket = null;
		    InputStream is = null;
		    FileOutputStream fos = null;
		    BufferedOutputStream bos = null;
		    int bufferSize = 0;

		    try {
		        socket = serverSocket.accept();
		    } catch (IOException ex) {
		        System.out.println("Can't accept client connection. ");
		    }

		    try {
		        is = socket.getInputStream();

		        bufferSize = socket.getReceiveBufferSize();
		        System.out.println("Buffer size: " + bufferSize);
		    } catch (IOException ex) {
		        System.out.println("Can't get socket input stream. ");
		    }

		    try {
		        fos = new FileOutputStream("C:\\Users\\Usman\\Desktop\\Test.mp3");
		        bos = new BufferedOutputStream(fos);

		    } catch (FileNotFoundException ex) {
		        System.out.println("File not found. ");
		    }

		    byte[] bytes = new byte[bufferSize];

		    int count;
		    try {
		    while ((count = is.read(bytes)) > 0) {
		       
					bos.write(bytes, 0, count);

		    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		    bos.flush();
//		    bos.close();
//		    is.close();
//		    socket.close();
//		    serverSocket.close();
		
	}

	

	
}
