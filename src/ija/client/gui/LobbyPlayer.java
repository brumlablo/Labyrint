/* file name  : LobbyPlayer.java
 * authors    : xhajek33, xblozo00
 */
package ija.client.gui;

/**
 * Trida reprezentujici hrace v JListu pro Lobby
 * @author xblozo00
 */
public class LobbyPlayer {
    String name = "";
    int ID = -1;

    public LobbyPlayer(int ID) {
        this.ID = ID;
        name = "hráč " + ID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
