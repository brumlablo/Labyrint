/* file name  : GridTile.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import ija.client.gui.RotateableIcon;
import ija.server.board.MazeCard;
import ija.client.gui.TextureCache;

public class GridTile extends JComponent {

    private String type;
    private int rotationVec;
    private TextureCache textures;

    public GridTile(MazeCard card, TextureCache textures) {
        this.type = card.getType();
        this.rotationVec = card.getRotation();
        this.textures = textures;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Image img = textures.getMazeTexture(type);
        
        switch(rotationVec) {
            case 0:
                g2.rotate(Math.toRadians(0.0));
                break;

            case 1:
                g2.rotate(Math.toRadians(90.0));
                break;

            case 2:
                g2.rotate(Math.toRadians(180.0));
                break;

            case 3:
                g2.rotate(Math.toRadians(270.0));
                break;
        }
        g2.drawImage(img, 0,0, this);
        //g2.drawImage(Players.GetPlayer(Player, Direction), 0,0, this);
  }
}
