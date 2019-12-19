package com.company;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Estadistiques {

    private DataInputStream in_lz78;
    private DataOutputStream dades_lz78;

    private DataInputStream in_lzw;
    private DataOutputStream dades_lzw;

    private DataInputStream in_lzss;
    private DataOutputStream dades_lzss;

    private DataInputStream in_jpeg;
    private DataOutputStream dades_jpeg;

    private DataOutputStream resultats;

    double mitjanaRati_lz78 = 0;
    double mitjanaVel_lz78 = 0;
    double mitjanaRati_lzw = 0;
    double mitjanaVel_lzw = 0;
    double mitjanaRati_lzss = 0;
    double mitjanaVel_lzss = 0;
    double mitjanaRati_jpeg = 0;
    double mitjanaVel_jpeg = 0;

    int nd1, nd2, nd3, nd4;

    public Estadistiques(DataInputStream din, DataOutputStream don, String algoritme, DataOutputStream resultats) {
        if (algoritme == "lz78") {
            this.in_lz78 = din;
            this.dades_lz78 = don;
            this.nd1 = 0;
        }
        else if (algoritme == "lzw") {
            this.in_lzw = din;
            this.dades_lzw = don;
            this.nd2 = 0;
        }
        else if (algoritme == "lzss") {
            this.in_lzss = din;
            this.dades_lzss = don;
            this.nd3 = 0;
        }
        else if (algoritme == "jpeg") {
            this.in_jpeg = din;
            this.dades_jpeg = don;
            this.nd4 = 0;
        }
        this.resultats = resultats;
    }


    public void editar_estadistica_LZ78(LZ78 c) throws IOException {

        this.nd1 = in_lz78.readInt();
        System.out.println("------------------he llegit el enter "+nd1);

        double midaOriginal = c.get_mida_original();
        double midaComprimit = c.get_mida_comprimit();
        double temps = c.get_temps();
        System.out.println("La mida original es "+midaOriginal+"    la mida comprimit "+midaComprimit+"    i el temps es "+temps);
        double ratiCompressio = midaComprimit / midaOriginal;
        double velocitat = midaOriginal / temps;

        dades_lz78.writeDouble(ratiCompressio);
        dades_lz78.writeDouble(velocitat);
        System.out.println("El rC es "+ratiCompressio+"   i la vel es "+velocitat);
        nd1 += 2;

    }
    /*
    public void editar_estadistica_LZW(LZW c) throws IOException {

        this.nd2 = in_lzw.readInt();

        double midaOriginal = c.get_mida_original();
        double midaComprimit = c.get_mida_comprimit();
        double temps = c.get_temps();
        double ratiCompressio = midaComprimit / midaOriginal;
        double velocitat = midaOriginal / temps;

        dades_lzw.writeDouble(ratiCompressio);
        dades_lzw.writeDouble(velocitat);
        nd2 += 2;
    }

    public void editar_estadistica_LZSS(LZSS c) throws IOException {

        this.nd3 = in_lzss.readInt();

        double midaOriginal = c.get_mida_original();
        double midaComprimit = c.get_mida_comprimit();
        double temps = c.get_temps();
        double ratiCompressio = midaComprimit / midaOriginal;
        double velocitat = midaOriginal / temps;

        dades_lzss.writeDouble(ratiCompressio);
        dades_lzss.writeDouble(velocitat);
        nd3 += 2;
    }

    public void editar_estadistica_JPEG(JPEG c) throws IOException {

        this.nd4 = in_jpeg.readInt();

        double midaOriginal = c.get_mida_original();
        double midaComprimit = c.get_mida_comprimit();
        double temps = c.get_temps();
        double ratiCompressio = midaComprimit / midaOriginal;
        double velocitat = midaOriginal / temps;

        dades_jpeg.writeDouble(ratiCompressio);
        dades_jpeg.writeDouble(velocitat);
        nd4 += 2;
    }
    */

    public void calcular_mitjanes_LZ78() throws IOException {
        double srC = 0;
        double sv = 0;
        double a1, a2;
        ArrayList<Double> aux1 = new ArrayList<Double>();
        ArrayList<Double> aux2 = new ArrayList<Double>();

        System.out.println("el tamany de dades es "+nd1);
        for (int i = 0; i < nd1; i+=2) {
            System.out.println("entro");
            a1 = in_lz78.readDouble();
            aux1.add(a1);
            srC += a1;
            System.out.println("he sumat el srC");
            a2 = in_lz78.readDouble();
            aux2.add(a2);
            sv += a2;
            System.out.println("he sumat la vel");
        }
        mitjanaRati_lz78 = srC/(nd1/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_lz78 = sv/(nd1/2);

        dades_lz78.writeInt(nd1);
        for (int i = 0; i < aux1.size(); i++) {
            dades_lz78.writeDouble(aux1.get(i));
            dades_lz78.writeDouble(aux2.get(i));

        }
    }

    public void calcular_mitjanes_LZW() throws IOException {
        double srC = 0;
        double sv = 0;
        double a1, a2;
        ArrayList<Double> aux1 = new ArrayList<Double>();
        ArrayList<Double> aux2 = new ArrayList<Double>();

        for (int i = 0; i < nd2; i+=2) {
            a1 = in_lzw.readDouble();
            aux1.add(a1);
            srC += a1;
            a2 = in_lzw.readDouble();
            aux2.add(a2);
            sv += a2;
        }
        mitjanaRati_lzw = srC/(nd2/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_lzw = sv/(nd2/2);

        dades_lzw.writeInt(nd2);
        for (int i = 0; i < aux1.size(); i++) {
            dades_lzw.writeDouble(aux1.get(i));
            dades_lzw.writeDouble(aux2.get(i));

        }
    }

    public void calcular_mitjanes_LZSS() throws IOException {
        double srC = 0;
        double sv = 0;
        double a1, a2;
        ArrayList<Double> aux1 = new ArrayList<Double>();
        ArrayList<Double> aux2 = new ArrayList<Double>();

        for (int i = 0; i < nd3; i+=2) {
            a1 = in_lzss.readDouble();
            aux1.add(a1);
            srC += a1;
            a2 = in_lzss.readDouble();
            aux2.add(a2);
            sv += a2;
        }
        mitjanaRati_lzss = srC/(nd3/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_lzss = sv/(nd3/2);

        dades_lzss.writeInt(nd3);
        for (int i = 0; i < aux1.size(); i++) {
            dades_lzss.writeDouble(aux1.get(i));
            dades_lzss.writeDouble(aux2.get(i));

        }
    }

    public void calcular_mitjanes_JPEG() throws IOException {
        double srC = 0;
        double sv = 0;
        double a1, a2;
        ArrayList<Double> aux1 = new ArrayList<Double>();
        ArrayList<Double> aux2 = new ArrayList<Double>();

        for (int i = 0; i < nd4; i+=2) {
            a1 = in_jpeg.readDouble();
            aux1.add(a1);
            srC += a1;
            a2 = in_jpeg.readDouble();
            aux2.add(a2);
            sv += a2;
        }
        mitjanaRati_jpeg = srC/(nd4/2); //la meitat dels elements son ratis i l'altra velocitats
        mitjanaVel_jpeg = sv/(nd4/2);

        dades_jpeg.writeInt(nd4);
        for (int i = 0; i < aux1.size(); i++) {
            dades_jpeg.writeDouble(aux1.get(i));
            dades_jpeg.writeDouble(aux2.get(i));

        }
    }

    public void mostrar_estadistiques() throws IOException {
        String s = "";
        if (nd1 > 0) {
            calcular_mitjanes_LZ78();
            s += "Estadística LZ78:\n  - Mitjana de rati de compressió: "+mitjanaRati_lz78+"\n  - Mitjana de velocitat de compressió: "+mitjanaVel_lz78+" bytes/ns\n\n";
        }
        else s += "Estadística LZ78:\nEncara no s'ha realitzat cap compressió\n\n";

        if (nd2 > 0) {
            calcular_mitjanes_LZW();
            s += "Estadística LZW:\n  - Mitjana de rati de compressió: "+mitjanaRati_lzw+"\n  - Mitjana de velocitat de compressió: "+mitjanaVel_lzw+" bytes/ns\n\n";
        }
        else s += "Estadística LZW:\nEncara no s'ha realitzat cap compressió\n\n";

        if (nd3 > 0) {
            calcular_mitjanes_LZSS();
            s += "Estadística LZSS:\n  - Mitjana de rati de compressió: "+mitjanaRati_lzss+"\n  - Mitjana de velocitat de compressió: "+mitjanaVel_lzss+" bytes/ns\n\n";
        }
        else s += "Estadística LZSS:\nEncara no s'ha realitzat cap compressió\n\n";

        if (nd4 > 0) {
            calcular_mitjanes_JPEG();
            s += "Estadística JPEG:\n  - Mitjana de rati de compressió: "+mitjanaRati_jpeg+"\n  - Mitjana de velocitat de compressió: "+mitjanaVel_jpeg+" bytes/ns";
        }
        else s += "Estadística JPEG:\nEncara no s'ha realitzat cap compressió";

        System.out.println(s);

        resultats.write(s.getBytes());
    }


}
