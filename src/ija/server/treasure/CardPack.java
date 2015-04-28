/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.server.treasure;

import java.util.Random;

/**
 *
 * @author xhajek33
 */

public class CardPack {

    private int stackSize;
    private int maxSize;
    private TreasureCard[] cardStack;
    
    public CardPack(int maxSize, int initSize) {
        if(maxSize < initSize)
            maxSize = initSize;
        
        this.stackSize = initSize;
        this.maxSize = maxSize;
        this.cardStack = new TreasureCard[initSize];
        
        Treasure.createSet();
        
        for(int i=0; i < stackSize; i++) {
            cardStack[i] = new TreasureCard(Treasure.getTreasure(stackSize-i-1));
        }
    }
    
    public TreasureCard popCard() {
        if(stackSize <= 0)
            return null;
        
        stackSize--;
        return cardStack[stackSize];
    }
    
    public int size() {
        return stackSize;
    }
    
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
