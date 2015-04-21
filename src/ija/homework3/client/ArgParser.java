/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.homework3.client;

//import java.io.BufferedReader;
import ija.homework3.board.MazeCard;
import java.io.*;

/**
 *
 * @author xhajek33
 * @author xblozo00
 */
public class ArgParser {
    
    BufferedReader inBuffer = null;
    String cliCommand = null;
    
    public ArgParser() {
        this.inBuffer = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public void start() {
        
        boolean isEnd = false;
        
        Renderer renderer = new Renderer();
        
        while(!isEnd) {
            
            try {
                cliCommand = inBuffer.readLine();
            }
            catch(IOException ioe) {
                System.out.println("Zadejte validni prikaz.");
                System.exit(1);
            }
            
            switch(cliCommand) {
                case "q":
                    isEnd = true;
                    break;
                
                case "n":
                    renderer.newGame();
                    break;
                
                case "p":
                    renderer.printBoard();
                    break;
                    
                default:
                    if(cliCommand.matches("s\\d\\d$"))
                        renderer.shift(cliCommand.substring(1, 2), cliCommand.substring(2, 3));
                    else
                        System.out.println("Nespravny prikaz.\n"
                            + "Validni prikazy: p, n, q, sRC");
            }
        }
    }
}
