/* file name  : GridTile.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import ija.shared.board.MazeCard;
import ija.client.gui.TextureCache;
import ija.shared.treasure.Treasure;

public class GridTile extends JComponent {

    private String type;
    private int rotationVec;
    private Treasure treasure;
    private TextureCache textures;

    public GridTile(MazeCard card, TextureCache textures) {
        this.type = card.getType();
        this.rotationVec = card.getRotation();
        this.treasure = card.getTreasure();
        this.textures = textures;
        setSize(new Dimension(80, 80));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();

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

        g2.setTransform(old);
        g2.rotate(Math.toRadians(0.0));
        if(treasure != null) {
          Image tr = textures.getTreasureTexture(treasure.getCode());
          g2.drawImage(tr, 0, 0, this);
        }
  }
}
