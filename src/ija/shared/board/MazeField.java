/* file name  : MazeField.java
 * authors    : xhajek33, xblozo00
 */

package ija.shared.board;

import ija.shared.player.Player;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;

/** 
 * Trida reprezentujici jedno policko na herni desce 
 * 
 * @author xblozo00, xhajek33
 */
public class MazeField implements Serializable{
    
    private int row = 0;
    private int col = 0;
    private MazeCard card = null;
    private ArrayList<Player> players;
    
    /** 
     * Vytvori jedno policko herni desky na zadane pozici
     * 
     * @param row radek policka
     * @param col sloupec policka
     */
    public MazeField(int row, int col) {
        this.row = row;
        this.col = col;
        this.players = new ArrayList<Player>();
    }
    
    /** 
     * Ziskani radku policka 
     * 
     * @return radek policka
     */
    public int row() {
        return this.row;
    }
    
    /** 
     * Ziskani sloupce policka 
     * 
     * @return sloupec policka
     */
    public int col() {
        return this.col;
    }
    
    /** 
     * Ziskani karty, ktere lezi na policku 
     * 
     * @return karta, ktera se nachazi na policku
     */
    public MazeCard getCard() {
        return this.card;
    }
    
    /** 
     * Vlozeni karty na policko 
     * 
     * @param c karta k vlozeni
     */
    public void putCard(MazeCard c) {
        this.card = c;
    }

    /** 
     * Pridani hrace na herni policko 
     * 
     * @param p Hrac k pridani
     */
    public void putPlayer(Player p) {
        this.players.add(p);
    }

    /** 
     * Odstraneni hrace z herni desky 
     * 
     * @param p Hrac k odstraneni
     */
    public void removePlayer(Player p) {
        this.players.remove(p);
    }

    /** 
     * Ziskani vsech hracu na hernim policku 
     * 
     * @return Seznam hracu, co se nachazi na policku
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /** 
     * Nastaveni noveho seznamu hracu na policko 
     * 
     * @param newPlayers seznam hracu, ktery se ma vlozit na policko
     */
    public void setPlayers(ArrayList<Player> newPlayers) {
        this.players.clear();
        this.players.addAll(newPlayers);
        for(Player el : players)
            el.setPosition(this);
    }
}
