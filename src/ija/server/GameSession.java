/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ija.server;
import java.util.*;
import ija.shared.*;

/**
 * Trida pro novou hru
 * @author babu
 */
public class GameSession {
    //MazeBoard gb = null;
    private int roomID;
    private static int roomCounter = 0;
    private ArrayList <Session> roommates;
    private int ready = 0;
    private GameStats game = null;

    public GameSession() {
        this.roomID = roomCounter++;
        this.roommates = new ArrayList <Session> ();
    }
    
    public void addPlayer(Session p) {
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
            if((notLeader) && (i == 0))
                continue;
            najemnik.send(toSend);
            i++;
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
    
    public void loader(int N,int K,GameStats savedGame) { //velikost hrany pole, pocet pokladu
        
        boolean loadSaved = false;
        if(savedGame != null) {
            loadSaved = true;
        }
        
        //generovani barev hracu
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i <= roommates.size(); i++) {
            colors.add(i);
            colors.add(i);
        }
        Collections.shuffle(colors);        
        
        int myTurn = 0; //ID hrace na tahu 
        Random rand = new Random();
        myTurn = rand.nextInt(roommates.size());
        
        //nacitani hry
        for(int i = 0; i < roommates.size() ; i++)  {      
            if(loadSaved) {
                this.game = savedGame;
            }
            else
                this.game = new GameStats(colors.get(i),this.roomID,N,K);
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
