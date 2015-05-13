/* file name  : Server.java
 * authors    : xblozo00, xhajek33
 */
package ija.server;

import ija.shared.board.MazeBoard;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import ija.shared.*;
import ija.shared.board.MazeField;
import ija.shared.board.PathFinder;
import ija.shared.player.Player;
import java.util.Arrays;

/**
 * Trida reprezentujici asynchronni server
 * @author xblozo00
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
    /**
     * Inicializace a nacteni klienta
     */
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
    
    @Override
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
    /**
     * Metoda pro nalezeni klienta/hrace v poli klientu na serveru
     * @param ID ID hrace na serveru
     * @return nazelene ID hrace
     */
    private int findClient(int ID) {  
        for(int i = 0; i < players.size() ; i++)
            if (players.get(i).getID() == ID)
                return i;
       return -1;
    }
    
    /**
     * Metoda pro nalezeni "pokoje" hracu, tedy jednoho herniho sezeni
     * @param ID ID hrace na serveru
     * @return herni sezeni
     */
    private GameSession findRoom(int ID) {
        for(GameSession room : gameRooms) {
            if (room.getRoomID() == ID)
                return room;
        }
        return null;
    }
    
    /**
     * Metoda zajistujici herni logiku na serveru
     * @param ID ID autora zpravy
     * @param toParse zprava na rozparsovani
     */
    public synchronized void dataHandler(int ID, DataUnit toParse) {
        //System.out.println("------------------s------------------");
        int autorID = findClient(ID);
        Session autor = null;
        if(autorID >= 0) { //pokud client ID neexistuje, nacitame dal
            autor = players.get(autorID);
        }
        else {
            return;
        }
        //String who = autor.getID() + ""; // pro vypisy
        //System.out.println( who + ": " + toParse.objCode + ", " + toParse.data);
        //if(toParse.objCode.getCode() < 21 ) //nejedna se o zpravu pro server
        //    return;
        DataUnit toSend = null;
        GameSession tmpgs = null;
        switch(toParse.objCode) {
            /*----------------------------------------------------------------*/
            case C_HELLO: { //bez na cekacku
                toSend = new DataUnit(autor.getID(),DataUnit.MsgID.S_LOBBY);
                autor.send(toSend);
                break;
            }
            /*----------------------------------------------------------------*/
            case C_OK_LOBBY: {
                    boolean ready = false; 
                    ArrayList <Integer> inLobby = new ArrayList <> ();
                    autor.setClientState(Session.PlState.INLOBBY);
                    for (Session client : players) { /*pokud je v lobby vic jak dva klientu, je mozne vyzvat hrace*/
                        if(client.getClientState() == Session.PlState.INLOBBY) {
                            inLobby.add(client.getID());
                            if(client.getID() != autor.getID()) {
                                ready = true;
                            }
                        }     
                    }
                    for (Session client : players) {
                        if(client.getClientState() == Session.PlState.INLOBBY) { //klienti v lobby
                            toSend = new DataUnit(ready,DataUnit.MsgID.S_READY);
                            client.send(toSend);
                            toSend = new DataUnit(inLobby,DataUnit.MsgID.S_CLOBBY);
                            client.send(toSend);
                        }
                    }
                    ready = false;
                break;
            } 
            /*----------------------------------------------------------------*/
            case C_UPDLOBBY: {
                ArrayList <Integer> inLobby = new ArrayList <> ();
                for (Session client : players) { /*pokud je v lobby vic jak dva klientu, je mozne vyzvat hrace*/
                    if(client.getClientState() == Session.PlState.INLOBBY) {
                        inLobby.add(client.getID());
                    }     
                }
                toSend = new DataUnit(inLobby,DataUnit.MsgID.S_CLOBBY);
                autor.send(toSend);                
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
                    foundID = findClient(clientIDs.get(i)); //najdi klienta s jeho id
                    if((foundID >= 0) && (players.get(foundID).getClientState() == Session.PlState.INLOBBY)) {
                        gs.addPlayer(players.get(foundID));
                    }
                    else {
                        toSend = new DataUnit(false,DataUnit.MsgID.S_READYFG); //server not ready for game
                        autor.setClientState(Session.PlState.INLOBBY);
                        autor.send(toSend);
                        gs.destroyer();
                        return;
                    }
                }
                toSend = new DataUnit(true,DataUnit.MsgID.S_READYFG); //prijmete vyzvu?
                gs.multicast(toSend,true);
                gameRooms.add(gs);
                break;
            }
            /*----------------------------------------------------------------*/
            case C_RESP_CHALLPL: { //dojde mi odpoved, autor je v mistnosti
                boolean resp = (boolean) toParse.data; //prijal nebo ne?
                tmpgs = findRoom(autor.getRoomID());
                if(tmpgs == null) { //pokud client ID neexistuje, nacitame dal
                    break;
                }
                else {
                    if(!resp) {
                        tmpgs.multicast(new DataUnit(false,DataUnit.MsgID.S_READYFG),false);
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
                            }
                            toSend = new DataUnit("",DataUnit.MsgID.S_CHOOSEG); //leader bude na klientove strane vybirat hru
                            leader.send(toSend);
                            toSend = new DataUnit(true,DataUnit.MsgID.S_WAITFG);
                            tmpgs.multicast(toSend,true);
                        }
                    }
                    break;
                }
            }
            /*----------------------------------------------------------------*/
            case C_CHOSENG: {  //inicializace: nova hra - v poli parametry N a K; ulozene hry- nacteny MazeBoard
                //nova hra
                if(toParse.data instanceof int []) {
                    int newgame [] = (int []) toParse.data;
                    tmpgs = findRoom(autor.getRoomID());
                    if(tmpgs == null) { //pokud client ID neexistuje, nacitame dal
                        return;
                    }
                    else {
                        //nova hra - nacteni
                        tmpgs.loader(newgame[0],newgame[1],null); //N,K,savedgame=null;
                        break;
                    }
                    
                }
                //ulozena hra
                else if(toParse.data instanceof MazeBoard){
                    tmpgs = findRoom(autor.getRoomID());
                    if(tmpgs == null) { //pokud client ID neexistuje, nacitame dal
                        return;
                    }
                    MazeBoard foundGame = (MazeBoard) toParse.data;
                    tmpgs.loader(-1,-1,foundGame);
                    break;
                }
                else { //prisel mi napriklad null, leader uzavrel dialog s vyberem hry
                    tmpgs = findRoom(autor.getRoomID());
                    if(tmpgs == null) { //pokud client ID neexistuje, autorovi posleme FailDialog
                        autor.send(new DataUnit(false,DataUnit.MsgID.S_READYFG));
                        return;
                    }
                    tmpgs.multicast(new DataUnit(false,DataUnit.MsgID.S_READYFG),false);
                    tmpgs.destroyer();
                }
                break;
            }
            /*----------------------------------------------------------------*/
            case C_SHIFT: { //klient mi poslal kam vlozil orotovanou FC, server mu nabidne dirs, ostatni zobrazeni casti tahu

                //Kontrola, jestli ten, kdo poslal zpravu, je na tahu
                /*************************************************/
                tmpgs = findRoom(autor.getRoomID());
                for(int i = 0; i < tmpgs.getRoommates().size() ; i++) {
                    if ((tmpgs.getRoommates().get(i).getID() == autor.getID()) && (i != tmpgs.getOnTurn()))
                        return;
                }
                /*************************************************/

                if(!tmpgs.getCanShift())
                     return;

                String input = (String) toParse.data;
                String [] coords = input.split("i|j|r"); //radek,sloupec,otoceni

                MazeBoard board = tmpgs.getGame();
                board.getFreeStone().turnForN(Integer.parseInt(coords[3])); // rotace
                if(!board.shift(board.get(Integer.parseInt(coords[1]), Integer.parseInt(coords[2]))))
                   return;

                tmpgs.setCanShift(false);
                tmpgs.setGame(board);
                
                for(int i = 0; i < tmpgs.getRoommates().size() ; i++) {
                    if (tmpgs.getRoommates().get(i).getID() == autor.getID()) {
                        //pro autora naleznu cesty, kam muze jit
                        board.findRoutes(autor.getID());
                        toSend = new DataUnit(board,DataUnit.MsgID.S_DIRS);
                        autor.send(toSend);
                        board.noRoutes();
                    }
                    else {
                    //odeslani update desky pro ostatni hrace
                    toSend = new DataUnit(board,DataUnit.MsgID.S_GUPADATE);
                    tmpgs.getRoommates().get(i).send(toSend);
                    }
                }
                break;
            }
            /*----------------------------------------------------------------*/
            case C_MOVE: { //hrac se pohnul, vzal poklad?
                tmpgs = findRoom(autor.getRoomID());
                for(int i = 0; i < tmpgs.getRoommates().size() ; i++) {
                    if ((tmpgs.getRoommates().get(i).getID() == autor.getID()) && (i != tmpgs.getOnTurn()))
                        return;
                }
                MazeField goal = (MazeField) toParse.data;
                tmpgs = findRoom(autor.getRoomID());
                MazeBoard board = tmpgs.getGame();
                Player clientFigure = board.getPlayerByID(autor.getID());
                
                //Odstraneni hrace z desky a posunuti na novou pozici
                clientFigure.seizePosition(board.get(goal.row(), goal.col()));
                
                if(clientFigure.checkTreasure()){ //je tam poklad,co hledam?
                    //odebrat z balicku, zkontrolovat pocet sebranych pokladu, jestli neni konec hry, predat opet novy tah
                    if(clientFigure.getTreasureCount() == tmpgs.getGoalScore()) {
                        tmpgs.setGame(board);
                        tmpgs.setCanShift(true);
                        tmpgs.multicast(new DataUnit(board,DataUnit.MsgID.S_GUPADATE),false);
                        tmpgs.multicast(new DataUnit(autor.getID(),DataUnit.MsgID.S_ENDGAME),false);
                        break;
                    }
                    else {    
                        tmpgs.setCanShift(true);
                        board.findRoutes(autor.getID());
                        board.setNewCard(clientFigure);
                        autor.send(new DataUnit(new Object[]{autor.getID(), board.getFinderPaths()}, DataUnit.MsgID.S_YOURTURN));
                    }        
                }
                else {
                    tmpgs.nextTurn();
                }
                tmpgs.setGame(board);
                tmpgs.multicast(new DataUnit(board,DataUnit.MsgID.S_GUPADATE),false);
                break;
            }
            /*----------------------------------------------------------------*/
            case C_LEFT_GAME: {  //klient odesel z rozehrane hry
                //System.out.println( "-" + who + ": " + "opustil hru.");
                tmpgs = findRoom(autor.getRoomID());
                tmpgs.removePlayer(autor);
                if((boolean) toParse.data) //vsem  se odesle zprava o odejiti hrace ze hry
                    tmpgs.multicast(new DataUnit(new Object[]{-autor.getID(),null},DataUnit.MsgID.S_YOURTURN), false);
                //tmpgs.destroyer();
                toSend = new DataUnit(autor.getID(),DataUnit.MsgID.S_LOBBY);
                autor.send(toSend); //hra skoncila normalne, autor jde do lobby
                
                break;
            }
            /*----------------------------------------------------------------*/
            default:
                //System.out.println("Tady jsme v defaultni vetvi");
                autor.send(new DataUnit("OK",DataUnit.MsgID.UNKNOWN));
                break;

        }
        //if(toSend != null)
        //    System.out.println(who +": "+ toSend.data + ".");
        //System.out.println("------------------s------------------");
    }
    
    /**
     * Odstraneni hrace ze serveru
     * @param ID ID hrace
     */
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
            System.err.println("Error closing thread: " + ioe);
        }
        threadExitus.stop();
    }

    /**
     * Vytvoreni noveho sezeni pro klienta
     * @param socket schranka pro klienta
     */
    private void newSession(Socket socket) {  
        System.out.println("Client accepted: " + socket);
        playerID++;
        Session newPlayer = new Session(this, socket,playerID);
        players.add(newPlayer);
        try {
            newPlayer.open(); 
            newPlayer.start();  
        }
        catch(IOException ioe) {  
            System.err.println("Error opening thread: " + ioe);
        }
    } 
    
  /**
   * Vytvoreni serveru
   * @param args CLI argumenty
   */
   public static void main(String args[]) {  
      Server mujserver = null;
      mujserver = new Server();

      }
}
