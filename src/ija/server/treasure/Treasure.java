/* file name  : Treasure.java
 * authors    : xhajek33, xblozo00
 * created    : Tue 28 Apr 2015 11:48:08 AM CEST
 * copyright  : 
 *
 * modifications:
 *
 */
package ija.server.treasure;

import java.io.Serializable;


/**Trida reprezentujici jeden poklad 
 *  
 * 
 * @author xhajek33
 * @version 1.0
 */
public class Treasure implements Serializable{
    
    private int code;
    private static int treasureCount;
    private static Treasure[] treasureSet;
    
    /**Konstruktor tridy 
     *  
     * 
     * @param code ID pokladu
     */
    private Treasure(int code) {
        this.code = code;   
    }
    
    /**Staticka metoda pro vytvoreni setu pokladu 
     *  
     * 
     * @param treasureCount pocet pokladu pro vytvoreni
     */
    public static void createSet(int treasureCount) {

        Treasure.treasureCount = 1;  //Resetovani velikosti balicku
        treasureSet = new Treasure[treasureCount];
        for(int i=0; i < treasureCount; i++) {
            Treasure.treasureCount++;
            treasureSet[i] = new Treasure(i);
        }
    }
    
    /**Ziskani pokladu podle jeho ID 
     *  
     * 
     * @param code ID reprezentujici poklad
     * @return poklad se zadanym ID, jinak NULL
     */
    public static Treasure getTreasure(int code) {
        if(treasureCount <= code || code < 0 || treasureSet[code] == null)
            return null;
        
        return treasureSet[code]; 
    }

    /**
     *  
     * 
     * @param o 
     * @return 
     */
    public boolean equals(Object o) {
        if(!(o instanceof Treasure))
            return false;
        else
            return code == ((Treasure)o).code;
    }
}
