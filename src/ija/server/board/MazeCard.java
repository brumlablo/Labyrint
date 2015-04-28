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
    
    public MazeCard(String type) {     
        this.dirs = new ArrayList<CANGO>();
        switch(type) {
            case "C":
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.UP);
                this.rotationVec = 0;
                break; 
            case "L":
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.RIGHT);
                this.rotationVec = 0;
                break;
            case "F": 
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.UP);
                dirs.add(CANGO.RIGHT);
                this.rotationVec = 0;
                break;
        }
    }   

    public static MazeCard create(String type) throws IllegalArgumentException {
        if(type.equals("C") || type.equals("L") || type.equals("F")) {
            MazeCard tmp = new MazeCard(type);
            return tmp;
        }
        else 
            throw new IllegalArgumentException();            
    }
    
    public boolean canGo(MazeCard.CANGO dir) {
        for(MazeCard.CANGO dirsElem: this.dirs) {
            if(dirsElem == dir)
                return true;
        }
        return false;
    }
    
    public void turnRight() {
        int index = 0;
        rotationVec = (rotationVec+1) % 4;
        for(CANGO element : this.dirs) {
            element = CANGO.values()[(element.ordinal() +1 ) % 4]; /*posun indexu o jeden dal a osetreni cyklicnosti*/
            this.dirs.set(index, element);
            index++;
        }
    }

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
    
}
