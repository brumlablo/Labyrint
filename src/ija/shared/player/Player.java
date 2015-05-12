/* file name  : Player.java
 * authors    : xhajek33, xblozo00
 */

package ija.shared.player;

import ija.shared.board.MazeField;
import ija.shared.treasure.Treasure;
import ija.shared.treasure.TreasureCard;
import java.io.Serializable;

import java.lang.String;

/**Trida reprezentujici jednoho hrace unikatniho
 * jeho barvou
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

    /**
     * Konstruktor tridy, vytvori jednoho hrace 
     * 
     * @param col barva hrace
     * @param card karta s pokladem, ktery hrac hleda
     */
    public Player(int col, TreasureCard card){
        this.color = col;
        this.activeCard = card;
    }

    /**
     * Ziskani jmena hrace 
     * 
     * @return jmeno/barva hrace
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Ziskani aktualni pozici hrace 
     * 
     * @return aktualni pozice hrace
     */
    public MazeField getPosition() {
        return position;
    }

    /** 
     * Nastaveni nove pozice hrace 
     * 
     * @param mf policko, na ktere se hrac umisti
     */
    public void setPosition(MazeField mf) {
        this.position = mf;
    }
 
    /** 
     * Obsazeni policka hracem, zaroven overi, jestli se na danem policku
     * nenachazi poklad, ktery hrac hleda; pokud ano, je navyseno jeho skore.
     *
     * @param mf policko desky k obsazeni
     */
    public void seizePosition(MazeField mf) {
        if(this.position != null)
            this.position.removePlayer(this);
        
        this.position = mf;
        mf.putPlayer(this);
    }

    /**
     * Ziskani aktivni pokladove karty (pokladu, ktery hrac hleda)
     *  
     * 
     * @return aktivni pokladova karta
     */
    public TreasureCard getActiveCard() {
        return this.activeCard;
    }
    
    /** 
     * Nastaveni ukolove karty hrace 
     * 
     * @param card karta s pokladem, kterou ma hrac hledat
     */
    public void setActiveCard(TreasureCard card) {
        this.activeCard = card;
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

    /** 
     * Ziskani poctu pokladu, ktere uz hrac nasel 
     * 
     * @return pocet pokladu ziskanych hracem
     */
    public int getTreasureCount() {
        return treasureCount;
    }

    

}
