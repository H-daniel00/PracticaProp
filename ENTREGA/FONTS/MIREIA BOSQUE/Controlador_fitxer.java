import java.io.*;

public class Controlador_fitxer {

    private String path_entrada ;
    private String path_sortida ;
    private boolean cd;
    private String algoritme ;

    Controlador_fitxer(String path, boolean cd, boolean manual,String algoritme) throws IOException{
        path_entrada  = path;
        this.cd = cd;
        if(manual) this.algoritme= algoritme;
        else{
            this.algoritme = get_algoritme_automatic();
        }

        path_sortida = get_path_sortida(path);
        System.out.println(path_sortida);
        System.out.println(path_entrada);
    }

    private String get_path_sortida(String path) throws IOException{
        if(cd) {
            if (path.endsWith(".ppm")) {
                return("sortida.jpg");
            } else if(algoritme.equals("LZW")){
                return("sortida.lzw");
            }
            else if(algoritme.equals("LZ78")){
                return("sortida.lz78");
            }
            else if(algoritme.equals("LZSS")){
                return("sortida.lzss");
            }

        }else{
            if(path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg") ){
                return("sortida.ppm");
            }else{
                return("sortida.txt");
            }
        }
        throw new IOException("Entrada incorrecta");
    }

    private String get_algoritme_automatic()throws  IOException{
        if(cd){
            if(path_entrada.endsWith(".ppm")) return("JPEG");
            else if(path_entrada.endsWith(".txt")) return("LZW");
            //Añadir el resto
        }else {
            if (path_entrada.endsWith(".jpg")) return ("JPEG");
            else if (path_entrada.endsWith(".lzw")) return ("LZW");
            //Añadir el resto
        }
        throw new IOException("Error al assignar algorisme automàtic");
    }
    void ComprimirDescomprimir() throws IOException {
        FileInputStream input = new FileInputStream(path_entrada);
        if(input == null){
            throw new IOException("No existeix el fitxer");
        }
        FileOutputStream output = new FileOutputStream(path_sortida);
        if(algoritme.equals("LZW")) {
            LZW text = new LZW( input, output);
            if (cd) text.comprimir();
            else text.descomprimir();
        }
        if(algoritme.equals("LZ78")) {
            LZ78 text = new LZ78( input, output);
            if (cd) text.comprimir();
            else text.descomprimir();
        }
        if(algoritme.equals("JPEG")) {
            JPEG text = new JPEG(input, output);
            if (cd) text.comprimir();
            else text.descomprimir();
        }
        if(algoritme.equals("LZSS")) {
           LZSS text = new LZSS(input, output);
            if (cd) text.comprimir();
            else text.descomprimir();
        }


    }
}
//comprimir = 0 , descomprimir = 1
//LZ78 = 0 , LZSS = 1, LZW = 2, JPEG = 3


