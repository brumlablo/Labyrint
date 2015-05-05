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

public class GridTile extends JComponent {

    private String type;
    private int rotationVec;
    private Treasure treasure;
    private ArrayList<Player> players;
    private TextureCache textures;

    public GridTile(MazeField mf, TextureCache textures) {

        MazeCard card = mf.getCard();
        this.type = card.getType();
        this.rotationVec = card.getRotation();
        this.treasure = card.getTreasure();
        this.players = mf.getPlayers();
        this.textures = textures;
        setPreferredSize(new Dimension(80, 80));
    }

    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return new Dimension(80, 80);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();

        //Kresleni textury labyrintu
        /**************************/
        Image img = textures.getMazeTexture(type);
        switch(rotationVec) {
            case 0:
                g2.rotate(Math.toRadians(0.0));
                break;

            case 1:
                g2.rotate(Math.toRadians(90.0), 40, 40);
                break;

            case 2:
                g2.rotate(Math.toRadians(180.0), 40, 40);
                break;

            case 3:
                g2.rotate(Math.toRadians(270.0), 40, 40);
                break;
            default:
                break;
        }
        g2.drawImage(img, 0,0, this);
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
        for(Player p : players) {
          Image pl = textures.getPlayerTexture(p.getColor());
          g2.drawImage(pl, 0, 0, this);
        }
        /**************************/
  }
}
