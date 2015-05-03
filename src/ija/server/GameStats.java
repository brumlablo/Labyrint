/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.server;

/**
 *
 * @author babu
 */
public class GameStats {
    //MazeBoard gb = null;
    private int color;
    private int roomID;

    public GameStats(int color,int roomID, int N, int K) {
        //Mazeboard a vyuziti N a K
        this.color = color;
        this.roomID = roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    

}
