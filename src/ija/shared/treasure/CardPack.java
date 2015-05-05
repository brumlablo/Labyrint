/* file name  : CardPack.java
 * authors    : xhajek33, xblozo00
 * created    : Tue 28 Apr 2015 11:48:40 AM CEST
 * copyright  : 
 *
 * modifications:
 *
 */
package ija.shared.treasure;

import java.io.Serializable;
import java.util.Random;

/** Trida reprezentujici balicek hernich karet s poklady, ktere hraci musi najit 
 *  
 * 
 * @author xhajek33
 * @version 
 */
public class CardPack implements Serializable{

    private int stackSize;
    private TreasureCard[] cardStack;
    
    /**Implicitni konstruktor tridy 
     *  
     * 
     * @param initSize velikost/pocet karet v balicku
     */
    public CardPack(int initSize) {
        
        if( (initSize == 12) || (initSize == 24) ) {        
            this.stackSize = initSize;
            this.cardStack = new TreasureCard[initSize];
            
            Treasure.createSet(initSize);
            
            for(int i=0; i < stackSize; i++) {
                cardStack[i] = new TreasureCard(Treasure.getTreasure(stackSize-i-1));
            }
        }
    }
    
    /**Sejmuti vrchni karty balicku 
     *  
     * 
     * @return vrchni karta balicku, pokud takova neexistuje, vraci null
     */
    public TreasureCard popCard() {
        if(stackSize <= 0)
            return null;
        
        stackSize--;
        return cardStack[stackSize];
    }
    
    /**Vraceni velikosti balicku 
     *  
     * 
     * @return pocet karet v balicku
     */
    public int size() {
        return stackSize;
    }
    
    /** Zamichani celeho baliku karet 
     *  
     */
    public void shuffle() {
        
        Random rand = new Random();
        //Durstenfeld shuffle
        for(int i=this.stackSize-1; i > 0; i--) {
            //ziskani pozice prvku, ktery se prohodi s prvkem na pozici i
            int pos = rand.nextInt(i);
            TreasureCard tmp = cardStack[i];
            cardStack[i] = cardStack[pos];
            cardStack[pos] = tmp;
        }
    }
}
