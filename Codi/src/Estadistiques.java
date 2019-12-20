

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Estadistiques {

    ArrayList<Double> input;
    double num_compresions;

    double mitjanaRati_lz78 = 0;
    double mitjanaVel_lz78 = 0;
    double mitjanaRati_lzw = 0;
    double mitjanaVel_lzw = 0;
    double mitjanaRati_lzss = 0;
    double mitjanaVel_lzss = 0;
    double mitjanaRati_jpeg = 0;
    double mitjanaVel_jpeg = 0;

    public Estadistiques(ArrayList<Double> in) throws IOException {
        this.input = in;
        System.out.println("inputEstadistiques: " + input);
    }


    public ArrayList<Double> editar_estadistica(double midaO, double midaC, double temps, int algoritme ) throws IOException {

        this.num_compresions = input.get(0);
        double ratiCompressio = midaC / midaO;
        double velocitat = midaO / temps;

        this.input.set(0, num_compresions++);
        this.input.add((double) algoritme);
        this.input.add (ratiCompressio);
        this.input.add(velocitat);

        return this.input;

    }
   /* public ArrayList<Double> mostrar_estadistiques(){

    }

    public void calcular_mitjanes_LZ78(ArrayList<Double> in) throws IOException {
        double srC = 0;
        double sv = 0;
        double n = in.get(0);

        for (int i = 0; i < n; i+=2) {
            srC += in.get(i);
            sv += in.get(i+1);
        }
        mitjanaRati_lz78 = srC/(n/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_lz78 = sv/(n/2);
    }

    public void calcular_mitjanes_LZW(ArrayList<Double> in) throws IOException {
        double srC = 0;
        double sv = 0;
        double n = in.get(0);

        for (int i = 0; i < n; i+=2) {
            srC += in.get(i);
            sv += in.get(i+1);
        }
        mitjanaRati_lzw = srC/(n/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_lzw = sv/(n/2);
    }

    public void calcular_mitjanes_LZSS(ArrayList<Double> in) throws IOException {
        double srC = 0;
        double sv = 0;
        double n = in.get(0);

        for (int i = 0; i < n; i+=2) {
            srC += in.get(i);
            sv += in.get(i+1);
        }
        mitjanaRati_lzss = srC/(n/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_lzss = sv/(n/2);
    }

    public void calcular_mitjanes_JPEG( ArrayList<Double> in) throws IOException {
        double srC = 0;
        double sv = 0;
        double n = in.get(0);

        for (int i = 0; i < n; i+=2) {
            srC += in.get(i);
            sv += in.get(i+1);
        }
        mitjanaRati_jpeg = srC/(n/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_jpeg = sv/(n/2);
    }*/
}
