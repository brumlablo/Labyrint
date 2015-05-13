/* file name  : LobbyPlayer.java
 * authors    : xblozo00, xhajek33
 */
package ija.client.gui;

/**
 * Trida reprezentujici hrace v JListu pro Lobby
 * @author xblozo00
 */
public class LobbyPlayer {
    String name = "";
    int ID = -1;

    /**
     * Metoda nastavujici name pro vypis
     * @param ID hracovo ID
     */
    public LobbyPlayer(int ID) {
        this.ID = ID;
        name = "hráč " + ID;
    }

    /**
     * ziskani promenne na vypis hrace s jeho ID
     * @return jmeno na vypis
     */
    public String getName() {
        return name;
    }

    /**
     * ziskani ID hrace
     * @return ID hrace
     */
    public int getID() {
        return ID;
    }
    
    /**
     * uprava pro vypis
     * @return hracuv string na vypis
     */
    @Override
    public String toString() {
        return this.name;
    }
    
}
