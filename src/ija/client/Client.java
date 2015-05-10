/* file name  : Client.java
 * authors    : xhajek33, xblozo00
 */
package ija.client;

import ija.client.gui.ClientFrame;
import ija.shared.board.MazeBoard;
import java.net.*;
import java.io.*;
import java.util.*;
import ija.shared.*;
import ija.shared.board.MazeField;
/**
 * Trida pro klienta-hrace
 * @author xblozo00
 */
public class Client
{  
    private Socket socket = null;
    private Thread thread = null;
    private ObjectOutputStream streamOut = null;
    private ClientHelper client = null;
    private int myID = -1;
    private ArrayList <MazeField> paths = null;
    private MazeBoard board = null;
    
    /**
     * Pripojeni na server
     */
    public Client() {
        //System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket("127.0.0.1", 12345);
            //System.out.println("Connected: " + socket);
            start();
         }
         catch(UnknownHostException uhe) {
             System.err.println("Host unknown: " + uhe.getMessage());
         }
         catch(IOException ioe) {
             System.err.println("Unexpected exception: " + ioe.getMessage());
         }
    }

    public MazeBoard getBoard() {
        return board;
    }
    
    /**
     * Nacteni prichozi zpravy ze serveru, provedeni zmen,generace prislusne odpovedi
     * @param toParse
     */
    public void dataHandler(DataUnit toParse) {
        DataUnit toSend = null; 
        ArrayList<Integer> toChall = new ArrayList<Integer>();
        //System.out.println("------------------c------------------");
        //System.out.println( toParse.objCode + ", " + toParse.data);
        switch(toParse.objCode) {
            /*----------------------------------------------------------------*/
            case S_LOBBY: { //hrac ma jit do lobby
                myID = (int) toParse.data;
                ClientFrame.getInstance().showView("lobby");
                toSend = new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY);
                send(toSend);
                break;
            }
            /*----------------------------------------------------------------*/        
            case S_CLOBBY: { //novy klient v lobby
                ArrayList <Integer> inLobby = (ArrayList <Integer>) toParse.data;
                inLobby.remove((Integer) myID); //sebe vypsat nechci
                if(inLobby.size() < 1)
                    ClientFrame.getInstance().setNGButton(false);
                else
                    ClientFrame.getInstance().setNGButton(true); 
                ClientFrame.getInstance().updateLobby(inLobby);
                break;
            }
            /*----------------------------------------------------------------*/
            case S_READY: { //server ready na vyzvani
                boolean ready = (boolean)toParse.data;
                if(!ready) {
                    //blokuj tlacitko ZACIT HRAT
                    ClientFrame.getInstance().setNGButton(false);
                    break;
                }
                ClientFrame.getInstance().setNGButton(true);
                ClientFrame.getInstance().setLobbyButtons(true);
                break;
            }
            /*----------------------------------------------------------------*/
            case S_READYFG: { //vyzva k pridani se do hry, dle hracova vyberu, otazka ano/ne
                boolean readyyy = false;
                readyyy = (boolean) toParse.data;
                ClientFrame.getInstance().setNGButton(false);
                ClientFrame.getInstance().setLobbyButtons(false);
                if(!readyyy) {
                    //vyzva se nepodarila
                    ClientFrame.getInstance().showChallFailDialog();
                    break;
                }
                //System.out.println("Jsi vyzvan ke hre, prijimas?");
                ClientFrame.getInstance().showChallDialog();
                break;
            }
            /*----------------------------------------------------------------*/
            case S_WAITFG: { //cekani na nastaveni parametru hry
                //System.out.println("Leader vybira parametry hry...");
                break;
            }
            /*----------------------------------------------------------------*/
            case S_CHOOSEG: { //pro leadera: vybrat hru novou nebo ulozenou
                //System.out.println("Leadere, vyber hru.");
                ClientFrame.getInstance().chooseGDialog();
                break;
            }
            /*----------------------------------------------------------------*/
            case S_NEWGAME: { //nova hra, barva hrace
                //System.out.println("Toto je moje skvela hra. Moc se mi libi.");
                this.board = (MazeBoard) toParse.data;
                this.paths = board.getFinderPaths();
                ClientFrame.getInstance().setIsEnd(false);
                ClientFrame.getInstance().showGame(this.board);
                break;
            }
            /*----------------------------------------------------------------*/
            case S_YOURTURN: { //predavani tahu popr.  hlaskka o vynucenem konci hry
                Object[] recData = (Object[]) toParse.data;
                int onTurnID = (int) recData[0];
                this.paths = (ArrayList<MazeField>) recData[1];
                String [] who = {"Modrý","Zelený","Červený","Žlutý"};
                if(onTurnID < 0) { //klient opustil hru
                    int color = board.getPlayerByID(-onTurnID).getColor();
                    ClientFrame.getInstance().setConsoleText(who[color].toString() + " hráč opustil hru. Hra vynuceně končí. Pokud chceš, ulož si ji.");
                }
                else if(onTurnID == this.myID) {
                    ClientFrame.getInstance().setConsoleText("Jsi na tahu!");
                }
                else {
                    int color = board.getPlayerByID(onTurnID).getColor();
                    ClientFrame.getInstance().setConsoleText(who[color].toString() + " hráč je na tahu.");
                }
                break;
            }
            /*----------------------------------------------------------------*/
            case S_DIRS: { //cesty, kam muze jit aktivni hrac
                //System.out.println("prijal jsem dirs");
                this.board = (MazeBoard) toParse.data;
                this.paths = board.getFinderPaths();
                ClientFrame.getInstance().refreshGame(board);
                break;
            }
            /*----------------------------------------------------------------*/
            case S_GUPADATE: { //prekresleni herni desky
                //System.out.println("prijal jsem gameupdate");
                this.board = (MazeBoard) toParse.data;
                ClientFrame.getInstance().refreshGame(board);
                break;
            }
            /*----------------------------------------------------------------*/
            case S_ENDGAME: { //konec hry - vypisy
                int winnerID = (int) toParse.data;
                ClientFrame.getInstance().setIsEnd(true);
                if(this.myID == winnerID) {
                    ClientFrame.getInstance().setConsoleText("Dobrá práce - jsi VÍTĚZ!");
                }
                else {
                    String [] who = {"Modrý","Zelený","Červený","Žlutý"};
                    int color = board.getPlayerByID(winnerID).getColor();
                    ClientFrame.getInstance().setConsoleText("Hra skončila! " + who[color].toString() + " hráč vyhrál.");
                }
                toSend = new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY);
                send(toSend);
                break;
            }      
            default:
                //send(new DataUnit("OK",DataUnit.MsgID.DENIED));
        }
        //if(toSend != null)
        //    System.out.println(toSend.data);
        //System.out.println("------------------c------------------"); 
    }

    /**
     * start vlakna
     * @throws IOException 
     */
    public void start() throws IOException {  
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        client = new ClientHelper(this, socket);
        send(new DataUnit("",DataUnit.MsgID.C_HELLO));
    }

    /**
     * konec vlakna
     */
    public void stop() {
        try {
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
        }
        catch(IOException ioe) {
            System.err.println("Error closing ..."); }
            client.close();  
            client.stop();
    }

    /**
     * Navraceni ID hrace.
     * @return 
     */
    public int getMyID() {
        return this.myID;
    }

    /**
     * Vraci mozne policka pro vstup hrace.
     * @return 
     */
    public ArrayList<MazeField> getPaths() {
        return this.paths;
    }

    /**
     * Odeslani zpravy klientem
     * @param toSend odesilana zprava
     */
    public void send(DataUnit toSend) {   
        try {
            streamOut.reset();
            streamOut.writeObject(toSend);
            streamOut.flush();
        }
        catch(IOException ioe) {  
            stop();
        }
    }
}
