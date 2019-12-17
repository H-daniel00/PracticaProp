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
abstract class Algoritme {
    InputStream input;
    OutputStream output;
    
    public Algoritme(InputStream input, OutputStream output){
        this.input = input;
        this.output = output;
    }
    
    abstract void comprimir() throws IOException;
    abstract void descomprimir() throws IOException;
}
