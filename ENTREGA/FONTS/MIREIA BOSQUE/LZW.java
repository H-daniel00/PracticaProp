import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class LZW {
    InputStream input = null;
    FileOutputStream output = null;

    private int punter;
    private int mida_diccionari;

    public LZW(InputStream i, FileOutputStream o) {
        this.input = i;
        this.output = o;
    }
    private String llegir_input() throws IOException {
        InputStreamReader isreader = new InputStreamReader(input);
        String texto_in = "";
        int data = isreader.read();
        while (data!= -1){
            char ccc = (char) data;
            texto_in += ccc;
            data= isreader.read();
        }
        return texto_in;
    }

    public void comprimir() throws IOException {
        Map<String, Integer> diccionari = new HashMap<>();
        char c;
        String s = "";
        String sc = "";
        String out = "";
        punter = 0;
        inicialitzar_diccionari(diccionari);
        String text_in = llegir_input();
        while (punter < text_in.length()){
            c = text_in.charAt(punter);
            sc = unir(s,c);
            if(!diccionari.containsKey(sc)){
                diccionari.put(sc,diccionari.size()+1);
                String aux = arreglar_out(diccionari.get(s));
                out += aux;
                s = "";
                s += c;
            }
            else s = sc;
            punter++;
        }
        //output.write(diccionari.get(s));
        mida_diccionari = diccionari.size();

        out += arreglar_out(diccionari.get(s));
        System.out.println("sortida: " + out);
        byte[] sortida = out.getBytes();
        System.out.println("s: " + sortida);
        output.write(sortida);
    }
    private String arreglar_out(Integer i) {
        int integer = i;
        String sortida ="";
        int num_digits;
        if(integer/10 < 1) num_digits = 1;
        else if(integer/100 < 1) num_digits = 2;
        else if(integer/1000 < 1) num_digits = 3;
        else if(integer/10000 < 1) num_digits = 4;
        else num_digits = 5;
        if (num_digits == 1) sortida = "0000" + i;
        else if (num_digits == 2) sortida = "000" + i;
        else if (num_digits == 3) sortida = "00" + i;
        else if (num_digits == 4) sortida = "0" + i;
        else if (num_digits == 5)sortida += i;
        return sortida;
    }
    private static void inicialitzar_diccionari(Map<String, Integer> diccionari) {
        for(int i = 0; i < 255; ++i ){
            char a = ' ';
            char aux = (char) (a+i);
            String afegir = "";
            afegir += aux;
            diccionari.put(afegir,i);
        }
    }
    public static String unir(String s, char c){
        String sc = s;
        sc += c;
        return sc;
    }

    public  void descomprimir() throws IOException{
        Map<Integer,String> diccionari = new HashMap<>();
        int cod_viejo = 0;
        int cod_nuevo = 0;
        String cadena = "";
        String caracter = "";
        String texto_out = "";
        punter = 0;
        inicialitzar_diccionari_descomprimir(diccionari);
        String text_in = llegir_input();
        System.out.println("text:" + text_in);

        cod_viejo = llegir_numero(text_in);
        caracter = traducir(cod_viejo,diccionari);
        texto_out += caracter;
        while(punter < text_in.length()-3){
            cod_nuevo = llegir_numero(text_in);
            boolean  esta_diccionari = buscar_d(cod_nuevo,diccionari);
            if (!esta_diccionari) {
                cadena = traducir(cod_viejo,diccionari);
                cadena += caracter;
            }
            else cadena = traducir(cod_nuevo,diccionari);
            texto_out += cadena;
            String primer_element = "";
            primer_element += cadena.substring(0,1);
            caracter = primer_element;
            String afegir =  traducir(cod_viejo,diccionari) + caracter;
            diccionari.put((diccionari.size()+1),afegir);
            cod_viejo = cod_nuevo;
        }

        byte[] contentInBytes = texto_out.getBytes();
        output.write(contentInBytes);



    }
    private int llegir_numero(String text_in) {
        int sencer = 0;
        int lec = 0;
        for(int i = 0; i < 5; ++i){
            char l = text_in.charAt(punter);
            String le = Character.toString(l);
            lec = Integer.parseInt(le);
            lec = lec + sencer*10;
            sencer = lec;
            ++punter;
        }
        return lec;
    }
    public static String traducir(Integer cod_viejo, Map<Integer, String> diccionari){
        String trad = diccionari.get(cod_viejo);
        return trad;
    }
    public static boolean buscar_d(Integer cod_nuevo, Map<Integer, String> diccionari){
        boolean trobat = diccionari.containsKey(cod_nuevo);
        return trobat;
    }
    private static void inicialitzar_diccionari_descomprimir(Map<Integer, String> diccionari) {
        for(int i = 0; i < 255; ++i ){
            char a = ' ';
            char aux = (char) (a+i);
            String afegir = "";
            afegir += aux;
            diccionari.put(i,afegir);
        }
    }
}
