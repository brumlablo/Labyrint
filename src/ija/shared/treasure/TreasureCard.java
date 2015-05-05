/* file name  : TreasureCard.java
 * authors    : xhajek33, xblozo00
 * created    : Tue 28 Apr 2015 11:48:55 AM CEST
 * copyright  : 
 *
 * modifications:
 *
 */
package ija.shared.treasure;

import java.io.Serializable;

/** Trida reprezentujici jednu herni kartu s pokladem 
 *  
 * 
 * @author xhajek33
 * @version 1.0
 */
public class TreasureCard implements Serializable{
    
    private Treasure treasure;
    
    /** Konstruktor tridy, vytvori jednu kartu s pokladem 
     *  
     * 
     * @param tr poklad, ktery karta reprezentuje
     */
    public TreasureCard(Treasure tr) {
        treasure = tr;
    }


    /** Metoda pro porovnani hernich karet 
     *  
     * 
     * @param o objekt pro porovnani
     * @return false pokud Object o neni instanci tridy TreasureCard,
     * jinak porovna oba objekty
     */
    public boolean equals(Object o) {
        if(!(o instanceof TreasureCard))
            return false;
        else
            return treasure.equals(((TreasureCard)o).treasure);
    }

    public Treasure getTreasure() {
        return this.treasure;
    }
}
