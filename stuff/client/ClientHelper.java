package client;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.*;


/**
 * Vlakno nacitani jednoho klienta ze serveru
 * @author babu
 */
public class ClientHelper extends Thread {  
    private Socket socket = null;
    private Client client = null;
    private ObjectInputStream streamIn = null;

    public ClientHelper(Client client, Socket socket) {
        this.client   = client;
        this.socket   = socket;
        open();  
        start(); //start vlakna Thread
    }
    
    public void open() {  
        try {
            streamIn  = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException ioe) {  
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
        }
    }
    
    public void close() {  
       try {  
           if (streamIn != null)
               streamIn.close();
        }
        catch(IOException ioe) {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run() {  
       while (true) { 
           try {
               client.dataHandler((DataUnit) streamIn.readObject()); //nacetl jsem data, jdu na event
            }
           catch(IOException ioe) {  
                System.err.println("Listening error: " + ioe.getMessage());
                client.stop();
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Programming error: " + cnfe.getMessage());
           }
           
         }
    }
}