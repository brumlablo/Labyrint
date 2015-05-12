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
    private int shiftableDir;

    /** 
     * Vytvori jedno policko desky 
     * 
     * @param mf Policko na herni desce reprezentovane souradnicemi [R, C]
     * @param textures kolekce textur, ze kterych se vykresluje
     */
    public GridTile(MazeField mf, TextureCache textures) {
        this.type = mf.getCard().getType();
        this.rotationVec = mf.getCard().getRotation();
        this.treasure = mf.getCard().getTreasure();
        this.players = mf.getPlayers();
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

    public void setShiftableDir(int dir) {
        this.shiftableDir = dir;
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
        return new Dimension( (int) (80 * scaleFactor), (int) (80 * scaleFactor));
    }

    /** 
     * Nastaveni faktoru skalovani obrazku podle velikosti desky 
     * 
     * @param size velikost herni desky
     */
    public void setScale(int size) {
        switch(size) {

            case 9:
               this.scaleFactor = 0.8f;
               break;

            case 11:
               this.scaleFactor = 0.6f;
               break;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Uchovani stare transformacni matice bez rotaci
        AffineTransform old = g2.getTransform();
        //Nova matice pro zmenseni a rotovani
        AffineTransform sc = new AffineTransform();
        sc.setToIdentity();
        sc.scale(scaleFactor, scaleFactor);
        // rotace 90° * rotationVec => 90 * 2 => 180°rotace
        sc.rotate(Math.PI/2 * rotationVec, 40, 40);

        //Kresleni textury labyrintu
        /**************************/
        Image img = textures.getMazeTexture(type);
        g2.drawImage(img, sc, null);
        /**************************/

        g2.setTransform(old); //nastaveni stare rotace
        g2.rotate(Math.toRadians(0.0));
        sc = old;
        sc.setToIdentity();
        sc.scale(scaleFactor, scaleFactor);


        //Vykresleni sipky pro indikaci moznosti vlozeni kamene
        /**************************/
        Image s = null;
        if(shiftableDir != 0)
           s = textures.getShiftableTexture(shiftableDir);
        g2.drawImage(s, sc, this);
        /**************************/

        //Kresleni pokladu
        /**************************/
        if(treasure != null) {
          Image tr = textures.getTreasureTexture(treasure.getCode());
          g2.drawImage(tr, sc, this);
        }
        /**************************/

        //Vykresleni hracu
        /**************************/
        if(players != null) {
          int x = 0;
          int y = 0;
          for(Player p : players) {
            sc.translate((double) x, (double) y);
            Image pl = textures.getPlayerTexture(p.getColor());
            g2.drawImage(pl, sc, null);
            x += 5;
            y += 5;
          }
        }
        /**************************/
  }
}
