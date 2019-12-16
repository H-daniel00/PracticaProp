/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author mireiabosquemari
 */
public class CtrlPresentacio implements Serializable{
   private String Funcio;
    private String Fitxer;
    private String Algoritme;
    private CtrlDomini ControladorDomini;

    public CtrlPresentacio(int funcio, String fitxer, int algoritme) throws IOException {
        if(funcio == 0) this.Funcio = "comprimir";
        else this.Funcio = "descomprimir";
        this.Fitxer = fitxer;
        if(algoritme == 0) this.Algoritme = "automatic";
        else if(algoritme == 1) this.Algoritme = "LZ78";
        else if(algoritme == 2) this.Algoritme = "LZSS";
        else if(algoritme == 3) this.Algoritme = "LZW";
        else if(algoritme == 4) this.Algoritme = "JPEG";
        
        ControladorDomini = new CtrlDomini(Funcio,Fitxer,Algoritme);
    }
    
}
