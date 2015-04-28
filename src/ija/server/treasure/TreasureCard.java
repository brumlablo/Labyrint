package ija.server.treasure;

/**
 *
 * @author xhajek33
 */
public class TreasureCard {
    
    private Treasure treasure;
    
    public boolean equals(Object o) {
        if(!(o instanceof TreasureCard))
            return false;
        else
            return treasure.equals(((TreasureCard)o).treasure);
    }
    
    public TreasureCard(Treasure tr) {
        treasure = tr;
    }
}
