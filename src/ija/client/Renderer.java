/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.client;

import ija.server.board.MazeBoard;
import ija.server.board.MazeField;

import java.lang.Object;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                        System.out.print("C");
                        break;
                        
                    case "L":
                        System.out.print("L");
                        break;
                        
                    case "F":
                        System.out.print("F");
                        break;
                        
                    default:
                        break;
                }
                System.out.print(gameBoard.get(row, col).getCard().getRotation() + " ");
            }
            System.out.println("");
            /*posledni cast hraciho pole, zalamujeme pro freeCard*/
            if(row == this.setSize) {
                System.out.print("Freestone: ");
                switch(gameBoard.getFreeCard().getType()) {
                    case "C":
                        System.out.print("C"); 
                        break;
                        
                    case "L":
                        System.out.print("L");
                        break;
                        
                    case "F":
                        System.out.print("F");
                        break;
                        
                    default:
                        break;
                }
                System.out.print(gameBoard.getFreeCard().getRotation() + "\n");
            }
        }

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout()); //Styl rozlozeni, viz API
                JPanel left = new JPanel();
                //JPanel right = new JPanel();
                left.add(new TestPane(this.setSize, this.gameBoard));
                //right.add(new TestPane(this.setSize, this.gameBoard));
                frame.add(left, BorderLayout.WEST);
                //frame.add(right, BorderLayout.EAST);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
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




    //here comes magic
    /****************/
    public class TestPane extends JPanel {

        public TestPane(int size, MazeBoard gameBoard) {
    
            GridLayout gameBoardLayout = new GridLayout(size, size, 0, 0);
            setLayout(gameBoardLayout);



            for(int row =1; row <= setSize; row++) {
                for(int col =1; col <= setSize; col++) {

                    JLabel stone = new JLabel();
                    ImageIcon stoneIcon = null;
                    RotatedIcon stoneIconRot = null;

                    switch(gameBoard.get(row, col).getCard().getType()) {
                        case "C":
                            stoneIcon = new ImageIcon("lib/images/tile3.png");
                            break;
                            
                        case "L":
                            stoneIcon = new ImageIcon("lib/images/tile1.png");
                            break;
                            
                        case "F":
                            stoneIcon = new ImageIcon("lib/images/tile2.png");
                            break;
                            
                        default:
                            break;
                    }

                    switch(gameBoard.get(row, col).getCard().getRotation()) {
                        case 0:
                            stoneIconRot = new RotatedIcon(stoneIcon, 0.0);
                            break;

                        case 1:
                            stoneIconRot = new RotatedIcon(stoneIcon, 90.0);
                            break;

                        case 2:
                            stoneIconRot = new RotatedIcon(stoneIcon, 180.0);
                            break;

                        case 3:
                            stoneIconRot = new RotatedIcon(stoneIcon, 270.0);
                            break;

                        default:
                            break;
                    }

                    stone.setIcon(stoneIconRot);
                    add(stone);
                }
            }
        }
    }


    public class CellPane extends JPanel {

        private Color defaultBackground;

        public CellPane() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    defaultBackground = getBackground();
                    setBackground(Color.RED);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(defaultBackground);
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }
    }

}

