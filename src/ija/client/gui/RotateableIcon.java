package ija.client.gui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;

public class RotateableIcon implements Icon
{
	private Icon icon;
	private double angle;

	/**Vytvori ikonu otocenou okolo stredu o zadany uhel 
	 *  
	 * 
	 * @param icon ikona k otoceni
	 * @param angle uhel ve stupnich
	 */
	public RotateableIcon(Icon icon, double angle)
	{
		this.icon = icon;
		this.angle = angle;
	}

	/**Ziskani sirky ikony
	 *  
	 * 
	 * @return sirka ikony v pixelech
	 */
	@Override
	public int getIconWidth()
	{
		double radians = Math.toRadians( angle );
		double sin = Math.abs( Math.sin( radians ) );
		double cos = Math.abs( Math.cos( radians ) );
		int width = (int)Math.floor(icon.getIconWidth() * cos + icon.getIconHeight() * sin);
		return width;
	}

	/**Ziskani vysky ikony
	 *  
	 * 
	 * @return vyska ikony v pixelech
	 */
	@Override
	public int getIconHeight()
	{
		double radians = Math.toRadians( angle );
		double sin = Math.abs( Math.sin( radians ) );
		double cos = Math.abs( Math.cos( radians ) );
		int height = (int)Math.floor(icon.getIconHeight() * cos + icon.getIconWidth() * sin);
		return height;
	}

   /**
	*  Vykresleni ikony na zadanou komponentu
	*
	*  @param c komponenta, na kterou se ma ikona vykreslit 
	*  @param g graficky kontext
	*  @param x x koordinaty
	*  @param y y koordinaty
	*/
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D)g.create();

		int cWidth = icon.getIconWidth() / 2;
		int cHeight = icon.getIconHeight() / 2;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform original = g2.getTransform();
		AffineTransform at = new AffineTransform();
		at.concatenate(original);
		at.translate((getIconWidth() - icon.getIconWidth()) / 2, (getIconHeight() - icon.getIconHeight()) / 2);
		at.rotate(Math.toRadians(angle), x + cWidth, y + cHeight);
		g2.setTransform(at);
		icon.paintIcon(c, g2, x, y);
		g2.setTransform(original);
	}
}
