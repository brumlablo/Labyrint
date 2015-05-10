/* file name  : TextureCache.java
 * authors    : 
 */

package ija.client.gui;

import java.awt.Image;

/** 
 * Trida ukladajici si vsechny porebne textury pro hru 
 * 
 * @author xhajek33
 */
public class TextureCache {

    private Image[] mazeTextures = new Image[3];
    private Image[] treasureTextures = new Image[24];
    private Image[] playerTextures = new Image[4];

    /** 
     * Konstruktor tridy, ulozi si vsechny potrebne textury 
     */
    public TextureCache() {
        //Textury hracich kamenu
        /**********************/
        //typ C
        mazeTextures[0] = new javax.swing.ImageIcon("lib/textures/maze/L2.jpg").getImage();
        //typ L
        mazeTextures[1] = new javax.swing.ImageIcon("lib/textures/maze/I.jpg").getImage();
        //typ F
        mazeTextures[2] = new javax.swing.ImageIcon("lib/textures/maze/T.jpg").getImage();
        /**********************/

        //Textury pokladu
        /*****************/
        for(int i = 0; i < 24; i++)
            if(i < 10)
                treasureTextures[i] = new javax.swing.ImageIcon("lib/textures/treasures/tr0" + i + ".png").getImage();
            else
                treasureTextures[i] = new javax.swing.ImageIcon("lib/textures/treasures/tr" + i + ".png").getImage();
        /*****************/

        //Textury hrace
        /*****************/
        playerTextures[0] = new javax.swing.ImageIcon("lib/textures/players/player1.png").getImage();
        playerTextures[1] = new javax.swing.ImageIcon("lib/textures/players/player2.png").getImage();
        playerTextures[2] = new javax.swing.ImageIcon("lib/textures/players/player3.png").getImage();
        playerTextures[3] = new javax.swing.ImageIcon("lib/textures/players/player4.png").getImage();
        /*****************/

        //ScaleDown(30);
    }

    private void ScaleDown(int size) {
        mazeTextures[0] = mazeTextures[0].getScaledInstance(size, size,java.awt.Image.SCALE_SMOOTH);
        mazeTextures[1] = mazeTextures[1].getScaledInstance(size, size,java.awt.Image.SCALE_SMOOTH);
        mazeTextures[2] = mazeTextures[2].getScaledInstance(size, size,java.awt.Image.SCALE_SMOOTH);

        for(int i = 0; i < 24; i++)
            treasureTextures[i] = treasureTextures[i].getScaledInstance(2*size, 2*size, java.awt.Image.SCALE_SMOOTH);
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

    /** 
     * Ziskani textury pro poklady 
     * 
     * @param code kod pokladu
     * @return textura zadaneho pokladu
     */
    public Image getTreasureTexture(int code) {
        return treasureTextures[code];
    }

    /** 
     * Ziskani textury pro hrace 
     * 
     * @param color barva hrace
     * @return textura zadaneho hrace
     */
    public Image getPlayerTexture(int color) {
        return playerTextures[color];   
    }
}
