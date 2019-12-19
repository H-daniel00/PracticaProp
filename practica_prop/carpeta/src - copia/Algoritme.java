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
    ByteArrayOutputStream output;
    
    public Algoritme(InputStream input){
        this.input = input;
        this.output = new ByteArrayOutputStream();
    }
    
    abstract ByteArrayOutputStream comprimir() throws IOException;
    abstract ByteArrayOutputStream descomprimir() throws IOException;
}
