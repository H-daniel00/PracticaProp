import java.io.*;
import java.util.Scanner;

public class JpegDriver {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        int cd = 0;
        while(cd ==0) {
            System.out.println("Comprimir / Descomprimir? (c/d)");
            String c = sc.nextLine();
            if (c.equals("c")) cd = 1;
            if (c.equals("d")) cd = 2;
        }
        try {
            InputStream input = new FileInputStream(path);
            String path_out = "";
            if(cd == 1) path_out = "sortida.jpg";
            if(cd == 2) path_out = "sortida.ppm";
            FileOutputStream output = new FileOutputStream(path_out);
            JPEG foto = new JPEG(input, output);
            if(cd == 1){
                foto.comprimir();
                System.out.println("Arxiu comprimit a: "+path_out);
            }
            if(cd == 2){
                foto.descomprimir();
                System.out.println("Arxiu descomprimit a: "+path_out);
            }
        }catch(NullPointerException e){
            System.out.println("Excepció no controlada (Null pointer)");
        }catch(FileNotFoundException e ){
            System.out.println("No existeix el fitxer");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


}
