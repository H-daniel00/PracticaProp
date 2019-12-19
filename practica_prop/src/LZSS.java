
import java.io.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileOutputStream;

public class LZSS extends Algoritme {

    DataInputStream in;
    DataOutputStream out;
    InputStream input;
    String text_in = "";

    private int cantidad = 1;
    private int nums = 0;
    private byte temporal = 0;
    private int contadormax = 0;

    public LZSS(InputStream input, int funcio) {
        super(input);
        if (funcio == 0) this.input = input;
        else this.in = new DataInputStream(new BufferedInputStream(input));
        this.out = new DataOutputStream(output);
    }

    public void llegir_input() throws IOException {
        String cadena;
        BufferedReader i = new BufferedReader(new InputStreamReader(this.input, "utf-8"));
        int aux = 0;
        while ((cadena = i.readLine()) != null) {
            if (aux != 0) text_in += "\n";
            text_in += cadena;
            aux++;
        }
        i.close();
    }

    private void comproba() throws IOException {
        ++cantidad;
        if (cantidad == 9) {
            cantidad = 1;
            out.writeByte(temporal);
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
        } else {
            while (numdec > 1) {
                retorna.push(numdec % 2);
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
            if (1 == (int) numsbin.peek()) {
                sumael2 = (int) Math.pow(2, (8 - cantidad));
                temporal = (byte) (temporal | sumael2);
            }
            comproba();
            numsbin.pop();
            --nums;
        }
    }

    private int trobaiguals(int i, int j) throws IOException {
        int contador = 0;
        int r = -1;
        if (text_in.charAt(j) == text_in.charAt(i)) {
            contador = 1;
            int m = j + 1;
            int n = i + 1;
            while (contador < 33 && n < text_in.length() && text_in.charAt(n) == text_in.charAt(m)) {
                ++contador;
                ++n;
                ++m;
            }
            if (contador > contadormax) {
                r = j;
                contadormax = contador;
            }
        }
        return r;
    }

    public void comprimir() throws IOException {
        llegir_input();
        char temp;
        int r;
        for (int i = 0; i < text_in.length(); ++i) {
            contadormax = 0;
            nums = 0;
            r = 0;
            if (i > 0) {
                if (i <= 8190) {
                    for (int j = i - 1; j >= 0; --j) {
                        int lol = trobaiguals(i, j);
                        if (lol != -1) r = lol;
                    }
                } else {
                    for (int j = i - 1; j >= (i - 8190); --j) {
                        int lol = trobaiguals(i, j);
                        if (lol != -1) r = lol;
                    }
                }
            }
            if (contadormax >= 2) {
                int sumael1 = (int) Math.pow(2, (8 - cantidad));
                temporal = (byte) (temporal | sumael1);
                comproba();
                Stack numsbin;
                numsbin = donanumbin(i - r);
                ficalallista(numsbin, 13);
                numsbin = donanumbin(contadormax - 2);
                ficalallista(numsbin, 5);
                i += contadormax - 1;
            } else {
                temp = text_in.charAt(i);
                comproba();
                Stack numsbin = donanumbin(temp);
                ficalallista(numsbin, 16);
            }
        }
        if (cantidad != 1) out.writeByte(temporal);
        return output;
    }

    public void descomprimir() throws IOException {
        String descomprimido = "";
        byte flag = 0;
        short letra = 0;
        short distancia = 0;
        short tam = 0;
        byte util = 1;
        int puntero = 0;
        int contador = 0;
        int afegir = 0;
        byte[] comprimido = new byte[in.available()];
        int x = 0;
        while (in.available() > 0) {
            afegir = in.read();
            comprimido[x] = (byte) afegir;
            ++x;
        }
        in.close();
        while (puntero < comprimido.length - 2) {
            flag = (byte) (((comprimido[puntero] & 0xFF) >> (8 - cantidad)) & util);
            puntero = comproba2(puntero);
            int numeros = 0;
            if (flag == 0 && (puntero < (comprimido.length - 2))) {
                numeros = 16;
                letra = 0;
                short tem;
                while (numeros > 0) {
                    tem = (byte) ((((byte) (comprimido[puntero] & 0xFF)) >> (8 - cantidad)) & util);
                    tem = (short) (tem << (numeros - 1));
                    letra = (short) (tem | letra);
                    puntero = comproba2(puntero);
                    --numeros;
                }
                descomprimido += (char) letra;
                ++contador;
            } else if (puntero < (comprimido.length - 2)){
                numeros = 18;
                distancia = 0;
                tam = 0;
                short tem = 0;
                while (numeros > 0) {
                    if (numeros == 5) tem = 0;
                    tem = (byte) ((((byte) (comprimido[puntero] & 0xFF)) >> (8 - cantidad)) & util);
                    if (numeros > 5) {
                        tem = (short) (tem << (numeros - 6));
                        distancia = (short) (tem | distancia);
                    } else {
                        tem = (byte) (tem << (numeros - 1));
                        tam = (byte) (tem | tam);
                    }
                    puntero = comproba2(puntero);
                    --numeros;
                }
                int por = contador - distancia;
                for (int k = 0; k < tam + 2; ++k) {
                    descomprimido += descomprimido.charAt(por);
                    ++por;
                    ++contador;
                }
            }
        }
        byte[] ceb = descomprimido.getBytes();
        this.out.write(ceb);
        return output;
    }
}

