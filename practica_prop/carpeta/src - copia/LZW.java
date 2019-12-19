

import java.io.*;
import java.util.*;


public class LZW extends Algoritme{
   
    DataInputStream in;
    DataOutputStream out;
    InputStream input;
    String text_in = "";

    private int mida_diccionari;
    private int MAX_SIZE = 65536;


    public LZW(InputStream input , int funcio) {
        super(input);
        if(funcio == 0) this.input = input;
        else this.in =  new DataInputStream( new BufferedInputStream(input));
        this.out = new DataOutputStream(output);
    } //contructora descomprimir

    public void llegir_input() throws UnsupportedEncodingException, IOException{
       String cadena;
         BufferedReader i = new BufferedReader(new InputStreamReader(input, "utf-8"));
         while ((cadena = i.readLine()) != null) {
                text_in += cadena;
                text_in += "\r\n";
        }
         i.close(); 
         
    }
        
    public ByteArrayOutputStream comprimir() throws IOException {
        llegir_input();
        Map<String, Short> diccionari = new HashMap<>();
        inicialitzar_diccionari(diccionari);
        mida_diccionari = 255;
        String s = "";
        String sc;
        
        for(int i = 0; i < text_in.length(); ++i){
            char c = text_in.charAt(i);
            sc = s+c;
            if(diccionari.containsKey(sc)){
                s = sc;
            }
            else{
                System.out.println("mida_diccionari: " + mida_diccionari);
                out.writeShort(diccionari.get(s));
                if(mida_diccionari < MAX_SIZE) diccionari.put(sc, (short) mida_diccionari++);
                s = ""+c;
            }
        }

        if(!s.equals("")) {
            out.writeShort(diccionari.get(s));
        }
        return output;

    }
    private static void inicialitzar_diccionari(Map<String, Short> diccionari) {
        for(int i = 0; i < 256; ++i ){
            diccionari.put("" + (char)i, (short) i);
        }
    }

    public  ByteArrayOutputStream descomprimir() throws IOException{


        List<Integer> text_in = new ArrayList<Integer>();
        //poso el text codificat a una llista d'enters
        double punter = 0;
        int afegir = 0;
        while(in.available() > 0){
            if(in.available() == 1) afegir = in.read();
            else afegir = in.readShort();
            text_in.add( afegir);
        }
        in.close();

        //declaro i inicialitzo diccionari
        Map<Integer,String> diccionari = new HashMap<>();
        inicialitzar_diccionari_descomprimir(diccionari);
        mida_diccionari = 255;

        String codViejo = "" + (char)(int) text_in.remove(0);
        StringBuffer textOut = new StringBuffer(codViejo);
        String cadena = null;

        for(int codNuevo : text_in){

            if(diccionari.containsKey(codNuevo)) cadena = diccionari.get(codNuevo);
            else if( codNuevo == mida_diccionari) cadena = codViejo + codViejo.charAt(0);
            textOut.append(cadena);
           if(mida_diccionari < MAX_SIZE) diccionari.put(mida_diccionari++, codViejo + cadena.charAt(0));
            codViejo = cadena;
        }

        System.out.println("textout:" + textOut);
        byte[] o = textOut.toString().getBytes();
        out.write(o);
        System.out.println("mida diccionari:" + mida_diccionari);
        return output;
    }

    private static void inicialitzar_diccionari_descomprimir(Map<Integer, String> diccionari) {
        for(int i = 0; i < 255; ++i ){
            diccionari.put(i,"" + (char)i);
        }

    }
}
