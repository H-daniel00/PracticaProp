package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {



    public static void comprovacio(String path_comprovacio, String path_sortida) throws IOException {
        FileReader fr1 = new FileReader(path_comprovacio);
        FileReader fr2 = new FileReader(path_sortida);

        BufferedReader br1 = new BufferedReader(fr1);
        BufferedReader br2 = new BufferedReader(fr2);

        String sCadena1 = br1.readLine();
        String sCadena2 = br2.readLine();
        Boolean iguals = true;

        while ((sCadena1 != null) && (sCadena2 != null) && iguals) {
            if (!sCadena1.equals(sCadena2)) iguals = false;
            sCadena1 = br1.readLine();
            sCadena2 = br2.readLine();
        }
        if (iguals && (sCadena1 == null) && (sCadena2 == null)) System.out.println("Els fitxers son iguals");
        else System.out.println("Els fitxers son diferents");
    }

    public static void main(String[] args) throws IOException {

        String path_entrada;
        String path_sortida;
        String path_comprovacio;
        String funcio;

        Scanner sc = new Scanner(System.in);
        System.out.println("Vol comprimir o descomprimir? (c/d)");
        funcio = sc.nextLine();

        if(funcio.equals("c")) path_sortida = "sortida.lz78";
        else if (funcio.equals("d")) path_sortida = "sortida.txt";
        else throw new IOException("Ordre incorrecta");

        try {
            System.out.println("Introdueixi el fitxer que vol comprimir/descomprimir");
            path_entrada = sc.nextLine();

            FileOutputStream output = new FileOutputStream(path_sortida);


            if (funcio.equals("c")) {
                FileInputStream input = new FileInputStream(path_entrada);
                LZ78 text = new LZ78(input,output,"c");
                text.comprimir();
                System.out.println("Arxiu comprimit a: "+path_sortida);

                /*Estadistiques est = Estadistiques.getInstancia_lz78(text);
                est.getInstancia_lz78(text);
                est.getInstancia_lz78(text);
                //est.editar_estadistica_LZ78(text);
                //est.mostrar_estadistiques();*/

                DataInputStream din = new DataInputStream(new FileInputStream("d.txt"));
                DataOutputStream don = new DataOutputStream(new FileOutputStream("d.txt"));
                DataOutputStream resultats = new DataOutputStream(new FileOutputStream("resultats.txt"));
                //if (inici == "inici")
                don.writeInt(0);
                Estadistiques2 est2 = new Estadistiques2(din,don,"lz78", resultats);
                est2.editar_estadistica_LZ78(text);
                est2.mostrar_estadistiques();

                est2.editar_estadistica_LZ78(text);
                est2.mostrar_estadistiques();

                est2.editar_estadistica_LZ78(text);
                est2.mostrar_estadistiques();
            }
            else {
                System.out.println("Amb quin fitxer vol comparar la sortida?");
                path_comprovacio = sc.nextLine();
                FileInputStream input = new FileInputStream(path_entrada);
                LZ78 text = new LZ78(input, output,"d");
                text.descomprimir();
                System.out.println("Arxiu descomprimit a: "+path_sortida);
                comprovacio(path_comprovacio, path_sortida);
            }
        } catch(NullPointerException e){
            System.out.println("Excepció no controlada (Null pointer)");
        } catch(FileNotFoundException e ) {
            System.out.println("No existeix el fitxer");
        }
    }
}
