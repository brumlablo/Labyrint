/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author babu
 */
import java.net.*;
import java.io.*;


public class ClientThread extends Thread
{  
    private Socket socket = null;
    private Client client = null;
    private DataInputStream streamIn = null;

    public ClientThread(Client client, Socket socket)
    {  this.client   = client;
       this.socket   = socket;
       open();  
       start(); //start vlakna Thread
    }
    public void open()
    {  
        try
        {
            streamIn  = new DataInputStream(socket.getInputStream());
        }
        catch(IOException ioe)
        {  
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
        }
   }
   public void close()
   {  
       try
       {  
           if (streamIn != null) streamIn.close();
        }
        catch(IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
   }
   
   @Override
   public void run()
   {  
       while (true)
         { 
           try
            {
               client.handle(streamIn.readUTF()); //nacetl jsem data, jdu na event
            }
           catch(IOException ioe)
            {  
                System.out.println("Listening error: " + ioe.getMessage());
                client.stop();
            }   
         }
   }
}