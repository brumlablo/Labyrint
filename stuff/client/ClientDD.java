/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import shared.*;
/**
 *
 * @author babu
 */
public class ClientDD implements DataDecoder{

    @Override
    public void decode(DataUnit msg,int PLayerID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void decode(DataUnit msg) {
        decode(msg,0);
    }
    
}
