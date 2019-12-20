import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZ78 extends Algoritme{

    private DataInputStream in = null;
    private DataOutputStream out;
    private InputStream input;
    private String text_in = "";

    private int MAX_SIZE = 32767;


    public LZ78(InputStream input, int funcio) {
        super(input);
        if (funcio == 0) this.input = input;
        else this.in =  new DataInputStream( new BufferedInputStream(input));
        this.out = new DataOutputStream(output);
    }

    private void llegir_input() throws IOException{
        String cadena;
        BufferedReader i = new BufferedReader(new InputStreamReader(input, "utf-8"));
        int aux = 0;
        while ((cadena = i.readLine()) != null) {
            if (aux != 0) text_in += "\n";
            text_in += cadena;
            aux++;
        }
        i.close();

    }


    public ByteArrayOutputStream comprimir() throws IOException {

        midaO = input.available();
        long time_ini = System.nanoTime();

        llegir_input();
        Map<String, Short> diccionari = new HashMap<>();
        String s = "";
        int index = 0;
        String saux;

        for (int i = 0; i < text_in.length(); ++i) {
            char c = text_in.charAt(i);
            saux = s;
            s += c;
            if (!diccionari.containsKey(s)) {
                if (diccionari.size() < MAX_SIZE) {
                    diccionari.put(s, (short) (diccionari.size()+1));
                    if (s.length() == 1) index = 0;
                    out.writeShort(index);
                    out.writeShort((short) c);
                }
                else {
                    out.writeShort(diccionari.get(saux));
                    out.writeShort((short) c);
                }
                s = "";
            }
            else index =  diccionari.get(s);
        }
        if (!s.isEmpty()) out.writeShort(index);

        long time_fin = System.nanoTime();
        temps = time_fin - time_ini;
        return output;
    }


    public ByteArrayOutputStream descomprimir() throws IOException {

        List<Integer> text_in = new ArrayList<>();
        int afegir;
        while (in.available() > 0) {                          //poso el text codificat a una llista d'enters
            if (in.available() == 1) afegir = in.read();
            else afegir = in.readShort();
            text_in.add(afegir);
        }
        in.close();

        Map<Integer, String> diccionari = new HashMap<>();
        String s;
        char c;
        int index = 0;
        int id,ca;
        boolean ultim = false;

        StringBuffer text_out = new StringBuffer(0);
        int tam_original = text_in.size();
        for (int i = 0; i < tam_original; i++) {
            if (text_in.size()-1 == 0) ultim = true;
            id = text_in.remove(0);
            if (ultim) {
                s = diccionari.get(id);
                text_out.append(s);
            }
            else {
                ca = text_in.remove(0);
                c = (char) ca;
                s = "";
                index++;
                if (id == 0) {
                    s += c;
                    if (diccionari.size() < MAX_SIZE) diccionari.put(index,s);
                    text_out.append(s);
                }
                else {
                    s += diccionari.get(id) + c;
                    if (diccionari.size() < MAX_SIZE) diccionari.put(index,s);
                    text_out.append(s);
                }
                i++;
            }
        }

        byte[] o = text_out.toString().getBytes();
        out.write(o);
        return output;
    }

}