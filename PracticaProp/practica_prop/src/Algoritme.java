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

    protected double midaO;
    protected double midaC;
    protected double temps;

    public Algoritme(InputStream input){
        this.input = input;
        this.output = new ByteArrayOutputStream();
    }

    abstract ByteArrayOutputStream comprimir() throws IOException;
    abstract ByteArrayOutputStream descomprimir() throws IOException;

    public  double getTemps(){
        return temps;
    }

    public double getMidaC() {
        return midaC;
    }

    public  double getMidaO(){
        return midaO;
    }
}
