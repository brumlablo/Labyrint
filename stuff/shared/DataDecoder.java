/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

/**
 * Rozhrani pro tridy ServerDD a ClientDD
 * @author babu
 */
public interface DataDecoder {
    public void decode(DataUnit msg,int PLayerID);  
}
