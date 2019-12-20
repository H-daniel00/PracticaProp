import java.io.*;
import java.util.*;


public class CtrlDomini {
    
    //entrada
    private int Funcio;
    private String Path_entrada;
    private int Algoritme;
    private String Path_sortida;
    
    private CtrlFitxer ControladorFitxer;
    private Estadistiques Estadistiques;
    private CtrlPresentacio ControladorPresentacio;


    private InputStream Input;

    
    
    public CtrlDomini(CtrlPresentacio Cp, int funcio, String path_entrada, int algoritme) throws IOException {
        this.ControladorPresentacio = Cp;
        inicializarCtrlDomini(path_entrada,algoritme,funcio);
       // carregarFitxersEstadistica();//carrego, llegeixo, poso a arrayList
        ByteArrayOutputStream output;
        System.out.println(Algoritme);
        if(Algoritme == 4){
            if(funcio == 0){
                output = comprimir(Path_entrada);
                escriureFitxerSortida(output);
            }else{
                carregarFitxerEntrada();
                descomprimir(Input,Path_sortida);
            }
        }else {
            carregarFitxerEntrada();
            if (funcio == 0) {
                output = comprimir(Input, Algoritme);
            } else {
                output = descomprimir(Input, Algoritme);
            }
            escriureFitxerSortida(output);
        }

    }


    private void carregarFitxersEstadistica() throws IOException {
        ArrayList<Double> in = ControladorFitxer.carregarFitxersEstadistica(Algoritme);
        Estadistiques = new Estadistiques(in);
    }



    public void inicializarCtrlDomini(String path_entrada, int algoritme, int funcio) throws IOException {

        ControladorFitxer = new CtrlFitxer(this);
        setFuncio(funcio);
        setPath_entrada(path_entrada);
        setAlgoritme(algoritme);
        setPath_sortida();
        
    }

    public int algoritme_automatic(String path, int funcio){
        if(funcio == 0){
            if(path.endsWith(".txt")) return 1;
            else if(path.endsWith(".ppm")) return 3;
            else if(new File(path).isDirectory()) return 4;
            else return 5;
        }else{
            if(path.endsWith(".lz78")) return 0;
            else if(path.endsWith(".lzw")) return 1;
            else if(path.endsWith(".lzss")) return 2;
            else if(path.endsWith(".jpg") || path.endsWith(".jpeg")) return 3;
            else if(path.endsWith(".tar")) return 4;
            else return 5;
        }
    }


    public void setFuncio(int Funcio) {
        this.Funcio = Funcio;
    }

    public void setPath_entrada(String path_entrada) {
        this.Path_entrada = path_entrada;
    }

    public void setAlgoritme(int algoritme) throws IOException {
        if(algoritme == 5) Algoritme = algoritme_automatic(Path_entrada,Funcio);
        else this.Algoritme = algoritme;
        if(Algoritme == 5) throw new IOException("No existeix algorisme per aquesta entrada.");
    }

    public void setPath_sortida(){

        if(Path_entrada.endsWith(".txt")){
            if(Algoritme == 5 || Algoritme == 1) this.Path_sortida = "sortida.lzw";
            else if(Algoritme == 0)this.Path_sortida = "sortida.lz78";
            else if(Algoritme == 2)this.Path_sortida = "sortida.lzss";
        }
        else if(Path_entrada.endsWith(".ppm")) this.Path_sortida = "sortida.jpg";
        else if( Path_entrada.endsWith(".lz78") || Path_entrada.endsWith(".lzss") || Path_entrada.endsWith(".lzw")) this.Path_sortida = "sortida.txt";
        else if ( Path_entrada.endsWith(".jpg")) this.Path_sortida = "sortida.ppm";
        else if ( Path_entrada.endsWith(".tar")) this.Path_sortida = "carpeta";
        else if (new File(Path_entrada).isDirectory()) Path_sortida = "carpeta.tar";

    }
    
    public void carregarFitxerEntrada() throws IOException{
        this.Input  = ControladorFitxer.carregarFitxerEntrada(Path_entrada);
    }

    public void escriureFitxerSortida(ByteArrayOutputStream stream) throws IOException{
        ControladorFitxer.escriureFitxerSortida(stream,Path_sortida);
    }
    
    public ByteArrayOutputStream comprimir(InputStream input, int algoritme) throws IOException{
        ByteArrayOutputStream out;
        Algoritme arxiu;
        switch (algoritme){
            case 0:
                arxiu = new LZ78(input,0);
                break;
            case 1:
                arxiu = new LZW(input,0);
                break;
            case 2:
                arxiu = new LZSS(input,0);
                break;
            case 3:
                arxiu = new JPEG(input);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + algoritme);
        }

        out =arxiu.comprimir();

        double midaO = arxiu.getMidaO();
        double midaC = arxiu.getMidaC();
        double temps = arxiu.getTemps();

        System.out.println(" midaO: " + midaO + " midaC: " + midaC + "temps: " + temps);
        System.out.println("comprimit");
        //ArrayList<Double> o = Estadistiques.editar_estadistica(midaO,midaC,temps, algoritme);
        //ControladorFitxer.EscriureDadesEstadistica(o);
        ControladorPresentacio.getEstadistiques(midaO,midaC,temps);

        return out;


    }

    public ByteArrayOutputStream comprimir(String path_entrada) throws IOException{
        CtrlCarpeta carpeta = ControladorFitxer.ControladorCarpeta;
        return carpeta.compress(path_entrada);
    }

    public ByteArrayOutputStream descomprimir(InputStream input, int algoritme) throws IOException{
        Algoritme arxiu;
        switch (algoritme){
            case 0:
                arxiu = new LZ78(input,1);
                break;
            case 1:
                arxiu = new LZW(input,1);
                break;
            case 2:
                arxiu = new LZSS(input,1);
                break;
            case 3:
                arxiu = new JPEG(input);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + algoritme);
        }
        return arxiu.descomprimir();
    }

    public void descomprimir(InputStream input, String path_sortida) throws IOException{
        CtrlCarpeta carpeta = ControladorFitxer.ControladorCarpeta;
        carpeta.decompress(input,path_sortida);
    }

}
