/* file name  : GameStats.java
 * authors    : xhajek33, xblozo00
 */
package ija.server;

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
