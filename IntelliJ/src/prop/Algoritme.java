package prop;

import java.io.*;

public abstract class Algoritme {

    public InputStream input;
    public ByteArrayOutputStream output;

    protected double midaO;
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
        return output.size();
    }

    public  double getMidaO(){
        return midaO;
    }
}
