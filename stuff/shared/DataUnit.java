package shared;
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
        C_OK(1),
        C_UNAV(2),
        C_HELLO(3), //jsem tu
        C_CHOOSEPL(4), //vyzvani hraci a hra
        C_SHOWGAMES(5),
        C_CHOOSEGAME(6),
        C_SHIFT(7),
        C_MOVE(8),
        //server - kody
        S_OK(21),       
        S_UNAV(22),  //server nedostupny
        S_LOBBY(23), //bez do lobby
        S_READY(24), //pripraven na hru, je dost hracu
        S_READYFG(25),
        S_NEWGAME(26), //hraci deska, barva hrace, pozice
        S_DIRS(27),
        S_YOURTURN(28), //jsi na tahu
        S_ENDGAME(29), //kdo vyhral, kdo prohral, remiza atd...
        //dalsi
        DENIED(98),
        UNKNOWN(99),
        NONE(100);
        private int code;
        private MsgID(int value) {
            this.code = value;
        }
    };
    public Object data;
    public MsgID objCode = MsgID.NONE;

    public DataUnit(Object data, MsgID objCode) {
        this.data = data;
        this.objCode = objCode;
    }
    
}
