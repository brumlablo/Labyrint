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
    private MazeField prevShift = null;

    /** 
    * Konstruktor tridy 
    * 
    * @param n velikost jedne strany herni desky
    * @param p velikost baliku karet
    * @param h pocet hracu
    * @param colors seznam barev hracu
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

    public ArrayList<Player> getPlayers() {
        return players;
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
     * @param colors seznam barev hracu
     * @return herni desku vytvorenou dle zadanych parametru
     */
   public static MazeBoard createMazeBoard(int n, int p, int h, List<Integer> colors) {
        
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

            //Osetreni, ze poklady nejsou v rozich
            if( (randX == 0 && randY == 0) ||                   //Levy horni roh
                (randX == 0 && randY == this.size-1) ||         //Pravy horni
                (randX == this.size-1 && randY == 0) ||         //Levy spodni
                (randX == this.size-1 && randY == this.size-1)  //Pravy spodni
                  )
               continue;

            if( gameBoard[randX][randY].getCard().getTreasure() == null ) {
               gameBoard[randX][randY].getCard().setTreasure(Treasure.getTreasure(cards-1));
               cards--;
            }
         }
    }
   
    /**
     * Vytvoreni balicku karet s poklady o velikosti
     * tridni promenne deckSize
     */
    private void createDeck() {
       this.deck = new CardPack(this.deckSize);
    }

    public int getDeckSize() {
        return this.deckSize;
    }

    public void setNewCard(Player p) {
        p.setActiveCard(this.deck.popCard());
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
     * @return hrace/figurku patrici ke klientovu ID
     */
    public Player getPlayerByID(int ID) {
       return this.players.get(id2col.get(ID));
    }

    /** 
     * Znehodnoceni moznych cest pro hrace 
     */
    public void noRoutes() {
        this.finderPaths = null;
    }

    /** 
     * Vyhledani vsech moznych cest pro hracovu figurku 
     * 
     * @param ID ID klienta
     */
    public void findRoutes(int ID) {
        //vycisteni predchozich cest
        this.finderPaths = null;

        this.finderPaths = finder.findRoutes(getPlayerByID(ID), this);
    }

    /** 
     * Navraceni seznamu cest pro hrace 
     * 
     * @return seznam vsech cest pro hrace
     */
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

    /**
     * Vraci policko (objekt typu MazeField) na zadane pozici. 
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
    

    /** 
     * Metoda vracejici volny kamen 
     * 
     * @return volny kamen na desce
     */
    public MazeCard getFreeStone() {
       return this.freeStone;
    }
    

    /**
     * Vlozi volny kamen na zadanou pozici a posune sloupec/radek;
     * Zaroven overuje posouvani hracu a nemoznost provadet inverzni tahy
     *
     * 
     * @param mf Kamen, na jehoz pozici se vlozi volny kamen.
     * @return  true pokud se operace povedla, na inverzni pohyb ostatni false
     */
    public boolean shift(MazeField mf) {
        int r = mf.row();
        int c = mf.col();
        MazeCard tmp = null;
        ArrayList<Player> tmpPL = null;
        
        //SHIFT DOLU
        /*********************************************************************/
        if( (r == 1) && ((c & 1) == 0) ) {
            //Kontrola na inverzni tah
            if(this.prevShift != null)
               if( (prevShift.row() == this.size) && (prevShift.col() == c) )
                  return false;

            tmp = get(size, c).getCard();
            tmpPL = (ArrayList<Player>) get(size, c).getPlayers().clone();
            
            for(int row = this.size; row > 1; row--) {
               get(row, c).setPlayers( get(row-1, c).getPlayers() );
               get(row, c).putCard(get(row-1, c).getCard());
            }
        }
        /*********************************************************************/


        //SHIFT NAHORU
        /*********************************************************************/
        else if( (r == this.size) && ((c & 1) == 0) ) {

            //Kontrola na inverzni tah
            if(this.prevShift != null)
               if( (prevShift.row() == 1) && (prevShift.col() == c) )
                  return false;

            tmp = get(1, c).getCard();
            tmpPL = (ArrayList<Player>) get(1, c).getPlayers().clone();
            
            for(int row = 1; row < this.size; row++) {
               get(row, c).setPlayers( get(row+1, c).getPlayers() );
                get(row, c).putCard(get(row+1, c).getCard());
            }
        }
        /*********************************************************************/
        

        //SHIFT DOPRAVA
        /*********************************************************************/
        else if( ((r & 1) == 0) && (c == 1) ) {

            //Kontrola na inverzni tah
            if(this.prevShift != null)
               if( (prevShift.row() == r) && (prevShift.col() == this.size) )
                  return false;

            tmp = get(r, size).getCard();
            tmpPL = (ArrayList<Player>) get(r, size).getPlayers().clone();
            
            for(int col = this.size; col > 1; col--) {
                get(r, col).setPlayers( get(r, col-1).getPlayers() );
                get(r, col).putCard(get(r, col-1).getCard());
            }
        }
        /*********************************************************************/
        

        //SHIFT DOLEVA
        /*********************************************************************/
        else if( ((r & 1) == 0) && (c == this.size) ) {

            //Kontrola na inverzni tah
            if(this.prevShift != null)
               if( (prevShift.row() == r) && (prevShift.col() == 1) )
                  return false;

            tmp = get(r, 1).getCard();
            tmpPL = (ArrayList<Player>) get(r, 1).getPlayers().clone();

            for(int col = 1; col < this.size; col++) {
               get(r, col).setPlayers( get(r, col+1).getPlayers() );
                get(r, col).putCard(get(r, col+1).getCard());
            }
        }
        /*********************************************************************/
        
        //NIC SE NEDEJE
        else {
           return false;
        }

        //Uchovani pro kontrolu na inverzni tah
        this.prevShift = mf;
        
        get(r, c).setPlayers(tmpPL);
        get(r, c).putCard(this.freeStone);
        this.freeStone= tmp;

        return true;
    }
    
    /** 
     * Metoda pro ziskani velikosti desky
     * 
     * @return velikost hrany desky
     */
    public int getSize() {
       return this.size;
    }
    
    /**
     * Reset namapovani hracu k barve
     */
    public void resetMap() {
        this.id2col.clear();
    }
}
