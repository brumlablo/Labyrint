package ija.shared;
import java.io.Serializable;

/**
 *
 * @author babu
 */
/**
 * Serializovana trida jako abstrakce jednotky dat.
 * @author babu
 */
public class DataUnit implements Serializable{
    
    /**
     * typy vsech zprav
     */
    public static enum MsgID{
        //client - kody
        C_UNAV(1),
        C_HELLO(2), //jsem tu
        C_CHALLPL(3), //vyzvani hraci a hra
        C_SHOWPL(5),
        C_CHOSENG(6), //nova hra
        C_LOADSG(7),
        C_SHIFT(8),
        C_MOVE(9),
        C_OK_LOBBY(10),
        C_RESP_CHALLPL(11), //klient response for game
        S_CLOBBY(12),
        //server - kody
        S_OK(21),       
        S_UNAV(22),  //server nedostupny
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
        DENIED(98),
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
