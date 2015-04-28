package client;

import java.net.*;
import java.io.*;
import java.util.*;
import shared.*;
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
    protected ClientDP parser = null;
    
    public Client() {
        this.parser = new ClientDP();
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
        /*if (msg.equals(".bye")) {
        System.out.println("Good bye. Press RETURN to exit ...");
        stop();
        }
        else*/
        DataUnit toSend = this.parser.parse(toParse);
        System.out.println("------------------ch-----------------");
        System.out.println(toParse.data);
        System.out.println(toSend.data);
        System.out.println("------------------ch-----------------");
        send(toSend); //FAAAKT, je to ok???
    }

    public void start() throws IOException {  
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        client = new ClientHelper(this, socket);
    }

    public void stop() {
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
        Client mujclient = null;
        mujclient = new Client();
        DataUnit msg = null;
       //nacitani z konzole klienta
        Scanner in = new Scanner(System.in);
        while(true) { 
            //PEKLO
            //PEKLO
            //WTF I AM DOING
            //NOPE
            //JUST NOPE
            /* in.nextLine();
            msg = new DataUnit("hello",DataUnit.MsgID.C_HELLO);
            mujclient.send(msg); */
        }
     }
}
