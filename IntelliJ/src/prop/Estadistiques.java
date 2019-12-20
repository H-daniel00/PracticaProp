package prop;

import java.io.IOException;
import java.util.ArrayList;

public class Estadistiques {

    ArrayList<Double> input;
    double num_compresions;

    double nLZW;
    double nLZSS;
    double nLZ78;
    double nJPEG;

    double mLZW;
    double mLZSS;
    double mLZ78;
    double mJPEG;

    double vLZW;
    double vLZSS;
    double vLZ78;
    double vJPEG;



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

    public ArrayList<Double> mostrarEstadistiques(ArrayList<Double> in) {

        double algoritme;
        double midaO;
        double midaC;
        double temps;
        double radiC;

        Double num_comp = in.get(0);
        int tam = (int) ((num_comp*3)+1);
        for(int i = 1; i< tam ;i +=4){
            System.out.println("tam: " + tam + "num_C: " + num_comp);
            algoritme = in.get(i);
            midaO = in.get(i+1);
            midaC = in.get(i+2);
            temps = in.get(i+3);
            radiC = midaC/midaO;
            if(algoritme == 0) {
                mLZ78 += radiC;
                vLZ78 = midaO/temps;
                nLZ78++;
            }
            if(algoritme == 1) {
                mLZW += radiC;
                vLZW = midaO/temps;
                nLZW++;
            }
            if(algoritme == 0) {
                mLZSS += radiC;
                vLZSS = midaO/temps;
                nLZSS++;
            }
            if(algoritme == 0) {
                mJPEG += radiC;
                vJPEG = midaO/temps;
                nJPEG++;
            }

        }
        vLZ78 = vLZ78/nLZ78;
        vJPEG = vJPEG/nJPEG;
        vLZSS = vLZSS/nLZSS;
        vLZW = vLZW/nLZW;

        mLZ78 = mLZ78/nLZ78;
        mJPEG = mJPEG/nJPEG;
        mLZSS = mLZSS/nLZSS;
        mLZW = mLZW/nLZW;

        ArrayList<Double> o = null;
        o.add(mLZ78);
        o.add(vLZ78);
        o.add(mLZW);
        o.add(vLZW);
        o.add(mLZSS);
        o.add(vLZSS);
        o.add(mJPEG);
        o.add(vJPEG);
        return o;
    }

}
