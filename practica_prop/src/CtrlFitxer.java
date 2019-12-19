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
public class CtrlFitxer {

    CtrlDomini domini;
    public CtrlCarpeta ControladorCarpeta;

    public CtrlFitxer(CtrlDomini ctrlDomini) {
        this.domini = ctrlDomini;
        this.ControladorCarpeta = new CtrlCarpeta(this);
    }


    public void comprimir(InputStream input, OutputStream output, int algoritme) throws IOException {
        domini.comprimir(input,algoritme);
    }
    public void descomprimir(InputStream input, OutputStream output, int algoritme) throws IOException {
        domini.descomprimir(input,algoritme);
    }

    public int algoritmeAutomatic(String path){
        return domini.algoritme_automatic(path);
    }

    
    public InputStream carregarFitxerEntrada(String path_entrada) throws IOException{
        InputStream input = null;
        input = new FileInputStream(path_entrada);
        return input;
    }
    
    public void escriureFitxerSortida(ByteArrayOutputStream stream,String path_sortida) throws IOException{
        byte[] data = stream.toByteArray();
        FileOutputStream output = new FileOutputStream(path_sortida);
        for(byte b: data){
            output.write(b);
        }
    }
}
