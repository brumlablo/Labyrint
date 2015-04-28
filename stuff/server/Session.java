/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.net.*;
import java.io.*;

/**
 *
 * 
 * Generator sezeni pro klienty
 * @author babu
 **/
public class Session extends Thread
{  
   private Server server    = null;
   private Socket socket    = null;
   private int clientID        = -1;
   private DataInputStream  streamIn  =  null;
   private DataOutputStream streamOut = null;

   public Session(Server server, Socket socket,int clientId)
   {
      this.server = server;
      this.socket = socket;
      clientID     = clientId;
   }
   
   public void send(String msg) {
       try {
           streamOut.writeUTF(msg);
           streamOut.flush();
       }
       catch(IOException ioe) {
           System.out.println(clientID + " ERROR sending: " + ioe.getMessage());
           server.remove(clientID);
           stop();
       }
   }
   
   public int getID() {  
       return clientID;
   }
   
   @Override
   public void run() {
       System.out.println("Server Thread " + clientID + " running.");
       while (true) {
           try {
               server.handle(clientID, streamIn.readUTF());
            }
            catch(IOException ioe)
            {  System.out.println(clientID + " ERROR reading: " + ioe.getMessage());
               server.remove(clientID);
               stop();
            }
        }
   }
   
   public void open() throws IOException {  
       streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
       streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
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
