/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author mireiabosquemari
 */
public class CtrlPresentacio implements Serializable{

    private CtrlDomini ControladorDomini;

    public CtrlPresentacio(int funcio, String fitxer, int algoritme) throws IOException {
        
        ControladorDomini = new CtrlDomini(funcio,fitxer,algoritme);
    }
    
}
