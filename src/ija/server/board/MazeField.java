package ija.server.board;

/**
 *
 * @author xhajek33
 * @author xblozo00
 */
public class MazeField {
    
    private int row = 0;
    private int col = 0;
    private MazeCard card = null;
    
    public MazeField(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int row() {
        return this.row;
    }
    
    public int col() {
        return this.col;
    }
    
    public MazeCard getCard() {
        return this.card;
    }
    
    public void putCard(MazeCard c) {
        this.card = c;
    }
}
