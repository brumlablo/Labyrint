/* file name  : MazeCard.java
 * authors    : xhajek33, xblozo00
 * created    : Tue 28 Apr 2015 11:55:03 AM CEST
 * copyright  : 
 *
 * modifications:
 *
 */

package ija.server.board;

import ija.server.treasure.Treasure;

import java.util.ArrayList;


/**Reprezentuje kamen na hraci desce 
 *  
 * 
 * @author xhajek33
 * @author xblozo00
 * @version 1.0
 */
public class MazeCard {
   
    private String type;
    public ArrayList<CANGO> dirs; /*smery*/
    private int rotationVec;
    private Treasure treasure;

    public static enum CANGO {
        LEFT,
        UP,
        RIGHT,
        DOWN
    };
    
    /**Konstruktor tridy 
     *  
     * 
     * @param type jeden ze tri typu karty: C, L, F
     */
    public MazeCard(String type) {     
        this.dirs = new ArrayList<CANGO>();
        switch(type) {
            case "C":
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.UP);
                this.rotationVec = 0;
                this.treasure = null;
                break; 
            case "L":
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.RIGHT);
                this.rotationVec = 0;
                this.treasure = null;
                break;
            case "F": 
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.UP);
                dirs.add(CANGO.RIGHT);
                this.rotationVec = 0;
                this.treasure = null;
                break;
        }
    }   

    /**Metoda pro vytvoreni jednoho herniho kamene 
     *  
     * 
     * @param type jeden ze tri typu karty: C, L, F
     * @return herni kamen
     * @throws IllegalArgumentException 
     */
    public static MazeCard create(String type) throws IllegalArgumentException {
        if(type.equals("C") || type.equals("L") || type.equals("F")) {
            MazeCard tmp = new MazeCard(type);
            return tmp;
        }
        else 
            throw new IllegalArgumentException();            
    }
    
    /**Overuje, jestli je mozne herni kamen danym smerem opustit 
     *  
     * 
     * @param dir smer, na ktery se kamen testuje
     * @return true pokud lze timhle smerem oopustit, jinak false
     */
    public boolean canGo(MazeCard.CANGO dir) {
        for(MazeCard.CANGO dirsElem: this.dirs) {
            if(dirsElem == dir)
                return true;
        }
        return false;
    }
    
    /**Otoci herni kamen o 90° doprava 
     *  
     */
    public void turnRight() {
        int index = 0;
        rotationVec = (rotationVec+1) % 4;
        for(CANGO element : this.dirs) {
            element = CANGO.values()[(element.ordinal() +1 ) % 4]; /*posun indexu o jeden dal a osetreni cyklicnosti*/
            this.dirs.set(index, element);
            index++;
        }
    }

    /**Otoci herni kamen n-krat doprava 
     *  
     * 
     * @param n pocet pravotocivych otoceni
     */
    public void turnForN(int n) {
        for(int i = 0; i < n; i++)
            this.turnRight();
    }

    public String getType() {
        return type;
    }

    public int getRotation() {
        return this.rotationVec;
    }
    
    public void setTreasure(Treasure tr) {
        this.treasure = tr;
    }

    public Treasure getTreasure() {
        return this.treasure;
    }
}
