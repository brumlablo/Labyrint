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
    private int playerID = 0;
    private ArrayList <GameSession> gameRooms = null;
    
    public int getPort() {
        return port;
    }
    
    public Server() {  
        this.players = new ArrayList <Session>();
        this.gameRooms = new ArrayList <GameSession>();
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
    
    private GameSession findRoom(int ID) {
        for(GameSession room : gameRooms) {
            if (room.getRoomID() == ID)
                return room;
        }
        return null;
    }

    public synchronized void dataHandler(int ID, DataUnit toParse) {
       /*if (input.equals(".bye")) {  
           players.get(findClient(ID)).send(".bye");
           remove(ID);
       }
       else {*/
        if(toParse.objCode.getCode() < 21 ) //nejedna se o zpravu pro server
            return;
        DataUnit toSend = null;
        int autorID = findClient(ID);
        Session autor = null;
        if(autorID > 0) { //pokud client ID neexistuje, nacitame dal
            autor = players.get(autorID);
        }
        else {
            return;
        }
        GameSession tmpgs = null;
        String who = autor.getID() + ""; // pro vypisy
        switch(toSend.objCode) {// zeptat se pavla na IP adresu na pripojeni ke hre
            /*----------------------------------------------------------------*/
            case C_HELLO: { //bez na cekacku
                System.out.println( who + ": " + (String)toParse.data);
                toSend = new DataUnit(1,toSend.objCode.S_LOBBY);
                autor.send(toSend);
                break;
            }
            /*----------------------------------------------------------------*/
            case C_OK_LOBBY: {
                    boolean ready = false;               
                    for (Session client : players) { /*pokud je v lobby vic jak dva klientu, je mozne vyzvat hrace*/
                        if(client.getClientState() == Session.PlState.INLOBBY) {
                            ready = true;
                            if(client.getID() != autor.getID())
                                client.send(new DataUnit(client.getID(),toSend.objCode.S_CLOBBY));
                            //+vsem ostatnim v lobby se vypise pripojeny klient
                            //break;
                        }     
                    }
                    autor.setClientState(Session.PlState.INLOBBY);
                    for (Session client : players) {
                        if(client.getClientState() == Session.PlState.INLOBBY) { //klienti v lobby
                            if(ready)
                                toSend = new DataUnit(true,toSend.objCode.S_READY);
                            else
                                toSend = new DataUnit(false,toSend.objCode.S_READY); //server neni ready na vyzvani
                            client.send(toSend);
                        }
                    }
                    ready = false;
                break;
            }   
            /*----------------------------------------------------------------*/
            case C_CHALLPL: { //v objektu dostanu pole int s ids
                autor.setClientState(Session.PlState.INGAME);
                ArrayList<Integer> clientIDs = (ArrayList<Integer>) toParse.data; //autor posle IDs hracu na vyzvani
                int foundID = 0; //hledani hraci k vyzvani
                GameSession gs = new GameSession(); //nove herni sezeni
                gs.addPlayer(autor);
                gs.addReady();
                for(int i = 0; i < clientIDs.size() ; i++) { //najdi vyzvane klienty v lobby
                    foundID = findClient(clientIDs.get(i)); //najdi klienta s jeho id // JE TO OK??
                    if((foundID >= 0) && (players.get(foundID).getClientState() == Session.PlState.INLOBBY)) {
                        gs.addPlayer(players.get(foundID));
                        toSend = new DataUnit(true,toSend.objCode.S_READYFG); //server ready for game
                        players.get(foundID).send(toSend);
                    }
                    else {
                        toSend = new DataUnit(false,toSend.objCode.S_READYFG); //server not ready for game
                        autor.send(toSend);
                        autor.setClientState(Session.PlState.INLOBBY);
                        //gs.multicast(toSend,false); //ostatnim doted nabranym rozeslu, ze nejsme ready
                        gs.destroyer();
                        break;
                    }
                }
                gameRooms.add(gs);
                break;
            }
            /*----------------------------------------------------------------*/
            case C_RESP_CHALLPL: { //dojde mi odpoved, autor je v mistnosti
                boolean resp = (boolean) toParse.data; //prijal nebo ne?
                tmpgs = findRoom(autor.getID());
                if(tmpgs == null) { //pokud client ID neexistuje, nacitame dal
                    break;
                }
                else {
                    if(!resp) {
                        tmpgs.multicast(new DataUnit(false,toSend.objCode.S_READYFG),false);
                        for(Session player : tmpgs.getRoommates()) {
                            player.setClientState(Session.PlState.INLOBBY);
                        }
                        tmpgs.destroyer();
                        break;
                    }
                    //NOVA HRA
                    tmpgs.addReady();
                    if(tmpgs.isAllReady()) {
                        //tady je potreba poslat autorovi vyber hry
                        Session leader = tmpgs.getLeader();
                        if(leader == null) { //vysoce nepravdepodobne
                            tmpgs.destroyer();
                            break;
                        }
                        else {
                            //vsichni uz jsou ve hre
                            ArrayList<Session> gsPlayers = tmpgs.getRoommates();
                            for(Session player : gsPlayers) {
                                player.setClientState(Session.PlState.INGAME);
                                //tady by se uz nemeli vykreslovat v lobby
                            }
                            toSend = new DataUnit("",toSend.objCode.S_CHOOSEG); //leader bude na klientove strane vybirat hru
                            leader.send(toSend);
                            toSend = new DataUnit(1,toSend.objCode.S_WAITFG);
                            tmpgs.multicast(toSend,true);
                        }
                    }
                    break;
                }
            }
            /*----------------------------------------------------------------*/
            case C_CHOSENG: { //nova nebo ulozena hra, inicializace
                int newgame [] = (int []) toParse.data; //nova hra - v poli parametry N a K; ulozene hry: pole s [-1][-1]
                
                tmpgs = findRoom(autor.getID());
                if(tmpgs == null) { //pokud client ID neexistuje, nacitame dal
                    break;
                }
                else {
                    //leader chce videt seznam ulozenych her
                    if((newgame[0] == -1)&& (newgame[1] == -1)) {
                        //vypis ulozene hry
                        //tmpgs = nalezena hra;
                        toSend = new DataUnit("seznam ulozenych her",toSend.objCode.S_SHOWGS);
                        autor.send(toSend); //leaderovi seznam
                        toSend = new DataUnit(1,toSend.objCode.S_WAITFG); //ostatni dale cekaji
                        tmpgs.multicast(toSend,true);
                        break;
                    }
                    else {
                        //nova hra
                        tmpgs.loader(newgame[0],newgame[1],null); //N,K,savedgame=null;
                        break;
                    }
                }
            }
            /*----------------------------------------------------------------*/            
            case C_LOADSG: {
                //prisel mi objekt??? nebo ID ulozene hry, jak to propojit s GameSession
                tmpgs = findRoom(autor.getID());
                if(tmpgs == null) { //pokud client ID neexistuje, nacitame dal
                    break;
                }
                else {
                GameStats savedGame = (GameStats) toParse.data;
                savedGame.setRoomID(tmpgs.getRoomID());
                tmpgs.loader(0,0,savedGame);
                break;
                }
            }
            /*----------------------------------------------------------------*/
            case C_SHIFT: { //klient mi poslal kam vlozil orotovanou FC, server mu nabidne dirs, ostatni zobrazeni casti tahu
                 break;
            }
            /*----------------------------------------------------------------*/
            case C_MOVE: { //vzal poklad?
                 break;
            }
            /*----------------------------------------------------------------*/
            case C_UNAV: {
                //pokud bude v mistnosti mene jak dva hraci, poslu je do lobby
                System.out.println( who + ": " + (String)toParse.data);
                break;
            }
            /*----------------------------------------------------------------*/
            default:
                players.get(findClient(ID)).send(new DataUnit("OK",DataUnit.MsgID.DENIED));

        }
        toSend.data = ID + ": " + toParse.data;
        System.out.println("------------------s------------------");
        System.out.println(toParse.data);
        System.out.println(toSend.data);
        System.out.println("------------------s------------------");
        
        for (Session client : players) //prozatim rozesilam vsem
            client.send(toSend);
        //players.get(findClient(ID)).send(toSend); //nebo i jednomu
    }
    
    public synchronized void remove(int ID) {  
        int pos = findClient(ID);
        Session threadExitus = players.get(pos);
        players.remove(pos);
        System.out.println("Removing client thread " + ID + " at " + pos);
        //playerCount--;
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
           // playerCount++;
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
