/* file name  : GridPanel.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;

import ija.client.gui.ClientFrame;
import ija.server.board.MazeBoard;

public class GridPanel extends JPanel {

    private int size;
    private GridTile[] tiles; 
    private TextureCache textures;
    private ClientFrame gameWindow;
    private MazeBoard gameBoard;

    public GridPanel(ClientFrame window, int size, MazeBoard gameBoard) {
        this.gameWindow = window;
        this.gameBoard = gameBoard;
        this.size = size;
        init(this.size);
    }

    public void init(int size) {
        this.textures = new TextureCache();
        
        setLayout(new GridLayout(this.size, this.size, 0,0));
        for(int row = 1; row <= size; row++) {
            for(int col = 1; col <= size; col++) {
                tiles[ (row-1)+(col-1) ] = new GridTile(gameBoard.get(row, col).getCard(), textures);
                add(tiles[ (row-1)+(col-1) ]);
            }
        }
    }

}
