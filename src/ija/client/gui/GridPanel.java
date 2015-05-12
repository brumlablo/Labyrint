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

/** 
 * Trida reprezentujici herni desku GUI 
 * 
 * @author xhajek33
 */
public class GridPanel extends JPanel {

    private int size;
    private GridTile[] tiles; 
    private TextureCache textures;
    private ClientFrame gameWindow;
    private MazeBoard gameBoard;
    private GridTile freeStoneTile;

    /** 
     * Vytvoreni matice policek typu GridTile reprezentujici
     * herni desku v GUI
     * 
     * @param window okno, do ktereho se vykresluje
     * @param gameBoard herni deska k vykresleni
     */
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

    /** 
     * Inicializace vsech komponent 
     */
    public void init() {
        this.removeAll();
        tiles = null;
        this.tiles = new GridTile[size*size];
        setLayout(new GridLayout(this.size, this.size, 3, 3));
        for(int row = 1; row <= size; row++) {
            for(int col = 1; col <= size; col++) {
                //Vytvoreni noveho policka
                tiles[ (row-1)+(col-1) ] = new GridTile(gameBoard.get(row, col), textures);
                //Nastaveni velikosti policka dle velikosti desky
                tiles[ (row-1)+(col-1) ].setScale(gameBoard.getSize());
                //Jestli je kamen na okraji a sudem radku/sloupci, nastav sipku
                tiles[ (row-1)+(col-1) ].setShiftableDir(checkShiftable(row, col));

                addListener(tiles[ (row-1)+(col-1) ], row, col);
                add(tiles[ (row-1)+(col-1) ]);
            }
        }
        revalidate();
    }

    private int checkShiftable(int row, int col) {
        if((row == 1) && ((col & 1) == 0))
            return 1;

        else if((row == size) && ((col & 1) == 0))
            return 3;

        else if( ((row & 1) == 0) && (col == 1) )
            return 4;

        else if( ((row & 1) == 0) && (col == size) )
            return 2;

        else
            return 0;
    }
    
    /** 
     * Nastaveni akci pro kliknuti mysi na GUI policko 
     * 
     * @param tile policko, kteremu se ma nastavit ActionListener
     * @param row radek policka
     * @param col sloupec policka
     */
    private void addListener(GridTile tile, final int row, final int col) {
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if(gameWindow.getConnect().getPaths() == null)
                        return;
                    for(MazeField el : gameWindow.getConnect().getPaths())
                        if(el.row() == row && el.col() == col)
                            gameWindow.getConnect().send(new DataUnit(gameBoard.get(row, col), DataUnit.MsgID.C_MOVE));
                }

                else if(SwingUtilities.isRightMouseButton(e))
                        gameWindow.getConnect().send(new DataUnit("i"+row+"j"+col+"r"+gameBoard.getFreeStone().getRotation(), DataUnit.MsgID.C_SHIFT));

            }
        });
    }

    /** 
     * Nastaveni akci pro kliknuti mysi na GUI volny kamen 
     * 
     * @param freestone GUI reprezentace volneho kamene
     */
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

    /** 
     * Nastaveni herni desky tride 
     * 
     * @param mb herni deska
     */
    public void setGameBoard(MazeBoard mb) {
        this.gameBoard = mb;
        this.freeStoneTile.setVisible(false);
        this.freeStoneTile.setCard(mb.getFreeStone());
        this.freeStoneTile.setVisible(true);
    }
    
    /** 
     * Ziskani GUI reprezentace volneho kamene  
     * 
     * @return volny kamen
     */
    public GridTile getFreeStone() {
        return freeStoneTile;
    }
}
