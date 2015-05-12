/* file name  : GridTile.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import ija.shared.board.MazeCard;
import ija.shared.board.MazeField;
import ija.shared.player.Player;
import ija.client.gui.TextureCache;
import ija.shared.treasure.Treasure;

/** 
 * Trida predstavujici jedno policko na vykresleni 
 * 
 * @author xhajek33
 */
public class GridTile extends JComponent {

    private String type;
    private int rotationVec;
    private Treasure treasure;
    private ArrayList<Player> players;
    private TextureCache textures;
    private float scaleFactor;

    /** 
     * Vytvori jedno policko desky 
     * 
     * @param card karta, ze ktere se vyberou vsechny dulezite hodnoty
     * @param players seznam hracu
     * @param textures kolekce textur, ze kterych se vykresluje
     */
    public GridTile(MazeCard card, ArrayList<Player> players, TextureCache textures) {

        this.type = card.getType();
        this.rotationVec = card.getRotation();
        this.treasure = card.getTreasure();
        this.players = players;
        this.textures = textures;
        this.scaleFactor = 1.0f;
    }

    /** 
     * Vytvori jedno policko desky 
     * 
     * @param card karta, ze ktere se vyberou vsechny dulezite hodnoty
     * @param players seznam hracu
     * @param textures kolekce textur, ze kterych se vykresluje
     */
    public GridTile(MazeCard card, TextureCache textures) {
        this.type = card.getType();
        this.rotationVec = card.getRotation();
        this.treasure = card.getTreasure();
        this.textures = textures;
        this.scaleFactor = 1.0f;
        //setPreferredSize(new Dimension(80, 80));
    }

    /** 
     * Nastaveni karty z desky 
     * 
     * @param card karta, ze ktere se vytahnou dulezite hodnoty
     */
    public void setCard(MazeCard card) {
        this.type = card.getType();
        this.rotationVec = card.getRotation();
        this.treasure = card.getTreasure();
    }

    /** 
     * Ziskani minimalni velikosti komponenty 
     * 
     * @return min. velikost komponenty
     */
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    /** 
     * Ziskani maximalni velikosti komponenty 
     * 
     * @return max. velikost komponenty
     */
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /** 
     * Ziskani preferovane velikosti komponenty 
     * 
     * @return preferovana velikost komponenty
     */
    public Dimension getPreferredSize() {
        return new Dimension(80, 80);
    }

    /** 
     * Nastaveni faktoru skalovani obrazku podle velikosti desky 
     * 
     * @param size velikost herni desky
     */
    public void setScale(int size) {
        switch(size) {
            case 8:
               this.scaleFactor = 0.8f;
               break;

            case 9:
               this.scaleFactor = 0.7f;
               break;

            case 10:
               this.scaleFactor = 0.6f;
               break;

            case 11:
               this.scaleFactor = 0.5f;
               break;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        AffineTransform sc = new AffineTransform();
        sc.setToIdentity();
        sc.scale(scaleFactor, scaleFactor);
        sc.rotate(Math.PI/2 * rotationVec, 40, 40);

        //Kresleni textury labyrintu
        /**************************/
        Image img = textures.getMazeTexture(type);
        //switch(rotationVec) {
            //case 0:
                //g2.rotate(Math.toRadians(0.0));
                //break;

            //case 1:
                //g2.rotate(Math.toRadians(90.0), 40, 40);
                //break;

            //case 2:
                //g2.rotate(Math.toRadians(180.0), 40, 40);
                //break;

            //case 3:
                //g2.rotate(Math.toRadians(270.0), 40, 40);
                //break;
            //default:
                //break;
        //}
        g2.drawImage(img, sc, null);
        /**************************/

        //Kresleni pokladu
        /**************************/
        g2.setTransform(old); //nastaveni stare rotace
        g2.rotate(Math.toRadians(0.0));
        if(treasure != null) {
          Image tr = textures.getTreasureTexture(treasure.getCode());
          g2.drawImage(tr, 0, 0, this);
        }
        /**************************/

        //Vykresleni hracu
        /**************************/
        if(players != null) {
          int x = 0;
          int y = 0;
          for(Player p : players) {
            Image pl = textures.getPlayerTexture(p.getColor());
            g2.drawImage(pl, x, y, this);
            x += 5;
            y += 5;
          }
        }
        /**************************/
  }
}
