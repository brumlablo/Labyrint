package ija.server.board;


import java.util.Random;

/** Trida reprezentujici celou herni desku
 *  
 * 
 * @author xhajek33
 * @author xblozo00
 * @version 1.0
 */
public class MazeBoard { /*hraci deska*/
    //private ArrayList <MazeField> gameBoard; //hraci plocha
    private MazeField gameBoard[][];
    private int size = 0; //velikost 1 hrany hraci plochy
    private MazeCard freeStone = null; //volny hraci kamen
    

   /** 
    *  
    * 
    * @param n velikost jedne strany herni desky
    */
   private MazeBoard(int n) {
        this.gameBoard = new MazeField[n][n];  
      //this.gameBoard = new ArrayList<MazeField>();
        this.size = n;
    }  
    

   /** Metoda pro vytvoreni herni desky o velikosti n*n a prida na 
    * na kazdou pozici objekt tridy MazeField.
     *  
     * 
     * @param n velikost jedne strany herni desky
     * @return 
     */
    public static MazeBoard createMazeBoard(int n) {
        
        MazeBoard tmp = new MazeBoard(n);
        for (int r=1; r <= n; r++){
            for (int c=1; c <= n; c++) {
                tmp.gameBoard[r-1][c-1] = new MazeField(r, c);
                //tmp.gameBoard.add(new MazeField(i,j)); //pridej nove policko na pozici
            }
        }
        return tmp;
    }
    

    /** Rozlozi na herni desce herni kameny, podle zadanych pravidel. 
     *  
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
              tmp = MazeCard.create(type[ randStone % 3 ]);
              randStone++;

              //nahodne natoceni
              randI = rand.nextInt(4);
              for(int j = 0; j <= randI; j++)
                  tmp.turnRight();
              
              //Vlozeni na desku
              this.gameBoard[r][c].putCard(tmp);
            }
        }

        //Vytvoreni volneho kamene
        randI = rand.nextInt(3);
        this.freeStone = MazeCard.create(type[randI]);
 
        //Vlozeni rohovych kamenu
        /***********************/
        tmp = MazeCard.create("C");

        this.gameBoard[this.size-1][0].putCard(tmp);

        tmp.turnRight();
        this.gameBoard[0][0].putCard(tmp); 

        tmp.turnRight();
        this.gameBoard[0][this.size-1].putCard(tmp);

        tmp.turnRight();
        this.gameBoard[this.size-1][this.size-1].putCard(tmp);
        /************************/


    }
    

    /** Vraci policko (objekt typu MazeField) na zadane pozici.
     *  
     * 
     * @param r radek desky
     * @param c sloupec desky
     * @return policko herni desky
     */
    public MazeField get(int r, int c) {
        //for (MazeField policko : this.gameBoard) { //foreach Arraylistu
            //if((policko.row() == r) && (policko.col() == c)) {
                //return policko;
            //} 
        //}
        //return null;

        if( (r > this.size) || (c > this.size) )
            return null;
        
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
    public MazeCard getFreeCard() {
       return this.freeStone;
    }
    

    /** Vlozi volny kamen na zadanou pozici a posune sloupec/radek.
     *  
     * 
     * @param mf Kamen, na jehoz pozici se vlozi volny kamen.
     */
    public void shift(MazeField mf) {
/*        int r,c;*/
        //r = mf.row();
        //c = mf.col();
        //MazeCard futFree = null; [>budouci volna karta<]
        
        //if((r==1||r==this.size) && (c%2 == 0) && ((c < this.size) && (c >= 0))) { [>sloupec shift<]
            //if(r==1) { //dolu
                //futFree =  this.gameBoard.get(((this.size-1)*this.size)+(c-1)).getCard();
                //for(int i = (this.size - 1); i > 0; i--)
                    //this.gameBoard.get(((i)*this.size)+(c-1)).putCard(this.gameBoard.get(((i-1)*this.size)+(c-1)).getCard()); [>shiftDOWN<]
                
                //this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeStone); [>vlozeni aktualni volne karty<]
                //this.freeStone = futFree;[>nova volna karta<]
                
            //}
            //else {//nahoru
                //futFree =  this.gameBoard.get(((r-this.size)*this.size)+(c-1)).getCard();
                //for(int i = 1; i < this.size; i++)
                    //this.gameBoard.get(((i-1)*this.size)+(c-1)).putCard(this.gameBoard.get(((i)*this.size)+(c-1)).getCard()); [>shiftUP<]
                
                //this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeStone); [>vlozeni aktualni volne karty<]
                //this.freeStone = futFree;[>nova volna karta<]
            //}
        //}
        
        //else if((c==1||c==this.size) && (r%2 == 0) && ((r < this.size) && (r >= 0))) { [>radek shift<]
            //if(c==1) { //doprava
                //futFree = this.gameBoard.get(((r-1)*this.size)+(this.size-1)).getCard();
                //for(int i = (this.size - 1); i > 0; i-- )
                    //this.gameBoard.get(((r-1)*this.size)+i).putCard(this.gameBoard.get(((r-1)*this.size)+(i-1)).getCard()); [>shiftRIGHT<]
                
                //this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeStone); [>vlozeni aktualni volne karty<]
                //this.freeStone = futFree;[>nova volna karta<]
            //}
            //else {//doleva
                //futFree = this.gameBoard.get(((r-1)*this.size)+(c-this.size)).getCard();
                //for(int i = 1; i < this.size; i++ )
                    //this.gameBoard.get(((r-1)*this.size)+(i-1)).putCard(this.gameBoard.get(((r-1)*this.size)+i).getCard()); [>shiftLEFT<]
                
                //this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeStone); [>vlozeni nasi karty<]
                //this.freeStone = futFree;[>nova volna karta<]
            //}
        //}
        //else
            //return;





        int r = mf.row();
        int c = mf.col();
        MazeCard tmp = null;
        
        //SHIFT DOLU
        if( (r == 1) && ((c & 1) == 0) ) {
            tmp = this.gameBoard[this.size-1][c-1].getCard();
            
            for(int row = this.size-1; row > 0; row--) {
                this.gameBoard[row][c-1].putCard(this.gameBoard[row-1][c-1].getCard());
            }
        }

        //SHIFT DOLU
        else if( (r == this.size) && ((c & 1) == 0) ) {
            tmp = this.gameBoard[0][c-1].getCard();
            
            for(int row = 1; row < this.size; row++) {
                this.gameBoard[row][c-1].putCard(this.gameBoard[row-1][c-1].getCard());
            }
        }
        
        //SHIFT DOPRAVA
        else if( ((r & 1) == 0) && (c == 1) ) {
            tmp = this.gameBoard[r-1][this.size-1].getCard();
            
            for(int col = this.size-1; col > 0; col--) {
                this.gameBoard[r-1][col].putCard(this.gameBoard[r-1][col-1].getCard());
            }
        }
        
        //SHIFT DOLEVA
        else if( ((r & 1) == 0) && (c == this.size) ) {
            tmp = this.gameBoard[r-1][0].getCard();
            
            for(int col = 1; col < this.size; col--) {
                this.gameBoard[r-1][col].putCard(this.gameBoard[r-1][col-1].getCard());
            }
        }
        
        //NIC SE NEDEJE
        else {
            return;
        }
        
        this.gameBoard[r-1][c-1].putCard(this.freeStone);
        this.freeStone= tmp;
    }
    
}
