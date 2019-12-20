import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class CtrlCarpeta{

    CtrlFitxer ctrlFitxer;

    public CtrlCarpeta(CtrlFitxer ctrlFitxer) {
        this.ctrlFitxer = ctrlFitxer;
    }

    void decompress(InputStream input,String path_out) throws IOException{
        int temp;
        Path target = Paths.get(path_out).toAbsolutePath();
        Path parent = target.getParent();
        File targetFile = new File(target.toString());
        if(!targetFile.exists()){
            File rootFolder = new File(parent.toString());
            if(!rootFolder.exists() || !rootFolder.isDirectory()) throw new IOException("Can't find output path");
            if(!targetFile.mkdir()) throw new IOException("Can't create output folder");
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
            if(type == 4){
                File folder = new File(path_out+"/"+path);
                folder.mkdir();
            }else{
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
                ByteArrayOutputStream out;
                if(type == 5) {
                    out = justCopy(fileIn);
                }else{
                    out = ctrlFitxer.descomprimir(fileIn,type);
                }
                ctrlFitxer.escriureFitxerSortida(out,path_out+"/"+path);
            }
        }
    }


    ByteArrayOutputStream compress(String path) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if(!path.endsWith("/") && !path.endsWith("\\")) path = path +"/";
        final File folder = new File(path);
        String relPath = folder.getName() + "/";
        String absPath = folder.getAbsolutePath() + "/";
        writeHeader(relPath,(byte)4,output);
        listFilesForFolder(relPath,absPath,folder,output);
        return output;
    }

    void listFilesForFolder(String rel, String abs,final File folder,OutputStream output) throws IOException {
        String relPath;
        String absPath;
        System.out.println(rel);
        System.out.println(abs);

        for (final File fileEntry : folder.listFiles()) {
            byte type = 0;
            relPath =  rel +fileEntry.getName();
            absPath = abs + fileEntry.getName();
            if (fileEntry.isDirectory()) {
                relPath += "/";
                absPath += "/";
                type = 4;
                writeHeader(relPath,type,output);
                listFilesForFolder(relPath, absPath,fileEntry,output);

                System.out.println(relPath);
            } else {
                type =(byte) ctrlFitxer.algoritmeAutomatic(fileEntry.getName(),0);

                System.out.println(relPath);
                FileInputStream input = ctrlFitxer.carregarFitxerEntrada(absPath);
                ByteArrayOutputStream out;
                if(type == 5){
                    out = justCopy(input);
                }else {
                    out = ctrlFitxer.comprimir(input, type);
                }
                byte[] compressedFile = out.toByteArray();
                writeFile(relPath,type,compressedFile,output);
            }
        }
    }
    void writeHeader(String relPath, byte type,OutputStream output) throws IOException {
        int chars = 100;
        for(char byt: relPath.toCharArray()) {
            output.write(byt);
            chars -= 1;
        }
        while(chars >0){
            output.write(0);
            chars -= 1;
        }
        output.write(type);
    }
    void writeFile(String relPath, byte type, byte[] compressedFile,OutputStream output) throws  IOException{
        writeHeader(relPath,type,output);
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

    ByteArrayOutputStream justCopy(InputStream in) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int temp;
        while((temp = in.read()) != -1){
            out.write(temp);
        }
        return out;
    }
}