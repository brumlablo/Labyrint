package client;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 * 
 * @author babu
 */
public class Client
{  private Socket socket              = null;
   private Thread thread              = null;
   private DataInputStream  console   = null;
   private DataOutputStream streamOut = null;
   private ClientThread client = null;

   public Client()
   {  System.out.println("Establishing connection. Please wait ...");
      try
      {  socket = new Socket("127.0.0.1", 12345);
         System.out.println("Connected: " + socket);
         start();
      }
      catch(UnknownHostException uhe)
      {  System.out.println("Host unknown: " + uhe.getMessage());
      }
      catch(IOException ioe)
      {  System.out.println("Unexpected exception: " + ioe.getMessage());
      }
   }
   
   /**
    * nacteni neceho, notifikace na event pro handle
    * @param msg 
    */
   public void handle(String msg)
   {  if (msg.equals(".bye"))
      {  System.out.println("Good bye. Press RETURN to exit ...");
         stop();
      }
      else
         System.out.println(msg);
   }
  
   
   public void start() throws IOException
   {  console   = new DataInputStream(System.in);
      streamOut = new DataOutputStream(socket.getOutputStream());
      client = new ClientThread(this, socket);                  
   }
   
   public void stop()
   {  try
      {  if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ..."); }
      client.close();  
      client.stop();
   }
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  
          stop();
       }
   }
    public static void main(String args[])
     {  
        Client mujclient = null;
        mujclient = new Client();
        Scanner in = new Scanner(System.in);
        while(true)
        {
            mujclient.send(in.nextLine());
        }
      }
}
