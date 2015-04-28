package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import shared.*;


/**
 * Trida reprezentujici asynchronni server
 * @author babu
 */
public class Server implements Runnable
{
    private ServerSocket server = null;
    private int port = 0;
    private Thread thread = null;
    private ArrayList <Session> players;
    private int playerCount = 0;
    private int playerID = 0;
    protected ServerDP parser = null;

    public int getPort() {
        return port;
    }
    
    public Server() {  
        this.players = new ArrayList <Session>();       
        try {  
            this.port = 12345;
            System.out.println("Binding to port " + port + ", please wait  ...");

            server = new ServerSocket(); //novy socket
            InetSocketAddress sAddr = new InetSocketAddress("127.0.0.1", port);
            server.bind(sAddr);

            System.out.println("Server started: " + server);
            start();
        }
        catch(IOException ioe) {  
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }
    
    public void run() {
        while (thread != null) {
            try {  
                System.out.println("Waiting for a client ..."); 
                newSession(server.accept()); }
            catch(IOException ioe) {  
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void start() {  
        if (thread == null) {  
            thread = new Thread(this); 
            thread.start();
       }
    }
    
    public void stop() {
        if (thread != null) {  
           thread.stop(); 
           thread = null;
       }
    }
    
    private int findClient(int ID) {  
        for(int i = 0; i < players.size() ; i++)
            if (players.get(i).getID() == ID)
                return i;
       return -1;
    }

    
    public synchronized void dataHandler(int ID, DataUnit toParse) //preposilani dat VSEM klientum, identifikace na zaklade ID clienta
    {  
       /*if (input.equals(".bye")) {  
           players.get(findClient(ID)).send(".bye");
           remove(ID);
       }
       else {*/
        DataUnit toSend = parser.parse(toParse, playerID);
        toSend.data = ID + ": " + toParse.data;
        System.out.println("------------------sh-----------------");
        System.out.println(toParse.data);
        System.out.println(toSend.data);
        System.out.println("------------------sh-----------------");
        
        for (Session client : players) //prozatim rozesilam vsem
            client.send(toSend);
        //players.get(findClient(ID)).send(toSend); //nebo i jednomu
    }
    
    public synchronized void remove(int ID) {  
        int pos = findClient(ID);
        Session threadExitus = players.get(pos);
        players.remove(pos);
        System.out.println("Removing client thread " + ID + " at " + pos);
        playerCount--;
        try {  
            threadExitus.close();
        }
        catch(IOException ioe) {  
            System.out.println("Error closing thread: " + ioe);
        }
        
        threadExitus.stop();
    }

    private void newSession(Socket socket) {  
        System.out.println("Client accepted: " + socket);
        playerID++;
        Session newPlayer = new Session(this, socket,playerID);
        players.add(newPlayer);
        try {
            newPlayer.open(); 
            newPlayer.start();  
            playerCount++;
        }
        catch(IOException ioe) {  
            System.out.println("Error opening thread: " + ioe);
        }
    }     
  /**
  * @param args the command line arguments
  */
   public static void main(String args[]) {  
      Server mujserver = null;
      mujserver = new Server();
   }
}
