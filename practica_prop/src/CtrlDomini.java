/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.util.*;

/**
 *
 * @author mireiabosquemari
 */
public class CtrlDomini {
    
    //entrada
    private int Funcio;
    private String Path_entrada;
    private int Algoritme;
    private String Path_sortida;
    
    private CtrlFitxer ControladorFitxer;
    
    
    private InputStream input;
    private OutputStream output;
    
    
    public CtrlDomini(int funcio, String path_entrada, int algoritme) throws IOException {
        inicializarCtrlDomini(funcio,path_entrada,algoritme);
        carregarFitxerEntrada();
        carregarFitxerSortida();
        if(funcio == 0) comprimir(input,output,Algoritme);
        else descomprimir(input,output,Algoritme);
        
    }
    
    public void inicializarCtrlDomini( int funcio, String path_entrada, int algoritme) {

        ControladorFitxer = new CtrlFitxer(this);
        setFuncio(funcio);
        setPath_entrada(path_entrada);
        setAlgoritme(algoritme);
        setPath_sortida();
        
    }

    public int algoritme_automatic(String path){
        if(path.endsWith(".txt") || path.endsWith(".lzw")) return 1;
        else if(path.endsWith(".ppm") || path.endsWith(".jpg") || path.endsWith(".jpeg")) return 3;
        if(path.endsWith(".lz78")) return 0;
        if(path.endsWith(".lzss")) return 2;
        else return 4;
    }


    public void setFuncio(int Funcio) {
        this.Funcio = Funcio;
    }

    public void setPath_entrada(String path_entrada) {
        this.Path_entrada = path_entrada;
    }
    public void setAlgoritme(int algoritme) {
        if(algoritme == 5) this.Algoritme = algoritme_automatic(Path_entrada);
        else this.Algoritme = algoritme;
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
    }
    
    public void carregarFitxerEntrada() throws IOException{
        this.input  = ControladorFitxer.carregarFitxerEntrada(Path_entrada);
    }
    public void carregarFitxerSortida() throws IOException{
        this.output = ControladorFitxer.carregarFitxerSortida(Path_sortida);
    }
    
    public void comprimir(InputStream input, OutputStream output, int algoritme) throws IOException{
        Algoritme arxiu;
        switch (algoritme){
            case 0:
                arxiu = new LZ78(input,output,0);
                break;
            case 1:
                arxiu = new LZW(input,output,0);
                break;
            case 2:
                arxiu = new LZSS(input,output,0);
                break;
            case 3:
                arxiu = new JPEG(input,output);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + algoritme);
        }
        arxiu.comprimir();
    }

    public void descomprimir(InputStream input, OutputStream output, int algoritme) throws IOException{
        Algoritme arxiu;
        switch (algoritme){
            case 0:
                arxiu = new LZ78(input,output,1);
                break;
            case 1:
                arxiu = new LZW(input,output,1);
                break;
            case 2:
                arxiu = new LZSS(input,output,1);
                break;
            case 3:
                arxiu = new JPEG(input,output);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + algoritme);
        }
        arxiu.descomprimir();
    }

}
