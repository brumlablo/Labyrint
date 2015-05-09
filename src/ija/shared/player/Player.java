/* file name  : Player.java
 * authors    : xhajek33, xblozo00
 * created    : Tue 28 Apr 2015 04:43:51 PM CEST
 * copyright  : 
 *
 * modifications:
 *
 */

package ija.shared.player;

import ija.shared.board.MazeField;
import ija.shared.treasure.TreasureCard;
import java.io.Serializable;

import java.lang.String;

/**Trida reprezentujici jednoho hrace unikatniho
 * jeho jmenem/barvou
 *  
 * 
 * @author xhajek33
 * @version 1.0
 */
public class Player implements Serializable{
    private int color = -1;
    private TreasureCard activeCard;
    private MazeField position;
    private int treasureCount;

    /**Konstruktor tridy, vytvori jednoho hrace
     *  
     * 
     * @param name jmeno hrace/barva
     * @param startPos startovni pozice, ktera se prideli do aktualni pozice
     */
    public Player(int col, TreasureCard card){
        this.color = col;
        this.activeCard = card;
    }

    /**Ziskani jmena hrace
     *  
     * 
     * @return jmeno/barva hrace
     */
    public int getColor() {
        return this.color;
    }

    /**Ziskani aktualni pozici hrace
     *  
     * 
     * @return aktualni pozice hrace
     */
    public MazeField getPosition() {
        return position;
    }
 
    /**Obsazeni policka hracem, zaroven overi, jestli se na danem policku
     * nenachazi poklad, ktery hrac hleda; pokud ano, je navyseno jeho skore.
     *  
     * 
     * @param mf policko desky k obsazeni
     */
    public void seizePosition(MazeField mf) {
        if(this.position != null)
            this.position.removePlayer(this);
        
        this.position = mf;
        mf.putPlayer(this);
        
        //if(mf.getCard().getTreasure() == this.activeCard.getTreasure())
            //this.treasureCount++;
    }

    /**Ziskani aktivni pokladove karty
     * (pokladu, ktery hrac hleda)
     *  
     * 
     * @return aktivni pokladova karta
     */
    public TreasureCard getActiveCard() {
        return this.activeCard;
    }

    /** 
     * Zkontroluje, jestli se na pozici, kde se hrac nachazi, nenachazi poklad, 
     * ktery ma sebrat
     * @return true pokud ano, jinak false
     */
    public boolean checkTreasure() {
        if(this.position.getCard().getTreasure() == this.activeCard.getTreasure()) {
            this.treasureCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.color != other.color) {
            return false;
        }
        return true;
    }


}
