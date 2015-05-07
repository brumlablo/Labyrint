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
/**
 * Trida pro klienta-hrace
 * @author babu
 */
public class Client
{  
    private Socket socket = null;
    private Thread thread = null;
    private ObjectOutputStream streamOut = null;
    private ClientHelper client = null;
    private int myID = -1;
    
    public Client() {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket("127.0.0.1", 12345);
            System.out.println("Connected: " + socket);
            start();
         }
         catch(UnknownHostException uhe) {
             System.out.println("Host unknown: " + uhe.getMessage());
         }
         catch(IOException ioe) {
             System.out.println("Unexpected exception: " + ioe.getMessage());
         }
    }

    /**
     * Nacteni prichoziho objektu, notifikace na event pro handle 
     * @param toParse
     */
    public void dataHandler(DataUnit toParse) {
        DataUnit toSend = null; 
        ArrayList<Integer> toChall = new ArrayList<Integer>();
        System.out.println("------------------c------------------");
        System.out.println( toParse.objCode + ", " + toParse.data);
        switch(toParse.objCode) {
            case S_OK: {
                send(toSend);
                break;
            }
            case S_UNAV: {
                send(toSend);
                break;
            }
            case S_LOBBY: {
                myID = (int) toParse.data;
                toSend = new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY);
                send(toSend);
                break;
            }
            case S_CLOBBY: { //na vypis: novy klient v lobby
                ArrayList <Integer> inLobby = (ArrayList <Integer>) toParse.data;
                inLobby.remove((Integer) myID); //sebe vypsat nechci
                if(inLobby.size() < 1)
                    ClientFrame.getInstance().setNGButton(false);
                else
                    ClientFrame.getInstance().setNGButton(true); 
                ClientFrame.getInstance().updateLobby(inLobby);
                break;
            }
            case S_READY: { //server ready na vyzvani
                boolean ready = (boolean)toParse.data;
                if(!ready) {
                    //blokuj tlacitko ZACIT HRAT
                    ClientFrame.getInstance().setNGButton(false);
                    System.out.println("not ready kurna");
                    break;
                }
                ClientFrame.getInstance().setNGButton(true);
                ClientFrame.getInstance().setLobbyButtons(true);
                System.out.println("ready kurna");
                break;
            }
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
                System.out.println("Jsi vyzvan ke hre, prijimas?");
                ClientFrame.getInstance().showChallDialog();
                break;
            }
            case S_WAITFG: { //+nastaveni mistnosti
                System.out.println("Leader vybira parametry hry...");
                break;
            }
            case S_CHOOSEG: { //pro leadera: vybrat hru novou nebo ulozenou
                //GUI s oknem na vyber hry
                //tlacitko nova hra a pole s N a K
                System.out.println("Leadere, vyber hru.");
                ClientFrame.getInstance().chooseGDialog();
                break;
            }
            case S_SHOWGS: { //vybrat hru a do C_CHOSENSG
               //POSLE C_CHOSENSG
                break;
            }
            case S_NEWGAME: { //nova hra, barva hrace
                System.out.println("Toto je moje skvela hra. Moc se mi libi.");
                ClientFrame.getInstance().showGame((MazeBoard) toParse.data);
                break;
            }
            case S_YOURTURN: {
                ClientFrame.getInstance().setGButtons((boolean) toParse.data);
                break;
            }
            case S_DIRS: {
                break;
            }
            case S_GUPADATE: {
                break;
            }
            case S_ENDGAME: {
                break;

            }      
            default:
                //send(new DataUnit("OK",DataUnit.MsgID.DENIED));
        }
        if(toSend != null)
            System.out.println(toSend.data);
        System.out.println("------------------c------------------"); 
    }

    public synchronized void start() throws IOException {  
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        client = new ClientHelper(this, socket);
        send(new DataUnit("Hello.",DataUnit.MsgID.C_HELLO));
    }

    public synchronized void stop() {
        send(new DataUnit("Ending...",DataUnit.MsgID.C_UNAV)); 
        try {
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
        }
        catch(IOException ioe) {
            System.out.println("Error closing ..."); }
            client.close();  
            client.stop();
    }

    public void send(DataUnit toSend) {   
        try {
            streamOut.writeObject(toSend);
            streamOut.flush();
        }
        catch(IOException ioe) {  
            stop();
        }
    }
}
