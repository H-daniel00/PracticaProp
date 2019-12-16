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
public class CtrlFitxer {
    
    public InputStream carregarFitxerEntrada(String path_entrada) throws IOException{
        InputStream input = null;
        input = new FileInputStream(path_entrada);
        return input;
    }
    
    public FileOutputStream carregarFitxerSortida(String path_sortida) throws IOException{
        FileOutputStream output = null;
        output = new FileOutputStream(path_sortida);
        return output;
    }
}
