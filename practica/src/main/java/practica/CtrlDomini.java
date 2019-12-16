/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica;

import java.io.*;
import java.util.*;

/**
 *
 * @author mireiabosquemari
 */
public class CtrlDomini {
    
    //entrada
    private String Funcio;
    private String Path_entrada;
    private String Algoritme;
    private String Path_sortida;
    
    private CtrlFitxer ControladorFitxer;
    
    
    private InputStream input;
    private FileOutputStream output;
    
    
    public CtrlDomini(String funcio, String path_entrada, String algoritme) throws IOException {
        inicializarCtrlDomini(funcio,path_entrada,algoritme);
        carregarFitxerEntrada();
        carregarFitxerSortida();
        comprimir_descomprimir();
        
    }
    
    public void inicializarCtrlDomini( String funcio, String path_entrada, String algoritme) {
       // controladorFichero = CtrlFichero.getInstance();
        ControladorFitxer = new CtrlFitxer();
        setFuncio(funcio);
        setPath_entrada(path_entrada);
        setAlgoritme(algoritme);
        setPath_sortida();
        
    }


    public void setFuncio(String Funcio) {
        this.Funcio = Funcio;
    }
    public void setPath_entrada(String path_entrada) {
        this.Path_entrada = path_entrada;
    }
    public void setAlgoritme(String algoritme) {
        if(algoritme == "LZ78")this.Algoritme = "LZ78";
        if(algoritme == "LZSS")this.Algoritme = "LZSS";
        if(algoritme == "LZW") this.Algoritme = "LZW";
        if(algoritme == "JPEG") this.Algoritme = "JPEG";
        if(algoritme == "automatic") {
            if(Path_entrada.endsWith(".txt") || Path_entrada.endsWith(".lzw")) this.Algoritme = "LZW";
            else if(Path_entrada.endsWith(".ppm") || Path_entrada.endsWith(".jpg")) this.Algoritme = "JPEG";
            
        }
        
    } 
    public void setPath_sortida(){
        
        if(Path_entrada.endsWith(".txt")){
            if(Algoritme == "automatic" || Algoritme == "LZW") this.Path_sortida = "sortida.lzw";
            else if(Algoritme == "LZ78")this.Path_sortida = "sortida.lz78";
            else if(Algoritme == "LZSS")this.Path_sortida = "sortida.lzss";
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
    
    public void comprimir_descomprimir() throws IOException{ 
       /*if(Algoritme == "LZ78"){
           Algoritme foto = new LZ78(input,output);
           if(Funcio == "comprimir") foto.comprimir();
           else foto.descomprimir();
       }
       else if(Algoritme == "LZSS"){
           Algoritme foto = new LZSS(input,output);
           if(Funcio == "comprimir") foto.comprimir();
           else foto.descomprimir();
       }*/
       if(Algoritme == "LZW"){
           Algoritme foto = new LZW(input,output,Funcio);
           if(Funcio == "comprimir") foto.comprimir();
           else foto.descomprimir();
       }
       else if(Algoritme == "JPEG"){
           Algoritme foto = new JPEG(input,output);
           if(Funcio == "comprimir") foto.comprimir();
           else foto.descomprimir();
       }
       
       
    }
    
    
    
}
