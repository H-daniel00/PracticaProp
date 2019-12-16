

import java.io.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileOutputStream;

public class LZSS {

    private FileInputStream input = null;
    private FileOutputStream output = null;


    private String texto = "";
    private int cantidad = 1;
    private int nums = 0;
    private byte temporal = 0;
    private int contadormax = 0;

    private void comproba() throws IOException {
        ++cantidad;
        if (cantidad == 9) {
            cantidad = 1;
            output.write(temporal);
            temporal = 0;
        }
    }

    private int comproba2(int puntero) {
        ++cantidad;
        if (cantidad == 9) {
            cantidad = 1;
            ++puntero;
        }
        return puntero;
    }

    private Stack donanumbin(int numdec) {
        Stack retorna = new Stack();
        if (numdec == 0) {
            nums = 0;
        }
        else {
            while (numdec > 1) {
                retorna.push(numdec%2);
                numdec /= 2;
                ++nums;
            }
            retorna.push(1);
            ++nums;
        }
        return retorna;
    }

    private void ficalallista(Stack numsbin, int tipo) throws IOException {
        for (int q = tipo; q > nums; --q) {
            comproba();
        }
        int sumael2;
        while (nums > 0) {
            if (1 == (int)numsbin.peek()) {
                sumael2 = (int)Math.pow(2, (8-cantidad));
                temporal = (byte) (temporal |sumael2);
            }
            comproba();
            numsbin.pop();
            --nums;
        }
    }

    private int trobaiguals(int i, int j) throws IOException {
        int contador = 0;
        int r = -1;
        if (texto.charAt(j) == texto.charAt(i)) {
            contador = 1;
            int m = j + 1;
            int n = i + 1;
            while (contador < 34 && n < texto.length() && texto.charAt(n) == texto.charAt(m)) {
                ++contador;
                ++n;
                ++m;
            }
            if (contador > contadormax) {
                r= j;
                contadormax = contador;
            }
            if (contadormax == 35) j = 0;
        }
        return r;
    }

    public void comprimir() throws IOException {
        char temp;
        int r;
        while (input.available() > 0) {
            texto += (char)input.read();
        }
        for (int i = 0; i < texto.length(); ++i) {
            contadormax = 0;
            nums = 0;
            r  = 0;
            if (i > 0) {
                if (i <= 8195) {
                    for (int j = i - 1; j >= 0; --j) {
                        int lol = trobaiguals(i, j);
                        if (lol != -1) r = lol;
                    }
                }
                else {
                    for (int j = i - 1; j >= i-8196; --j) {
                        int lol = trobaiguals(i, j);
                        if (lol != -1) r = lol;
                    }
                }
            }
            if (contadormax >= 3) {
                int sumael1 = (int)Math.pow(2, (8-cantidad));
                temporal = (byte) (temporal | sumael1);
                comproba();
                Stack numsbin;
                numsbin = donanumbin(i-r);
                ficalallista(numsbin, 13);
                numsbin = donanumbin(contadormax - 3);
                ficalallista(numsbin,  5);
                i += contadormax - 1;
            }
            else {
                temp = texto.charAt(i);
                comproba();
                Stack numsbin = donanumbin(temp);
                ficalallista(numsbin, 8);
            }
        }
        if (cantidad != 1) output.write(temporal);

    }

    public void descomprimir() throws IOException {
        byte flag = 0;
        byte letra = 0;
        short distancia = 0;
        short tam = 0;
        byte util = 1;
        int j = 0;
        int puntero= 0;
        int contador= 0;
        ByteArrayOutputStream comp = new ByteArrayOutputStream();
        while ( (j= input.read()) != -1) {
            comp.write(((byte)j));
        }
        byte[] comprimido = comp.toByteArray();

        while (puntero < comprimido.length -1) {
            flag = (byte) (((comprimido[puntero] & 0xFF)>> (8 -cantidad)) & util);
            puntero = comproba2(puntero);
            int numeros = 0;
            if (flag == 0 && (puntero < (comprimido.length -1))) {
                numeros = 8;
                letra = 0;
                if (cantidad == 1) {
                    letra = (byte) (comprimido[puntero] & 0xFF);
                    ++puntero;
                }
                else {
                    byte tem;
                    while (numeros > 0) {
                        tem = (byte) ((((byte) (comprimido[puntero] & 0xFF)) >> (8-cantidad)) & util);
                        tem = (byte) (tem << (numeros-1));
                        letra = (byte) (tem |letra);
                        puntero = comproba2(puntero);
                        --numeros;
                    }
                }
                texto += (char)letra;
                ++contador;
            }
            else {
                numeros = 18;
                distancia = 0;
                tam = 0;
                short tem = 0;
                while (numeros > 0) {
                    if (numeros == 5) tem = 0;
                    tem = (byte) ((((byte) (comprimido[puntero] & 0xFF)) >> (8-cantidad)) & util);
                    if (numeros > 5) {
                        tem = (short) (tem << (numeros-6));
                        distancia = (short) (tem | distancia);
                    }
                    else {
                        tem = (byte) (tem << (numeros-1));
                        tam = (byte) (tem | tam);
                    }
                    puntero = comproba2(puntero);
                    --numeros;
                }
                int por = contador - distancia;
                for (int k = 0; k < tam + 3; ++k) {
                    texto += texto.charAt(por);
                    ++por;
                    ++contador;
                }
            }
        }
        byte[] ceb = texto.getBytes();
        output.write(ceb);
    }

    public LZSS(FileInputStream input, FileOutputStream output) {
        this.input =  input;
        this.output = output;
    }
}
