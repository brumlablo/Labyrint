/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import shared.*;
/**
 *
 * @author babu
 */
public class ClientDP implements DataParser{

    @Override
    public DataUnit parse(DataUnit msg, int PlayerID) {
        switch(msg.objCode) {
            case S_OK: {
                return null;
            }
            case S_UNAV: {
                return null;
            }
            case S_LOBBY: {
                return null;
            }
            case S_READY: {
                return null;
            }
            case S_READYFG: {
                return null;
            }
            case S_NEWGAME: {
                return null;
            }
            case S_YOURTURN: {
                return null;
            }
            case S_DIRS: {
                return null;
            }
            case S_ENDGAME: {
                return null;
            }         
            default:
                return (new DataUnit("OK",DataUnit.MsgID.DENIED));

        }
    }
    public DataUnit parse(DataUnit msg) {
        return parse(msg,-1);
    }
    
}
