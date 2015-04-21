/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.homework3.board;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author xhajek33
 * @author xblozo00
 */
public class MazeCard {
   
    private String type;
    public ArrayList<CANGO> dirs; /*smery*/
    
    public static enum CANGO {
        LEFT,
        UP,
        RIGHT,
        DOWN
    };
    
    public MazeCard(String type) {     
        this.dirs = new ArrayList<CANGO>();
        switch(type) {
            case "C": /*corner*/
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.UP);
                break; 
            case "L": /*line*/
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.RIGHT);
                break;
            case "F": /*!!! tvar T*/
                this.type = type;
                dirs.add(CANGO.LEFT);
                dirs.add(CANGO.UP);
                dirs.add(CANGO.RIGHT);
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
        for(CANGO element : this.dirs) {
            element = CANGO.values()[(element.ordinal() +1 ) % 4]; /*posun indexu o jeden dal a osetreni cyklicnosti*/
            this.dirs.set(index, element);
            index++;
        }
    }

    public String getType() {
        return type;
    }
    
}
