package server;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.*;

/**
 *
 * 
 * Generator sezeni pro klienty
 * @author babu
 **/
public class Session extends Thread
{  
   private Server server = null;
   private Socket socket = null;
   private int clientID = -1;
   private ObjectInputStream  streamIn;
   private ObjectOutputStream streamOut;
   private ServerDP parser = null;

   public Session(Server server,Socket socket,int clientId) {
      this.streamIn = null;
      this.streamOut = null;
      this.server = server;
      this.socket = socket;
      this.clientID = clientId;
      this.parser = server.parser;
   }
   
   public void send(DataUnit msg) {
       try {
           this.streamOut.writeObject(msg);
           this.streamOut.flush();
       }
       catch(IOException ioe) {
           System.out.println(this.clientID + " ERROR sending: " + ioe.getMessage());
           server.remove(this.clientID);
           stop();
       }
   }
   
   public int getID() {  
       return this.clientID;
   }
   
   @Override
   public void run() {
       System.out.println("Server Thread " + this.clientID + " running.");
       while (true) {
           try {
               server.dataHandler(this.clientID, (DataUnit) streamIn.readObject());
            }
            catch(IOException ioe){
                System.out.println(this.clientID + " ERROR reading: " + ioe.getMessage());
                server.remove(this.clientID);
                stop();
            } catch (ClassNotFoundException cnfe) {
               System.out.println(this.clientID + " Programming ERROR: " + cnfe.getMessage());
           }
        }
   }
   
   public void open() throws IOException {  
       streamOut = new ObjectOutputStream(socket.getOutputStream());
       streamOut.flush();
       streamIn = new ObjectInputStream(socket.getInputStream());
       send(new DataUnit("Client is here",DataUnit.MsgID.C_HELLO));
   }
   
   public void close() throws IOException {  
    send(new DataUnit("Client ending",DataUnit.MsgID.C_UNAV));  
       if (socket != null)
       socket.close();
      if (streamIn != null)
          streamIn.close();
      if (streamOut != null)
          streamOut.close();
   }
}
