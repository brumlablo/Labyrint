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
 * @author babu
 */

/**
 *
 * 
 * generator sezeni pro klienty
 **/
public class ServerThread extends Thread
{  
   private Server server    = null;
   private Socket socket    = null;
   private int ID        = -1;
   private DataInputStream  streamIn  =  null;
   private DataOutputStream streamOut = null;

   public ServerThread(Server server, Socket socket,int clientId)
   {
      this.server = server;
      this.socket = socket;
      ID     = clientId;
   }
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   public int getID()
   {  
       return ID;
   }
   @Override
   public void run()
   {  System.out.println("Server Thread " + ID + " running.");
      while (true)
      {  try
         {  server.handle(ID, streamIn.readUTF());
         }
         catch(IOException ioe)
         {  System.out.println(ID + " ERROR reading: " + ioe.getMessage());
            server.remove(ID);
            stop();
         }
      }
   }
   public void open() throws IOException
   {  
       streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
       streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
   }
   
   public void close() throws IOException
   {  
      if (socket != null)
       socket.close();
      if (streamIn != null)
          streamIn.close();
      if (streamOut != null)
          streamOut.close();
   }
}
