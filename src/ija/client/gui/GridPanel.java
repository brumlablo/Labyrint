/* file name  : GridPanel.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Dimension;

import ija.client.gui.ClientFrame;
import ija.shared.board.MazeBoard;

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
        this.tiles = new GridTile[size*size];
        //setPreferredSize(new Dimension(200, 200));
        init();
    }

    public void init() {
        this.textures = new TextureCache();

        setLayout(new GridLayout(this.size, this.size, 5, 5));
        for(int row = 1; row <= size; row++) {
            for(int col = 1; col <= size; col++) {
                tiles[ (row-1)+(col-1) ] = new GridTile(gameBoard.get(row, col), textures);
                add(tiles[ (row-1)+(col-1) ]);
            }
        }
        revalidate();
    }

}
