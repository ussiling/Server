package com.server.aqordify;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
//import javazoom.jl.player.*;
//import org.apache.commons.io.IOUtils;


public class Recieve {

public Recieve() throws IOException
{
    //create file
    File file = new File("src/mario_bros.mp3");
    FileInputStream fin = new FileInputStream(file);
     
//connect to server
//open output and input streams
    System.out.println("Connecting to server...");
    Socket socket = new Socket("192.168.43.250", 24000);
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));     
    
//send image to server
    byte[] readData = new byte[1024];
    int l = (int)file.length();
    System.out.println("Sending image (" + l + " B)...");
    out.writeInt(l); //send file length to server
    int i;
    
    while((i = fin.read(readData)) != -1){
        out.write(readData, 0, i);
        System.out.println(i + "B sent");
    }
    
    System.out.println("Image sent");
    fin.close();
     
//receive reply from server
    System.out.println("Waiting for server reply...");
    String plate = in.readLine();
    System.out.println("FROM SERVER: " + plate);
     
//close connection to server
    in.close();
    out.close();
    socket.close();

}

public static void main(String[] args) throws IOException {
	Recieve r = new Recieve();
	
}
}

