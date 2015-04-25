/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.client;

import ija.server.board.MazeBoard;
import ija.server.board.MazeCard;
import ija.server.board.MazeField;

/**
 *
 * @author xhajek33
 * @author xblozo00
 */
public class Renderer {
    
    MazeBoard gameBoard = null;
    int setSize = 7; /*velikost hrany je dana*/
    boolean createdBoard;
    
    public Renderer() {
        gameBoard = MazeBoard.createMazeBoard(setSize); /*vytvoreni herni desky o dane velikosti*/
        createdBoard = false;
    }
    
    public void newGame() {
        gameBoard.newGame();
        createdBoard = true;
    }
    
    public void printBoard() {
        
        if(!createdBoard) {
            System.out.println("Deska nebyla vytvorena.");
            return;
        } 
        System.out.println("");

        for(int row = 1; row <= setSize; row++) {
            for(int col = 1; col <= setSize; col++) {           
                
                switch(gameBoard.get(row, col).getCard().getType()) {
                    case "C":
                        System.out.print("C ");
                        break;
                        
                    case "L":
                        System.out.print("L ");
                        break;
                        
                    case "F":
                        System.out.print("F ");
                        break;
                        
                    default:
                        break;
                }
            }
            System.out.println("");
            /*posledni cast hraciho pole, zalamujeme pro freeCard*/
            if(row == this.setSize) {
                System.out.print("Freecard: ");
                switch(gameBoard.getFreeCard().getType()) {
                    case "C":
                        System.out.println("C\n"); 
                        break;
                        
                    case "L":
                        System.out.println("L\n");
                        break;
                        
                    case "F":
                        System.out.println("F\n");
                        break;
                        
                    default:
                        break;
                }
            }
        }
    }
    
    public void shift(String sRow, String sCol) {
        
        if(!createdBoard) {
            System.out.println("Deska nebyla vytvorena.");
            return;
        }
        
        //parsovani stringu na int
        int row = Integer.parseInt(sRow);
        int col = Integer.parseInt(sCol);
        
        //vytvoreni referencniho policka pro operaci shift
        MazeField mf = new MazeField(row, col);
        gameBoard.shift(mf);
    }
}
