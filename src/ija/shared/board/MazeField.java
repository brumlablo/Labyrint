/* file name  : MazeField.java
 * authors    : xhajek33, xblozo00
 */

package ija.shared.board;

import ija.shared.player.Player;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author xhajek33
 * @author xblozo00
 */
public class MazeField implements Serializable{
    
    private int row = 0;
    private int col = 0;
    private MazeCard card = null;
    private ArrayList<Player> players;
    
    public MazeField(int row, int col) {
        this.row = row;
        this.col = col;
        this.players = new ArrayList<Player>();
    }
    
    public int row() {
        return this.row;
    }
    
    public int col() {
        return this.col;
    }
    
    public MazeCard getCard() {
        return this.card;
    }
    
    public void putCard(MazeCard c) {
        this.card = c;
    }

    public void putPlayer(Player p) {
        this.players.add(p);
    }

    public void removePlayer(Player p) {
        this.players.remove(p);
    }

    public boolean hasPlayer() {
        return !players.isEmpty();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
