/* file name  : GameSession.java
 * authors    : xhajek33, xblozo00
 */

package ija.server;
import ija.shared.board.MazeBoard;
import java.util.*;
import ija.shared.*;

/**
 * Trida pro novou hru
 * @author babu
 */
public class GameSession {
    private int roomID;
    private static int roomCounter = 0;
    private ArrayList <Session> roommates;
    private int ready = 0;
    private MazeBoard game = null;

    public GameSession() {
        this.roomID = roomCounter++;
        this.roommates = new ArrayList <Session> ();
    }
    
    public void addPlayer(Session p) {
        System.out.println("pridan hrac: " + p.getID() + " v mistnosti " + this.roomID);
        this.roommates.add(p);
        p.setRoomID(roomID);
    }
    
    public void destroyer () {
       for(Session najemnik : roommates) {
           najemnik.setRoomID(-1);
       };
       roommates.clear();
    } 
    
    public void multicast(DataUnit toSend,boolean notLeader){
        
        int i = 0;
        for(Session najemnik : roommates) {
            if((notLeader) && (i++ == 0))
                continue;
            System.out.println("odeslano klientovi: " + najemnik.getID());
            najemnik.send(toSend);
        }
    }

    public int getRoomID() {
        return roomID;
    }

    public void addReady() {
        this.ready++;
    }
    
    public boolean isAllReady() {
        if(ready == roommates.size())
            return true;
        return false;
    } 
    
    public void loader(int N,int K,MazeBoard savedGame) { //velikost hrany pole, pocet pokladu
        
        boolean loadSaved = false;
        if(savedGame != null) {
            loadSaved = true;
        }
        
        //generovani barev hracu
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i <= roommates.size(); i++) {
            colors.add(i);
        }
        Collections.shuffle(colors);        
        
        int myTurn = 0; //ID hrace na tahu 
        Random rand = new Random();
        myTurn = rand.nextInt(roommates.size());
        
        if(loadSaved)
            this.game = savedGame;
        else {
            this.game = MazeBoard.createMazeBoard(N,K,roommates.size(),colors);
            this.game.newGame();
        }
        //nacitani hry
        for(int i = 0; i < roommates.size() ; i++)  {      
                roommates.get(i).send(new DataUnit(this.game,DataUnit.MsgID.S_NEWGAME));
            if(i == myTurn)
                roommates.get(i).send(new DataUnit(1,DataUnit.MsgID.S_YOURTURN));
            else
                roommates.get(i).send(new DataUnit(0,DataUnit.MsgID.S_YOURTURN));
        }
    }

    public Session getLeader() {
        return roommates.get(0); //vrat leadera
    }

    public ArrayList<Session> getRoommates() {
        return roommates;
    }

    public void setRoommates(ArrayList<Session> roommates) {
        this.roommates = roommates;
    }
    
    
    
}
