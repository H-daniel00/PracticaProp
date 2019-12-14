

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LZ78 {

    private FileInputStream input = null;
    private FileOutputStream output = null;

    int punter;

    LZ78(FileInputStream input, FileOutputStream output) {
        this.input = input;
        this.output = output;

    }

    private String llegir_input() throws IOException {
        InputStreamReader isreader = new InputStreamReader(input);
        String text_in = "";
        int data = isreader.read();
        while (data != -1) {
            char ccc = (char) data;
            text_in += ccc;
            data = isreader.read();
        }
        return text_in;
    }

    private String longitud_fixa_index(Integer i) {
        int integer = i;
        String sortida ="";
        int num_digits;
        if(integer/10 < 1) num_digits = 1;
        else if(integer/100 < 1) num_digits = 2;
        else if(integer/1000 < 1) num_digits = 3;
        else if(integer/10000 < 1) num_digits = 4;
        else if(integer/100000 < 1) num_digits = 5;
        else num_digits = 6;
        if (num_digits == 1) sortida = "00000" + i;
        else if (num_digits == 2) sortida = "0000" + i;
        else if (num_digits == 3) sortida = "000" + i;
        else if (num_digits == 4) sortida = "00" + i;
        else if (num_digits == 5) sortida = "0" + i;
        else if (num_digits == 6)sortida += i;
        return sortida;
    }

    private String longitud_fixa_caracter(Integer i) {
        int integer = i;
        String sortida ="";
        int num_digits;
        if(integer/10 < 1) num_digits = 1;
        else if(integer/100 < 1) num_digits = 2;
        else num_digits = 3;
        if (num_digits == 1) sortida = "00" + i;
        else if (num_digits == 2) sortida = "0" + i;
        else if (num_digits == 3)sortida += i;
        return sortida;
    }

    private int llegir_num_index(String text_in) {
        int sencer = 0;
        int lec = 0;
        for(int i = 0; i < 6; ++i){
            char l = text_in.charAt(punter);
            String le = Character.toString(l);
            lec = Integer.parseInt(le);
            lec = lec + sencer*10;
            sencer = lec;
            ++punter;
        }
        return lec;
    }

    private int llegir_num_caracter(String text_in) {
        int sencer = 0;
        int lec = 0;
        for(int i = 0; i < 3; ++i){
            char l = text_in.charAt(punter);
            String le = Character.toString(l);
            lec = Integer.parseInt(le);
            lec = lec + sencer*10;
            sencer = lec;
            ++punter;
        }
        return lec;
    }


    public void comprimir() throws IOException {
        Map<String, Integer> diccionari = new HashMap<String, Integer>();
        String s = "";
        String aux;
        String text_out = "";
        char c;
        int index = 0;
        punter = 0;

        String text = llegir_input();
        while (punter < text.length()-1) {
            c = text.charAt(punter);
            s += c;
            if (!diccionari.containsKey(s)) {
                diccionari.put(s, diccionari.size()+1);
                if (s.length() == 1) index = 0;

                aux = longitud_fixa_index(index);
                text_out += aux;

                aux = longitud_fixa_caracter((int)c);
                text_out += aux;

                s = "";
            }
            else index =  diccionari.get(s);
            punter++;
        }
        if (!s.isEmpty()) {
            aux = longitud_fixa_index(index);
            text_out += aux;
        }
        byte[] ceb = text_out.getBytes();                   //contingut en bytes
        output.write(ceb);
    }


    public void descomprimir() throws IOException {
        Map<Integer, String> diccionari = new HashMap<Integer, String>();
        String s = "";
        String text_out = "";
        char c;
        int index = 0;
        int id, ca;
        Boolean ultim = false;
        punter = 0;

        String text = llegir_input();
        while (punter < text.length()) {
            if ((punter + 6) == text.length()) ultim = true;
            id = llegir_num_index(text);
            if (ultim) text_out += diccionari.get(id);
            else {
                ca = llegir_num_caracter(text);
                c = (char) ca;
                s = "";
                index++;
                if (id == 0) {
                    s += c;
                    diccionari.put(index,s);
                    text_out += s;
                }
                else {
                    s += diccionari.get(id) + c;
                    diccionari.put(index,s);
                    text_out += s;
                }
            }
        }
        byte[] ceb = text_out.getBytes();                   //contingut en bytes
        output.write(ceb);
        System.out.println(diccionari.size());
    }


}
