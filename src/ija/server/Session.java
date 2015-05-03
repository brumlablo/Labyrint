/* file name  : Session.java
 * authors    : xhajek33, xblozo00
 */

package ija.server;

import java.net.*;
import java.io.*;
import ija.shared.*;

/**
 *
 * 
 * Generator sezeni pro klienty
 * @author babu
 **/
public class Session extends Thread
{  
   public static enum PlState {
       INGAME,INLOBBY,IDLE
   };
   private int clientID = -1;
   private PlState clientState = PlState.IDLE;
   private int roomID = -1;
   
   private Server server = null;
   private Socket socket = null;
   private ObjectInputStream  streamIn = null;
   private ObjectOutputStream streamOut = null;

   public Session(Server server,Socket socket,int clientId) {
      this.server = server;
      this.socket = socket;
      this.clientID = clientId;
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
   
   public int getRoomID() {
      return this.roomID; 
    }

    public PlState getClientState() {
        return clientState;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setClientState(PlState clientState) {
        this.clientState = clientState;
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
    }
   
   public void close() throws IOException {  
       if (socket != null)
       socket.close();
      if (streamIn != null)
          streamIn.close();
      if (streamOut != null)
          streamOut.close();
   }
}
