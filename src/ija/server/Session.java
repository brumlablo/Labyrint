/* file name  : Session.java
 * authors    : xblozo00, xhajek33
 */

package ija.server;

import java.net.*;
import java.io.*;
import ija.shared.*;

/**
 * Generator sezeni pro klienty
 * @author xblozo00
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

   /**
    * Dostava server,socket a ID clienta ze serveru
    * @param server nas server
    * @param socket schranka pro klienta
    * @param clientId ID clienta na serveru
    */
   public Session(Server server,Socket socket,int clientId) {
      this.server = server;
      this.socket = socket;
      this.clientID = clientId;
   }
   
   /**
    * Odesila data ze serveru
    * @param msg zprava
    */
   public void send(DataUnit msg) {
       try {
           this.streamOut.reset();
           this.streamOut.writeObject(msg);
           this.streamOut.flush();
       }
       catch(IOException ioe) {
           System.err.println("Client " + this.clientID + ": ERROR sending: " + ioe.getMessage());
           server.remove(this.clientID);
           stop();
       }
   }
   
   /**
    * Vraci ID hrace
    * @return ID hrace 
    */
   public int getID() {  
       return this.clientID;
   }
   
   /**
    * Vraci cislo jednoho herniho sezeni, "pokoje"
    * @return roomID ID pokoje na serveru
    */
   public int getRoomID() {
      return this.roomID; 
    }

   /**
    * Vraci stav hrace
    * @return clientState, tedy stav hrace
    */
    public PlState getClientState() {
        return clientState;
    }

    /**
     * Nastaveni ID pokoje
     * @param roomID ID pokoje na serveru
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Nastaveni stavu hrace
     * @param clientState stav hrace
     */
    public void setClientState(PlState clientState) {
        this.clientState = clientState;
    }

    /**
     * Spusteni vlakna
     */
   @Override
   public void run() {
       //System.out.println("Server Thread " + this.clientID + " running.");
       while (true) {
           try {
               server.dataHandler(this.clientID, (DataUnit) streamIn.readObject());
            }
            catch(IOException ioe){
                System.err.println("Client " + this.clientID + ": ERROR reading: " + ioe.getMessage());
                server.remove(this.clientID);
                stop();
            } catch (ClassNotFoundException cnfe) {
               System.err.println("Client " + this.clientID + ": Programming ERROR: " + cnfe.getMessage());
           }
        }
   }
   
   /**
    * Otevreni vstupnich a vystupnich streamu
    * @throws IOException 
    */
   public void open() throws IOException {  
       streamOut = new ObjectOutputStream(socket.getOutputStream());
       streamOut.flush();
       streamIn = new ObjectInputStream(socket.getInputStream());
    }
   
   /**
    * Ukonceni vstupnich a vystupnich streamu
    * @throws IOException 
    */
   public void close() throws IOException {  
       if (socket != null)
       socket.close();
      if (streamIn != null)
          streamIn.close();
      if (streamOut != null)
          streamOut.close();
   }
}
