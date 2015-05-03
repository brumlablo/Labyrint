/* file name  : PathFinder.java
 * authors    : xhajek33, xblozo00
 */

package ija.server.board;

import ija.server.board.MazeBoard;
import ija.server.board.MazeField;
import ija.server.board.MazeCard;
import ija.server.player.Player;

import java.util.ArrayList;
import java.lang.System;


/**Trida, ktera pomoci algoritmu BFS vyhleda vsechna mozna
 * policka, na ktere muze hrac vstoupit
 *  
 * 
 * @author xhajek33
 * @version 1.0
 */
public class PathFinder {
   
   private ArrayList<MazeField> OPEN;
   private ArrayList<MazeField> AVAILABLE;


   /**Konstruktor tridy, inicializuje pomocne seznamy OPEN a AVAILABLE 
    *  
    */
   public PathFinder() {
      this.OPEN = new ArrayList<MazeField>();
      this.AVAILABLE = new ArrayList<MazeField>();
   }


   /**Vyhleda a vrati vsechna mozna policka, na ktera muze hrac vstoupit 
    *  
    * 
    * @param player hrac, pro ktereho se hleda
    * @param gameBoard herni deska
    * @return seznam vsech policek, na ktera muze hrac player vstoupit
    */
   public ArrayList<MazeField> findRoutes(Player player, MazeBoard gameBoard) {

      this.OPEN.clear();
      this.AVAILABLE.clear();

      ArrayList<MazeCard.CANGO> exits;

      this.OPEN.add(player.getPosition());
      this.AVAILABLE.add(player.getPosition());

      MazeField pos;
      while( !this.OPEN.isEmpty() ) {
         pos = this.OPEN.remove(0);

         exits = checkExits(pos);
         
         for(MazeCard.CANGO dir : exits) {
            int row = pos.row();
            int col = pos.col();

            switch(dir) {
               case LEFT:
                  if( (--col) == 0 )
                     break;
                  checkNeighbour(dir, gameBoard.get(row, col) );
                  break;

               case UP:
                  if( (--row) == 0 )
                     break;
                  checkNeighbour(dir, gameBoard.get(row, col) );
                  break;

               case RIGHT:
                  if( (++col) == gameBoard.getSize()+1 )
                     break;
                  checkNeighbour(dir, gameBoard.get(row, col) );
                  break;

               case DOWN:
                  if( (++row) == gameBoard.getSize()+1 )
                     break;
                  checkNeighbour(dir, gameBoard.get(row, col) );
                  break;

               default:
                  break;
            }
         }
      }   

      return (ArrayList<MazeField>) this.AVAILABLE.clone();
   }


   /**Overuje a vraci seznam smeru, kterymi lze pozici opustit 
    *  
    * 
    * @param mf pozice, pro kterou se hledaji vychody
    * @return seznam smeru, kterymi lze policko opustit
    */
   private ArrayList<MazeCard.CANGO> checkExits(MazeField mf) {

      ArrayList<MazeCard.CANGO> exits = new ArrayList<MazeCard.CANGO>();

      for(MazeCard.CANGO dir : MazeCard.CANGO.values()) {
         if( mf.getCard().canGo(dir) == true )
            exits.add(dir);
      }

      return exits;
   }


   /**Overovani sousedniho policka, lze-li na nej vstoupit 
    *  
    * 
    * @param dir vstupni smer z vedlejsiho policka
    * @param mf overovane policko
    */
   private void checkNeighbour(MazeCard.CANGO dir, MazeField mf) {
      
      //Overit, jestli uz tento kamen neni v moznych smerech
      for(MazeField element : this.AVAILABLE)
         if(element == mf)
            return;

      //Vstupni smer, invertovany vstupni puvodni
      MazeCard.CANGO inDir = MazeCard.CANGO.values()[ (dir.ordinal()+2) % 4 ];

      if(mf.getCard().canGo(inDir)) {
         this.OPEN.add(mf);
         this.AVAILABLE.add(mf);
      }
   }
   
}
