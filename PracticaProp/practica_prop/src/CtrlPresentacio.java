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
public class CtrlPresentacio {

    private CtrlDomini ControladorDomini;

    public CtrlPresentacio(int funcio, String fitxer, int algoritme) throws IOException {

        ControladorDomini = new CtrlDomini(/*this,*/funcio,fitxer,algoritme);
    }

    public void getEstadistiques(double midaO, double midaC, double temps) {
        double ratiCompressio = midaC / midaO;
        double velocitat = midaO / temps;

    }
}
