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
        
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform original = g2.getTransform();
		AffineTransform at = new AffineTransform();
		at.concatenate(original);
        at.translate((img.getWidth() - img.getWidth()) / 2, (img.getHeight() - img.getHeight()) / 2);
		at.rotate(Math.toRadians(angle), x + cWidth, y + cHeight);
		g2.setTransform(at);


        g2.drawImage(img, 0,0, this);
        //g2.drawImage(Players.GetPlayer(Player, Direction), 0,0, this);
  }
}
