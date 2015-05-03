/* file name  : Client.java
 * authors    : xhajek33, xblozo00
 */
package ija.client;

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
        System.out.println(toParse.data);
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
                toSend = new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY);
                send(toSend);
                break;
            }
            case S_CLOBBY: { //na vypis: novy cizi klient v lobby, pro vykresleni na gui
                System.out.println("Client " + (int) toParse.data + " se pripojil...");
                break;
            }
            case S_READY: { //server ready na vyzvani
                boolean ready = (boolean)toParse.data;
                if(!ready) {
                    break;
                }
                else {
                    //je mozno vyzvat hrace
                    //zde bude gui brat id hracu
                    Scanner in = new Scanner(System.in);
                    int i = 0;
                    while(i < 3) {
                        if(in.hasNextInt())
                            toChall.add(in.nextInt());
                        i++;
                    }
                    //if(toChall.size() > 3)
                    //    System.out.println("Nelze vyzvat vice jak 3 hrace");
                    //    return;
                    toSend = new DataUnit(toChall,DataUnit.MsgID.C_CHALLPL);
                    send(toSend);
                    break;
                }
            }
            case S_READYFG: { //vyzva k pridani se do hry, dle hracova vyberu, otazka ano/ne
                boolean readyyy = false;
                boolean resp = false;
                if(!readyyy) {
                    //leaderovi prislo oznameni o pokazene vyzve, bude v lobby
                    System.out.println("Nepodarilo se uskutecnit vyzvu.");
                    toSend = new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY);
                }
                else {
                    Scanner in = new Scanner(System.in);
                    System.out.println("Jsi vyzvan ke hre, prijimas?");
                    if(in.hasNextInt()) {
                        if(in.nextInt() == 1)
                            resp = true;
                        else
                            resp = false;
                    }
                    toSend = new DataUnit(resp,DataUnit.MsgID.C_RESP_CHALLPL);
                }  
                send(toSend); 
                break;
            }
            case S_WAITFG: { //+nastaveni mistnosti
                System.out.println("Leader vybira parametry hry.");
            }
            case S_CHOOSEG: { //pro leadera: vybrat hru novou nebo ulozenou
                //GUI s oknem na vyber hry
                //tlacitko nova hra a pole s N a K
                int [] gParams = new int[2]; //parametry hry
                gParams[0] = 7; //hrana
                gParams[1] = 12; //pocet pokladu
                
                //tlacitko ulozene hry
                //gParams[0] = -1; //hrana
                //gParams[1] = -1; //pocet pokladu
                
                toSend = new DataUnit(gParams,DataUnit.MsgID.C_CHOSENG);
                send(toSend);
                break;
            }
            case S_SHOWGS: { //vybrat hru a do C_CHOSENSG
                send(toSend); //POSLE C_CHOSENSG
                break;
            }
            case S_NEWGAME: { //nova hra, barva hrace
                send(toSend); 
                break;
            }
            case S_YOURTURN: {
                send(toSend); 
                break;
            }
            case S_DIRS: {
                send(toSend); 
                break;
            }
            case S_GUPADATE: {
                send(toSend);
                break;
            }
            case S_ENDGAME: {
                send(toSend); 
                break;

            }         
            default:
                send(new DataUnit("OK",DataUnit.MsgID.DENIED));
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
    
    public static void main(String args[]) {  
        //Client mujclient = null;
        //mujclient = new Client();
     }
}
