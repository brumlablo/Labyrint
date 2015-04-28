package ija.client;

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
