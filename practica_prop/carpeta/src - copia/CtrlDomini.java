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
    private String Path_entrada;
    private int Algoritme;
    private String Path_sortida;
    
    private CtrlFitxer ControladorFitxer;
    
    
    private InputStream Input;
    
    
    public CtrlDomini(int funcio, String path_entrada, int algoritme) throws IOException {
        inicializarCtrlDomini(path_entrada,algoritme);
        ByteArrayOutputStream output;
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

    public void inicializarCtrlDomini(String path_entrada, int algoritme) {

        ControladorFitxer = new CtrlFitxer(this);
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
        else if ( Path_entrada.endsWith(".tar")) this.Path_sortida = "./carpeta";
        else if (new File(Path_entrada).isDirectory()) Path_sortida = "carpeta.tar";
    }
    
    public void carregarFitxerEntrada() throws IOException{
        Input  = ControladorFitxer.carregarFitxerEntrada(Path_entrada);
    }
    public void escriureFitxerSortida(ByteArrayOutputStream stream) throws IOException{
        ControladorFitxer.escriureFitxerSortida(stream,Path_sortida);
    }
    
    public ByteArrayOutputStream comprimir(InputStream input, int algoritme) throws IOException{
        Algoritme arxiu;
        switch (algoritme) {
            case 0:
                arxiu = new LZ78(input, 0);
                break;
            case 1:
                arxiu = new LZW(input, 0);
                break;
            case 2:
                arxiu = new LZSS(input, 0);
                break;
            case 3:
                arxiu = new JPEG(input);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + algoritme);
        }
        return arxiu.comprimir();
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
