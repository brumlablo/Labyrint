/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.client.gui;

/**
 * Trida reprezentujici hrace v JListu pro Lobby
 * 
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
