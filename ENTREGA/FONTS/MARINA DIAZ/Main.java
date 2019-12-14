import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        // path + c/d + manual + tipusA
        Scanner sc = new Scanner(System.in);

        String path = sc.nextLine();
        System.out.println(path);

        boolean cd = sc.nextLine().equals("--c");
        System.out.println(cd);

        boolean manual = sc.nextLine().equals("true");
        System.out.println(manual);

        String algoritme = "" ;
        if(manual) algoritme = sc.nextLine();
        try {
            Controlador_fitxer fitxer = new Controlador_fitxer(path, cd, manual, algoritme);
            fitxer.ComprimirDescomprimir();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
