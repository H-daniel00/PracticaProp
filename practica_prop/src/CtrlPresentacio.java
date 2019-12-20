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
    private VistaPrincipal VistaPrincipal;

    public CtrlPresentacio(VistaPrincipal vp,int funcio, String fitxer, int algoritme) throws IOException {
        VistaPrincipal = vp;
        ControladorDomini = new CtrlDomini(this,funcio,fitxer,algoritme);
    }

    public void getEstadistiques(double midaO, double midaC, double temps) {

        VistaPrincipal.mostrarEstadistiques(midaO,midaC,temps);
    }
}
