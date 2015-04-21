/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.homework3.board;

import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author xhajek33
 * @author xblozo00
 */
public class MazeBoard { /*hraci deska*/
    private ArrayList <MazeField> gameBoard; //hraci plocha
    private int size = 0; //velikost 1 hrany hraci plochy
    private MazeCard freeCard = null; //volna hraci karta
    
   private MazeBoard(int n) {
        this.gameBoard = new ArrayList<MazeField>();
        this.size = n;
    }  
    
    public static MazeBoard createMazeBoard(int n) {
        MazeBoard tmp = new MazeBoard(n);
        for (int i=1; i <= n; i++){
            for (int j=1; j <= n; j++) {
                tmp.gameBoard.add(new MazeField(i,j)); //pridej nove policko na pozici
            }
        }
        return tmp;
    }
    
    public void newGame() {
        String type [] = {"C","L","F"};
        int randI = 0; 
        Random rand = new Random();
        
        MazeCard tmp;
        for (int i=0; i < (this.size * this.size); i++){
            /*pseudorandomni generovani indexu v poli type od 0 do 2*/
            randI = rand.nextInt(3);
            tmp = MazeCard.create(type[randI]); //vytvor hraci kartu(kamen)
            this.gameBoard.get(i).putCard(tmp); //vloz kartu na hraci plochu
        }
        randI = rand.nextInt(3);
        this.freeCard = MazeCard.create(type[randI]);
    }
    
    public MazeField get(int r, int c) {
        for (MazeField policko : this.gameBoard) { //foreach Arraylistu
            if((policko.row() == r) && (policko.col() == c)) {
                return policko;
            } 
        }
        return null;
    }
    
    public MazeCard getFreeCard() {
        return this.freeCard;
    }
    
    public void shift(MazeField mf) {
        int r,c;
        r = mf.row();
        c = mf.col();
        MazeCard futFree = null; /*budouci volna karta*/
        
        if((r==1||r==this.size) && (c%2 == 0) && ((c < this.size) && (c >= 0))) { /*sloupec shift*/
            if(r==1) { //dolu
                futFree =  this.gameBoard.get(((this.size-1)*this.size)+(c-1)).getCard();
                for(int i = (this.size - 1); i > 0; i--)
                    this.gameBoard.get(((i)*this.size)+(c-1)).putCard(this.gameBoard.get(((i-1)*this.size)+(c-1)).getCard()); /*shiftDOWN*/
                
                this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeCard); /*vlozeni aktualni volne karty*/
                this.freeCard = futFree;/*nova volna karta*/
                
            }
            else {//nahoru
                futFree =  this.gameBoard.get(((r-this.size)*this.size)+(c-1)).getCard();
                for(int i = 1; i < this.size; i++)
                    this.gameBoard.get(((i-1)*this.size)+(c-1)).putCard(this.gameBoard.get(((i)*this.size)+(c-1)).getCard()); /*shiftUP*/
                
                this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeCard); /*vlozeni aktualni volne karty*/
                this.freeCard = futFree;/*nova volna karta*/
            }
        }
        
        else if((c==1||c==this.size) && (r%2 == 0) && ((r < this.size) && (r >= 0))) { /*radek shift*/
            if(c==1) { //doprava
                futFree = this.gameBoard.get(((r-1)*this.size)+(this.size-1)).getCard();
                for(int i = (this.size - 1); i > 0; i-- )
                    this.gameBoard.get(((r-1)*this.size)+i).putCard(this.gameBoard.get(((r-1)*this.size)+(i-1)).getCard()); /*shiftRIGHT*/
                
                this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeCard); /*vlozeni aktualni volne karty*/
                this.freeCard = futFree;/*nova volna karta*/
            }
            else {//doleva
                futFree = this.gameBoard.get(((r-1)*this.size)+(c-this.size)).getCard();
                for(int i = 1; i < this.size; i++ )
                    this.gameBoard.get(((r-1)*this.size)+(i-1)).putCard(this.gameBoard.get(((r-1)*this.size)+i).getCard()); /*shiftLEFT*/
                
                this.gameBoard.get(((r-1)*this.size)+(c-1)).putCard(this.freeCard); /*vlozeni nasi karty*/
                this.freeCard = futFree;/*nova volna karta*/
            }
        }
        else
            return;
    }
    
}
