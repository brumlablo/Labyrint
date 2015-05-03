package ija.client;

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

	/**
	 *  Create a RotatedIcon. The icon will rotate about its center. This
	 *  constructor will automatically set the Rotate enum to ABOUT_CENTER.
	 *  For rectangular icons the icon will be clipped before the rotation
	 *  to make sure it doesn't paint over the rest of the component.
	 *
	 *  @param icon    the Icon to rotate
	 *  @param angle   the angle of rotation
	 */
	public RotateableIcon(Icon icon, double angle)
	{
		this.icon = icon;
		this.angle = angle;
	}

	/**
	 *  Gets the width of this icon.
	 *
	 *  @return the width of the icon in pixels.
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

	/**
	 *  Gets the height of this icon.
	 *
	 *  @return the height of the icon in pixels.
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
	*  Paint the icons of this compound icon at the specified location
	*
	*  @param c The component on which the icon is painted
	*  @param g the graphics context
	*  @param x the X coordinate of the icon's top-left corner
	*  @param y the Y coordinate of the icon's top-left corner
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
