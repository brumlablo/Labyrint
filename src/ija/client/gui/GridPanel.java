/* file name  : GridPanel.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;

import ija.client.gui.ClientFrame;
import ija.shared.DataUnit;
import ija.shared.board.MazeBoard;
import ija.shared.board.MazeField;

public class GridPanel extends JPanel {

    private int size;
    private GridTile[] tiles; 
    private TextureCache textures;
    private ClientFrame gameWindow;
    private MazeBoard gameBoard;
    private GridTile freeStoneTile;

    public GridPanel(ClientFrame window, MazeBoard gameBoard) {
        this.gameWindow = window;
        this.size = gameBoard.getSize();
        this.tiles = new GridTile[size*size];
        this.textures = new TextureCache();
        this.gameBoard = gameBoard;
        freeStoneTile = new GridTile(gameBoard.getFreeStone(), textures);
        addFreeStoneListener(freeStoneTile);
        setBackground(new Color(0xFFC373));

        init();
    }

    public void init() {
        this.removeAll();
        tiles = null;
        this.tiles = new GridTile[size*size];
        setLayout(new GridLayout(this.size, this.size, 3, 3));
        for(int row = 1; row <= size; row++) {
            for(int col = 1; col <= size; col++) {
                tiles[ (row-1)+(col-1) ] = new GridTile(gameBoard.get(row, col).getCard(), gameBoard.get(row, col).getPlayers(), textures);
                addListener(tiles[ (row-1)+(col-1) ], row, col);
                add(tiles[ (row-1)+(col-1) ]);
            }
        }
        revalidate();
    }
    
    private void addListener(GridTile tile, final int row, final int col) {
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e))
                        gameWindow.getConnect().send(new DataUnit("i"+row+"j"+col+"r"+gameBoard.getFreeStone().getRotation(), DataUnit.MsgID.C_SHIFT));

                else if(SwingUtilities.isLeftMouseButton(e))
                    for(MazeField el : gameWindow.getConnect().getPaths())
                        if(el.row() == row && el.col() == col)
                            gameWindow.getConnect().send(new DataUnit(gameBoard.get(row, col), DataUnit.MsgID.C_MOVE));

            }
        });
    }

    private void addFreeStoneListener(GridTile freestone) {
        freeStoneTile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                freeStoneTile.setVisible(false);
                gameBoard.getFreeStone().turnRight();
                freeStoneTile.setCard(gameBoard.getFreeStone());
                freeStoneTile.setVisible(true);
            }
        });
    }

    public void setGameBoard(MazeBoard mb) {
        this.gameBoard = mb;
        this.freeStoneTile.setVisible(false);
        this.freeStoneTile.setCard(mb.getFreeStone());
        this.freeStoneTile.setVisible(true);
    }
    
    public GridTile getFreeStone() {
        return freeStoneTile;
    }
}
