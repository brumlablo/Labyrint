/* file name  : TextureCache.java
 * authors    : 
 */

package ija.client.gui;

import java.awt.Image;

import ija.client.gui.RotateableIcon;

/** 
 * Trida ukladajici si vsechny porebne textury pro hru 
 * 
 * @author xhajek33
 */
public class TextureCache {

    private Image[] mazeTextures = new Image[3];
    private Image[] treasureTextures = new Image[24];

    /** 
     * Konstruktor tridy, ulozi si vsechny potrebne textury 
     */
    public TextureCache() {
        //Textury hracich kamenu
        /**********************/
        //typ C
        mazeTextures[0] = new javax.swing.ImageIcon("lib/textures/maze/tile1.png").getImage();
        //typ L
        mazeTextures[1] = new javax.swing.ImageIcon("lib/textures/maze/tile2.png").getImage();
        //typ F
        mazeTextures[2] = new javax.swing.ImageIcon("lib/textures/maze/tile3.png").getImage();
        /**********************/

        //Textury pokladu
        /*****************/
        //Coming soon
        /*****************/

        //Textury hrace
        /*****************/
        //Coming soon
        /*****************/

    }


    /** 
     * Ziskani textury pro hraci kameny 
     * 
     * @param type typ kamene
     * @return textura daneho kamene
     */
    public Image getMazeTexture(String type) {
        switch(type) {
            case "C":
                return mazeTextures[0];
            case "L":
                return mazeTextures[1];
            case "F":
                return mazeTextures[2];
            default:
                return null;
        }
    }

    //public Image getTreasureTexture() {}
}
