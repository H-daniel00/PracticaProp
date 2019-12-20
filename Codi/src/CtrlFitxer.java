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
    CtrlCarpeta ControladorCarpeta;

    DataInputStream Estadistica_input;
    DataOutputStream Estadistica_output;


    public CtrlFitxer(CtrlDomini ctrlDomini) {
        this.domini = ctrlDomini;
        this.ControladorCarpeta = new CtrlCarpeta(this);
    }


    public ByteArrayOutputStream comprimir(InputStream input, int algoritme) throws IOException {
        return domini.comprimir(input,algoritme);
    }

    public ByteArrayOutputStream descomprimir(InputStream input, int algoritme) throws IOException {
        return domini.descomprimir(input,algoritme);
    }

    public int algoritmeAutomatic(String path,int funcio){
        return domini.algoritme_automatic(path,funcio);
    }


    public FileInputStream carregarFitxerEntrada(String path_entrada) throws IOException{
        FileInputStream input = new FileInputStream(path_entrada);
        return input;
    }

    public void escriureFitxerSortida(ByteArrayOutputStream stream,String path_sortida) throws IOException{
        byte[] data = stream.toByteArray();
        FileOutputStream output = new FileOutputStream(path_sortida);
        for(byte b: data){
            output.write(b);
        }
    }

    public ArrayList<Double> carregarFitxersEstadistica(int algoritme) throws IOException{
        Estadistica_output = new DataOutputStream(new FileOutputStream("DadesEstadistica.txt"));
        File  f = new File("DadesEstadistica.txt");
        if (!f.exists()) {
            Estadistica_output.writeDouble(0);
        }
        Estadistica_input = new DataInputStream(new FileInputStream("DadesEstadistica.txt"));
        ArrayList<Double> in = llegir(Estadistica_input);
        return in;
    }

    public ArrayList<Double> llegir(DataInputStream d) throws IOException {
        ArrayList<Double> in = new ArrayList<>();
        double n = d.readDouble();
        in.add(n);
        System.out.println("n: " + n);
        for(int i = 1; i < n; ++i ){
            in.add(d.readDouble());
        }

        return in;
    }

    public void EscriureDadesEstadistica(ArrayList<Double> o) throws IOException {
        int tam = o.size();
        for(int i = 0; i <= tam ; ++i){
            Estadistica_output.writeDouble(o.get(i));
        }
    }

}
