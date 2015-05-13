/* file name  : ClientHelper.java
 * authors    : xblozo00, xhajek33
 */

package ija.client;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ija.shared.*;


/**
 * Trida pro spravu nacitani vstupu(pro jednoho klienta) ze serveru
 * @author babu
 */
public class ClientHelper extends Thread {  
    private Socket socket = null;
    private Client client = null;
    private ObjectInputStream streamIn = null;

    /**
     * Inicializace nacitani
     * @param client klient
     * @param socket schranka pro klienta
     */
    public ClientHelper(Client client, Socket socket) {
        this.client   = client;
        this.socket   = socket;
        open();  
        start(); //start vlakna Thread
    }
    
    /**
     * Otevreni vstupu pro klienta
     */
    public void open() {  
        try {
            streamIn  = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException ioe) {  
            System.err.println("Error getting input stream: " + ioe);
            client.stop();
        }
    }
    
    /**
     * Uzavreni vstupu pro klienta
     */
    public void close() {  
       try {  
           if (streamIn != null)
               streamIn.close();
        }
        catch(IOException ioe) {
            System.err.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run() {  
       while (true) { 
           try {
               client.dataHandler((DataUnit) streamIn.readObject());
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
