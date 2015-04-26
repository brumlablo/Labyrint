package server;

import java.net.*;
import java.io.*;
/**
 *
 * @author babu
 */

/* TODO: arraylist clientid pro prideleni barvy, ZPRAVA jako posilany serializovany OBJEKT s id a obsahem, v serveru bude "parser" id ser. objektu,
data(in/out)putstream na objectinoutdatastream cosy */

public class Server implements Runnable
{
    private ServerSocket server = null;
    private Thread thread = null;
    private ServerThread clients[] = new ServerThread[50];
    private int port = 0;
    private int clientNum = 0;
    private int clientId = 0;

    public int getPort() {
        return port;
    }
    public Server()
    {  
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
       {  thread.stop(); 
          thread = null;
       }
    }
    private int findClient(int ID)
    {  for (int i = 0; i < clientNum; i++)
          if (clients[i].getID() == ID)
             return i;
       return -1;
    }

    
    public synchronized void handle(int ID, String input)
    {  if (input.equals(".bye"))
       {  clients[findClient(ID)].send(".bye");
          remove(ID); }
       else
          for (int i = 0; i < clientNum; i++)
             clients[i].send(ID + ": " + input);
       System.out.println(ID + ": " + input);
    }
    
    public synchronized void remove(int ID)
    {  int pos = findClient(ID);
       if (pos >= 0)
       {  
          ServerThread toTerminate = clients[pos];
          System.out.println("Removing client thread " + ID + " at " + pos);
          if (pos < clientNum-1)
             for (int i = pos+1; i < clientNum; i++)
                clients[i-1] = clients[i];
          clientNum--;
          try
          {  
              toTerminate.close();
          }
          catch(IOException ioe)
          {  System.out.println("Error closing thread: " + ioe); }
             toTerminate.stop();
          }
    }

    private void addThread(Socket socket)
    {  if (clientNum < clients.length)
       {  System.out.println("Client accepted: " + socket);
          this.clientId++;
          clients[clientNum] = new ServerThread(this, socket,this.clientId);
          try
          {  clients[clientNum].open(); 
             clients[clientNum].start();  
             clientNum++; }
          catch(IOException ioe)
          {  System.out.println("Error opening thread: " + ioe); } }
       else
          System.out.println("Client refused: maximum " + clients.length + " reached.");
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
