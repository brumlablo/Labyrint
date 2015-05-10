/* file name  : DataUnit.java
 * authors    : xhajek33, xblozo00
 */

package ija.shared;
import java.io.Serializable;

/**
 * Serializovana trida jako abstrakce jednotky dat.
 * @author xblozo00
 */
public class DataUnit implements Serializable{
    
    /**
     * typy vsech zprav
     */
    public static enum MsgID{
        //client - kody
        C_UNAV(1),
        C_HELLO(2), //novy klient
        C_CHALLPL(3), //vyzvani hraci a hra
        C_SHOWPL(4),
        C_CHOSENG(5), //nova hra
        C_LOADSG(6),
        C_SHIFT(7),
        C_MOVE(8),
        C_OK_LOBBY(9),
        C_RESP_CHALLPL(10), //klient response for game
        S_CLOBBY(11), //novy hrac v lobby
        C_UPDLOBBY(12), //update lobby
        C_LEFT_GAME(13),
        //server - kody     
        S_LOBBY(23), //bez do lobby
        S_READY(24), //pripravenost serveru na vyzvani hracu
        S_READYFG(25), //pripravenost na hru
        S_WAITFG(26), //vsichni krome leadera cekejte
        S_CHOOSEG(27), //leader vybere typ hry
        S_SHOWGS(28), //server vypise klientovi ulozene hry
        S_NEWGAME(29), //hraci deska, barva hrace, pozice
        S_DIRS(30), //koordinaty hraci na tahu
        S_GUPADATE(31), //update s tahem  ostatnim v mistnosti
        S_YOURTURN(32), //jsi na tahu
        S_ENDGAME(33), //kdo vyhral, kdo prohral, remiza atd...
        //dalsi
        UNKNOWN(99),
        NONE(100);
        private int code;
        private MsgID(int value) {
            this.code = value;
        }
        public int getCode() {
            return code;
        }
        
    };
    public Object data;
    public MsgID objCode = MsgID.NONE;

    public DataUnit(Object data, MsgID objCode) {
        this.data = data;
        this.objCode = objCode;
    }
    
}
