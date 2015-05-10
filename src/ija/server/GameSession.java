/* file name  : GameSession.java
 * authors    : xhajek33, xblozo00
 */

package ija.server;
import ija.shared.board.MazeBoard;
import ija.shared.board.MazeField;
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
    private int onTurn;
    private int goalScore = 0;

    public GameSession() {
        this.roomID = roomCounter++;
        this.roommates = new ArrayList <Session> ();
    }
    
    public void addPlayer(Session p) {
        System.out.println("pridan hrac: " + p.getID() + " v mistnosti " + this.roomID);
        this.roommates.add(p);
        p.setRoomID(roomID);
    }
    
    public void removePlayer(Session p) {
        System.out.println("odebran hrac: " + p.getID() + " v mistnosti " + this.roomID);
        this.roommates.remove(p);
        p.setRoomID(-1);
        onTurn = -1;
    }
    
    
    public void nextTurn() {
        onTurn = (++onTurn) %roommates.size();
        for(int i = 0; i < roommates.size() ; i++)  {      
             if(i == onTurn) {
                game.findRoutes(roommates.get(i).getID());
                Object[] data= new Object[2];
                data[0] = roommates.get(i).getID();
                data[1] = game.getFinderPaths();
                roommates.get(i).send(new DataUnit((Object[])data,DataUnit.MsgID.S_YOURTURN));
             }
            else
                roommates.get(i).send(new DataUnit(new Object[]{roommates.get(onTurn).getID(), null},DataUnit.MsgID.S_YOURTURN));
        }
    }

    public int getOnTurn() {
        return onTurn;
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

    public MazeBoard getGame() {
        return game;
    }

    public void setGame(MazeBoard game) {
        this.game = game;
    }

    public int getGoalScore() {
        return goalScore;
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
        for (int i = 0; i < roommates.size(); i++) {
            colors.add(i);
        }
        Collections.shuffle(colors);
        System.out.println("barvy: " + Arrays.toString(colors.toArray()));
        
        this.onTurn = 0; //ID hrace na tahu 
        Random rand = new Random();
        onTurn = rand.nextInt(roommates.size());
        
        if(loadSaved)
            this.game = savedGame; //je potreba zjistit goal score a vytahnout u ulozene hry n a k
        else {
            this.goalScore = K/roommates.size();
            this.game = MazeBoard.createMazeBoard(N,K,roommates.size(),colors);
            int col = 0;
            for(Session el : this.roommates) {
                this.game.bindID2col(el.getID(), col);
                col++;
            }
            this.game.newGame();
            this.game.findRoutes(roommates.get(onTurn).getID());
        }
        //nacitani hry
        for(int i = 0; i < roommates.size() ; i++)  {      
                roommates.get(i).send(new DataUnit(this.game,DataUnit.MsgID.S_NEWGAME));
            if(i == onTurn)
                roommates.get(i).send(new DataUnit(new Object[]{roommates.get(i).getID(), game.getFinderPaths()},DataUnit.MsgID.S_YOURTURN));
            else
                roommates.get(i).send(new DataUnit(new Object[]{roommates.get(onTurn).getID(), null}, DataUnit.MsgID.S_YOURTURN));
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
