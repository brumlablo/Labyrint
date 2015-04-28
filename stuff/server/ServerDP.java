/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import shared.*;

/**
 * Dekoder serveru
 * @author babu
 */
public class ServerDP implements DataParser{
    @Override
    public DataUnit parse(DataUnit msg, int PlayerID) {
        switch(msg.objCode) {
            case C_HELLO: { //bez na cekacku, kontrola poctu
                return null;
            }
            case C_OK: {
                return null;
            }
            case C_UNAV: { //kolik zbylo hracu?
                return null;
            }
            case C_CHOOSEPL: { //vic IDs
                return null;
            }
            case C_SHOWGAMES: { //ulozene hry
                return null;
            }
            case C_CHOOSEGAME: { //nova nebo ulozena hra, inicializace
                return null;
            }
            case C_SHIFT: { //dirs
                return null;
            }
            case C_MOVE: { //vzal poklad?
                return null;
            }
            default:
                return (new DataUnit("OK",DataUnit.MsgID.DENIED));

        }
    }

}
