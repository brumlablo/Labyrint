package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author babu
 */

/* TODO: arraylist clientid pro prideleni barvy !!! PLUS jedinecne id, co se musi ulozit do budoucna,
data(in/out)putstream na objectinoutdatastream cosy */

public class Server implements Runnable
{
    private ServerSocket server = null;
    private int port = 0;
    private Thread thread = null;
    private ArrayList <Session> players;
    private int playerCount = 0;
    private int playerID = 0;
    private ServerDD dcd;

    public int getPort() {
        return port;
    }
    public Server()
    {  
      this.players = new ArrayList <Session>();
      this.dcd = new ServerDD();
      try
      {  
         this.port = 12345;
         System.out.println("Binding to port " + port + ", please wait  ...");
         
         server = new ServerSocket(); //novy socket
         InetSocketAddress sAddr = new InetSocketAddress("127.0.0.1", port);
         server.bind(sAddr);
         
         System.out.println("Server started: " + server);
         start();
      }
      catch(IOException ioe)
      {  
          System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
      }
    }
    
    public void run() {
      while (thread != null)
          {  try
             {  
                System.out.println("Waiting for a client ..."); 
                addThread(server.accept()); }
             catch(IOException ioe)
             {  
                 System.out.println("Server accept error: " + ioe);
                 stop();
             }
          }
       }

    public void start()
    {  
        if (thread == null) {  
            thread = new Thread(this); 
            thread.start();
       }
    }
    
    public void stop()
    {  if (thread != null)
       {  
           thread.stop(); 
           thread = null;
       }
    }
    
    private int findClient(int ID)
    {  for(int i = 0; i < this.players.size() ; i++)
          if (players.get(i).getID() == ID)
             return i;
       return -1;
    }

    
    public synchronized void handle(int ID, String input) //preposilani dat VSEM klientum, identifikace na zaklade ID clienta
    {  
       if (input.equals(".bye"))
       {  
           players.get(findClient(ID)).send(".bye");
           remove(ID);
       }
       else
          for (Session client : players)
             client.send(ID + ": " + input);
       System.out.println(ID + ": " + input);
    }
    
    public synchronized void remove(int ID) {  
        int pos = findClient(ID);
        Session threadExitus = players.get(pos);
        players.remove(pos);
        System.out.println("Removing client thread " + ID + " at " + pos);
        playerCount--;
        try
        {  
            threadExitus.close();
        }
        catch(IOException ioe)
        {  
            System.out.println("Error closing thread: " + ioe);
        }
        
        threadExitus.stop();
    }

    private void addThread(Socket socket)
    {  
        System.out.println("Client accepted: " + socket);
        this.playerID++;
        Session newPlayer = new Session(this, socket,this.playerID);
        players.add(newPlayer);
        try
        {
            newPlayer.open(); 
            newPlayer.start();  
            playerCount++;
        }
        catch(IOException ioe)
        {  
            System.out.println("Error opening thread: " + ioe);
        }
    }     
  /**
  * @param args the command line arguments
  */
   public static void main(String args[])
   {  
      Server mujserver = null;
      mujserver = new Server();
   }
}
