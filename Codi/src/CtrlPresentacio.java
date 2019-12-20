import java.io.IOException;


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
