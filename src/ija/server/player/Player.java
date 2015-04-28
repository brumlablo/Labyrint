/* file name  : Player.java
 * authors    : xhajek33, xblozo00
 * created    : Tue 28 Apr 2015 04:43:51 PM CEST
 * copyright  : 
 *
 * modifications:
 *
 */

package ija.server.player;

import ija.server.board.MazeBoard;
import ija.server.board.MazeCard;

import java.util.ArrayList;

/**Trida reprezentujici jednoho hrace 
 *  
 * 
 * @author xhajek33
 * @version 1.0
 */
public class Player {
    private string name;
    private TreasureCard activeCard;
    private MazeCard position;

    public Player(string name, MazeField startPos) {
        this.name = name;
        this.position = startPos;
    }
}
