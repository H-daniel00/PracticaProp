

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

public class CtrlCarpeta{

    CtrlFitxer ctrlFitxer;

    public CtrlCarpeta(CtrlFitxer ctrlFitxer) {
        this.ctrlFitxer = ctrlFitxer;
    }

    public static void main(String[] args){
        try {
            CtrlCarpeta folderDriver = new CtrlCarpeta(new CtrlFitxer(new CtrlDomini(0,"hola",1)));
            Scanner sc = new Scanner(System.in);
            System.out.println("Path:");
            String path = sc.nextLine();
            String op;
            System.out.println("Comprimir / Descomprimir? (c/d)");
            do {
                op = sc.nextLine();
            } while (!op.equals("d") && !op.equals("c"));
            if (op.equals("d")) {
                FileInputStream input = new FileInputStream(path);
                System.out.println("Path Out:");
                String pathOut = sc.nextLine();
                folderDriver.decompress(input, pathOut);
            } else { //Comprimir
                System.out.println("Path Out:");
                String pathOut;
                do {
                    pathOut = sc.nextLine();
                }while(!pathOut.endsWith(".tar"));
                FileOutputStream output = new FileOutputStream(pathOut);
                folderDriver.compress(path);
            }
        }catch(Exception e){
            System.out.println("Error");
        }
    }

    void decompress(InputStream input,String path_out) throws IOException{
        int temp;
        path_out = path_out.replace("\\","/");
        String[] separatedPath = path_out.split("/");
        StringBuilder auxPath = new StringBuilder();
        for(int i = 0; i<separatedPath.length-1; i++){
            auxPath.append(separatedPath[i]);
            if(i != separatedPath.length-2){
                auxPath.append("/");
            }
        }
        String auxPathResult = auxPath.toString();
        File auxFolder = new File(path_out);
        if(!auxFolder.exists()){
            File rootFolder = new File(auxPathResult);
            if(!rootFolder.exists() || !rootFolder.isDirectory()) throw new IOException("Can't find output path");
            if(!auxFolder.mkdir()) throw new IOException("Can't create output folder");
        }
        while((temp = input.read()) != -1) {
            String path = "";
            for (int i = 0; i < 100; i++) {
                if (i != 0) {
                    if ((temp = input.read()) == -1) break;
                }
                if (temp != 0) path += (char) temp;
            }
            System.out.println(path);
            int type = input.read();
            System.out.println(type);
            if(type == 0) {
                byte[] size = new byte[4];
                int fileSize = 0;
                for (int b = 0; b < 4; b++) {
                    size[b] = (byte)input.read();
                    fileSize |= ((size[b]&0xFF) << (8 * b)) ;
                }
                System.out.println(fileSize);
                ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
                for (int i = 0; i < fileSize; i++) {
                    tempOut.write(input.read());
                }
                ByteArrayInputStream fileIn = new ByteArrayInputStream(tempOut.toByteArray());

                FileOutputStream fileOut = new FileOutputStream(path_out+"/"+path);
                fakeCompress(fileIn,fileOut);
            }if(type == 4){
                File folder = new File(path_out+"/"+path);
                folder.mkdir();
            }
        }
    }


    ByteArrayOutputStream compress(String path) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        final File folder = new File(path);
        listFilesForFolder("",folder,output);
        return output;
    }

    void listFilesForFolder(String rel,final File folder,OutputStream output) throws IOException {
        String path = "";
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            byte type = 0;
            if (fileEntry.isDirectory()) {
                path = rel +fileEntry.getName() +"/";
                type = 4;
                writeHeader(rel+fileEntry.getName(),output,type);
                listFilesForFolder(path,fileEntry,output);

                System.out.println(path);
            } else {
                type = 0; //Get algorithm
                path = rel+fileEntry.getName();
                FileInputStream input = new FileInputStream(path);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                fakeCompress(input,out);
                byte[] compressedFile = out.toByteArray();
                writeFile(path,output, type,compressedFile);

                System.out.println(path);
            }
        }
    }
    void writeHeader(String name,OutputStream output, byte type) throws IOException {
        int chars = 100;
        for(char byt: name.toCharArray()) {
            output.write(byt);
            chars -= 1;
        }
        while(chars >0){
            output.write(0);
            chars -= 1;
        }
        output.write(type);
    }
    void writeFile(String name,OutputStream output, byte type, byte[] compressedFile) throws  IOException{
        writeHeader(name,output,type);
        byte[] size = new byte[4];
        int fileSize = compressedFile.length;
        System.out.println(fileSize);
        for(int b = 0; b < 4; b++){
            size[b] = (byte)((fileSize >> (8*b)) & 0xFF);
        }
        output.write(size);
        for(byte byt: compressedFile) {
            output.write(byt);
        }
    }

    void fakeCompress(InputStream in, OutputStream out) throws IOException{
        int temp;
        while((temp = in.read()) != -1){
            out.write(temp);
        }
    }
}