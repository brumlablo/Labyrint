/* file name  : GridPanel.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;

import ija.client.gui.ClientFrame;
import ija.shared.DataUnit;
import ija.shared.board.MazeBoard;

public class GridPanel extends JPanel {

    private int size;
    private GridTile[] tiles; 
    private TextureCache textures;
    private ClientFrame gameWindow;
    private MazeBoard gameBoard;
    private GridTile freeStoneTile;

    public GridPanel(ClientFrame window, MazeBoard gameBoard) {
        this.gameWindow = window;
        this.gameBoard = gameBoard;
        this.size = gameBoard.getSize();
        this.tiles = new GridTile[size*size];
        this.textures = new TextureCache();
        init();
    }

    public void init() {

        this.removeAll();
        freeStoneTile = null;
        tiles = null;
        this.tiles = new GridTile[size*size];
        setLayout(new GridLayout(this.size, this.size, 5, 5));
        for(int row = 1; row <= size; row++) {
            for(int col = 1; col <= size; col++) {
                tiles[ (row-1)+(col-1) ] = new GridTile(gameBoard.get(row, col).getCard(), gameBoard.get(row, col).getPlayers(), textures);
                addListener(tiles[ (row-1)+(col-1) ], row, col);
                add(tiles[ (row-1)+(col-1) ]);
            }
        }
        freeStoneTile = new GridTile(gameBoard.getFreeStone(), null, textures);
        freeStoneTile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                gameBoard.getFreeStone().turnRight();
                //gameWindow.updateAfterShift(getFreeStone());
                init();
            }
        });

        revalidate();
    }
    
    private void addListener(GridTile tile, final int row, final int col) {
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                gameWindow.getConnect().send(new DataUnit("i"+row+"j"+col+"r"+gameBoard.getFreeStone().getRotation(), DataUnit.MsgID.C_SHIFT));

                //gameBoard.shift(gameBoard.get(row, col));
                init();
            }
        });
    }

    public GridTile getFreeStone() {
        return freeStoneTile;
    }
}
