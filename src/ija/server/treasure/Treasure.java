/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.server.treasure;

/**
 *
 * @author xhajek33
 */
public class Treasure {
    
    private int code;
    private int treasureCount;
    private static Treasure[] treasureSet;
    
    private Treasure(int code) {
        this.code = code;   
    }
    
    public boolean equals(Object o) {
        if(!(o instanceof Treasure))
            return false;
        else
            return code == ((Treasure)o).code;
    }
    
    public static void createSet(int treasureCount) {
        treasureCount = treasureCount;
        treasureSet = new Treasure[treasureCount];
        for(int i=0; i < treasureCount; i++)
            treasureSet[i] = new Treasure(i);
    }
    
    public static Treasure getTreasure(int code) {
        if(treasureCount <= code || code < 0 || treasureSet[code] == null)
            return null;
        
        return treasureSet[code]; 
    }
}
