/* file name  : MazeBoard.java
 * authors    : xhajek33, xblozo00
 */
package ija.shared.board;

import ija.shared.player.Player;
import ija.shared.treasure.CardPack;
import ija.shared.treasure.TreasureCard;
import ija.shared.treasure.Treasure;
import ija.shared.board.PathFinder;
import java.io.Serializable;

import java.util.*;

/** 
 * Trida reprezentujici celou herni desku
 * 
 * @author xhajek33
 * @author xblozo00
 */
public class MazeBoard implements Serializable { /*hraci deska*/
    private MazeField gameBoard[][];
    private int size = 0; //velikost 1 hrany hraci plochy
    private int deckSize = 0;
    private MazeCard freeStone = null; //volny hraci kamen
    private CardPack deck = null;
    private ArrayList<Player> players;
    private PathFinder finder;
    private ArrayList<MazeField> finderPaths;
    private Map<Integer, Integer> id2col;

    /** 
    * Konstruktor tridy 
    * 
    * @param n velikost jedne strany herni desky
    * @param p velikost baliku karet
    */
   private MazeBoard(int n, int p, int h,List<Integer> colors) {
      this.gameBoard = new MazeField[n][n];
      this.size = n;
      this.deckSize = p;
      this.players = new ArrayList<Player>();
      this.finder = new PathFinder();
      this.id2col = new HashMap<Integer, Integer>();
      createDeck();

      for(int i = 0; i < h; i++ ) {
         this.players.add(new Player(colors.get(i), this.deck.popCard()));
      }
    }  
    

   /** 
    * Provazani ID klienta s jeho barvou 
    * 
    * @param ID klientovo ID
    * @param color klientova barva
    */
   public void bindID2col(int ID, int color) {
      this.id2col.put(ID, color);
   }

    /** 
     * Metoda pro vytvoreni herni desky o velikosti n*n a prida na 
     * na kazdou pozici objekt tridy MazeField.
     *
     * @param n velikost jedne strany herni desky
     * @param p velikost baliku karet
     * @param h pocet hracu
     * @return 
     */
   public static MazeBoard createMazeBoard(int n, int p, int h,List<Integer> colors) {
        
        MazeBoard tmp = new MazeBoard(n, p, h,colors);
        for (int r=1; r <= n; r++){
            for (int c=1; c <= n; c++) {
                tmp.gameBoard[r-1][c-1] = new MazeField(r, c);
            }
        }
        return tmp;
    }
    

    /** 
     * Rozlozi na herni desce herni kameny, podle zadanych pravidel. 
     */
    public void newGame() {
        String type [] = {"C","L","F"};
        int randI = 0;
        int randStone = 0;
        MazeCard tmp;
        Random rand = new Random();

        //Predvyplneni herni desky pomoci kamenu s nahodnym natoceni
        for (int r=0; r < this.size; r++){
            for (int c=0; c < this.size; c++) {

               //Nahodne generovany kamen
              tmp = MazeCard.create(type[ randStone % 3 ]);
              randStone++;

              //nahodne natoceni
              randI = rand.nextInt(4);
              for(int j = 0; j <= randI; j++)
                  tmp.turnRight();

              //Lichy sloupec a radek, vytvoreni kamene F
              /*****************************************/
              if( ((r % 2) == 0) && ((c % 2) == 0) ) {
                  tmp = MazeCard.create("F");
                  if( r == 0 ) 
                     tmp.turnForN(2);

                  else if(r == this.size-1) 
                     tmp.turnForN(0);

                  else if(c == 0)
                     tmp.turnForN(1);

                  else if(c == this.size-1)
                     tmp.turnForN(3);
                  
                  else {
                     randI = rand.nextInt(4);
                     for(int j = 0; j <= randI; j++)
                        tmp.turnRight();
                  }
               }
              /*****************************************/


              //Vlozeni vysledneho kamene na desku
              this.gameBoard[r][c].putCard(tmp);
            }
        }

        //Vytvoreni volneho kamene
        randI = rand.nextInt(3);
        this.freeStone = MazeCard.create(type[randI]);
 

        //Vlozeni rohovych kamenu
        /***********************/
        MazeCard c1 = MazeCard.create("C");
        MazeCard c2 = MazeCard.create("C");
        MazeCard c3 = MazeCard.create("C");
        MazeCard c4 = MazeCard.create("C");

        this.gameBoard[this.size-1][this.size-1].putCard(c1);

        c2.turnRight();
        this.gameBoard[this.size-1][0].putCard(c2); 

        c3.turnForN(2);
        this.gameBoard[0][0].putCard(c3);

        c4.turnForN(3);
        this.gameBoard[0][this.size-1].putCard(c4);
        /************************/

        putTreasures();
        putPlayers();
    }

    /** 
     * Nahodne rozlozeni pokladu po herni desce
     */
    private void putTreasures() {
         int randX = 0;
         int randY = 0;
         Random rand = new Random();
         int cards = this.deckSize;
         
         while(cards != 0) {
            randX = rand.nextInt(this.size);
            randY = rand.nextInt(this.size);
            
            if( gameBoard[randX][randY].getCard().getTreasure() == null ) {
               gameBoard[randX][randY].getCard().setTreasure(Treasure.getTreasure(cards-1));
               cards--;
            }
         }
    }
   
    /**Vytvoreni balicku karet s poklady o velikosti
     * tridni promenne deckSize
     *  
     */
    private void createDeck() {
       this.deck = new CardPack(this.deckSize);
    }


    /** 
     * Ziskani hrace podle indexu 
     * 
     * @param index index hrace v seznamu hracu
     * @return hrace na zadanem indexu
     */
    public Player getPlayer(int index) {
       return players.get(index);
    }

    /** 
     * Ziskani hrace dle klientova ID 
     * 
     * @param ID klientovo ID
     * @return 
     */
    public Player getPlayerByID(int ID) {
       return this.players.get(id2col.get(ID));
    }

    public void noRoutes() {
        this.finderPaths = null;
    }

    public void findRoutes(int ID) {
        //vycisteni predchozich cest
        this.finderPaths = null;

        this.finderPaths = finder.findRoutes(getPlayerByID(ID), this);
    }

    public ArrayList<MazeField> getFinderPaths() {
        return this.finderPaths;
    }
    
    /**
     * Vlozi do rohu herni desky hrace 
     */
    private void putPlayers() {
       for(int i = 0; i < this.players.size(); i++) {
          switch(i) {
             case 0:
                this.players.get(i).seizePosition(get(1,1));
                break;
             case 1:
                this.players.get(i).seizePosition(get(this.size, this.size));
                break;
             case 2:
                this.players.get(i).seizePosition(get(this.size, 1));
                break;
             case 3:
                this.players.get(i).seizePosition(get(1, this.size));
                break;
          }

       }
    }

    /**Vraci policko (objekt typu MazeField) na zadane pozici.
     *  
     * 
     * @param r radek desky
     * @param c sloupec desky
     * @return policko herni desky
     */
    public MazeField get(int r, int c) {
        if( (r > this.size) || (c > this.size) ) {
            return null;
        }
        
        for(MazeField [] i : this.gameBoard) {
            for(MazeField j : i) {
                if( ( j.row() == r ) && ( j.col() == c ) )
                    return j;
            }
        }
        return null;
    }
    

    /** Metoda vracejici volny kamen 
     *  
     * 
     * @return 
     */
    public MazeCard getFreeStone() {
       return this.freeStone;
    }
    

    /** Vlozi volny kamen na zadanou pozici a posune sloupec/radek.
     *  
     * 
     * @param mf Kamen, na jehoz pozici se vlozi volny kamen.
     */
    public void shift(MazeField mf) {
        int r = mf.row();
        int c = mf.col();
        MazeCard tmp = null;
        
        //SHIFT DOLU
        if( (r == 1) && ((c & 1) == 0) ) {
            tmp = get(size, c).getCard();
            
            for(int row = this.size; row > 1; row--) {
                get(row, c).putCard(get(row-1, c).getCard());
            }
        }

        //SHIFT NAHORU
        else if( (r == this.size) && ((c & 1) == 0) ) {
            tmp = get(1, c).getCard();
            
            for(int row = 1; row < this.size; row++) {
                get(row, c).putCard(get(row+1, c).getCard());
            }
        }
        
        //SHIFT DOPRAVA
        else if( ((r & 1) == 0) && (c == 1) ) {
            tmp = get(r, size).getCard();
            
            for(int col = this.size; col > 1; col--) {
                get(r, col).putCard(get(r, col-1).getCard());
            }
        }
        
        //SHIFT DOLEVA
        else if( ((r & 1) == 0) && (c == this.size) ) {
            tmp = get(r, 1).getCard();
            
            for(int col = 1; col < this.size; col++) {
                get(r, col).putCard(get(r, col+1).getCard());
            }
        }
        
        //NIC SE NEDEJE
        else {
            return;
        }
        
        get(r, c).putCard(this.freeStone);
        this.freeStone= tmp;
    }
    
    /**Metoda pro ziskani velikosti desky 
     *  
     * 
     * @return velikost hrany desky
     */
    public int getSize() {
      return this.size;
    }
}
