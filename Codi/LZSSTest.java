

import org.junit.Test;


import java.io.*;
import static org.junit.Assert.assertArrayEquals;


public class LZSSTest {
    @Test
    public void testcomprimeix1() throws IOException {
        InputStream prova1 = new FileInputStream("LZSS_prova1_entrada.txt");
        byte[] comprimit1 = new LZSS(prova1, 0).comprimir().toByteArray();
        byte[] bcomprova1 = {0,49,0,24,64,12,96,6,64,3,40,1,4};
        assertArrayEquals(bcomprova1, comprimit1);
    }

     @Test
        public void testcomprimeix2() throws IOException {
            InputStream prova2 = new FileInputStream("LZSS_prova2_entrada.txt");
            byte[] comprimit2 = new LZSS(prova2, 0).comprimir().toByteArray();
            byte[] bcomprova2 = {0,49,0,24,64,12,112,1,-126,0,80,-128};
            assertArrayEquals(bcomprova2, comprimit2);
        }

    @Test
    public void testcomprimeix3() throws IOException {
        InputStream prova3 = new FileInputStream("LZSS_prova3_entrada.txt");
        byte[] comprimit3 = new LZSS(prova3, 0).comprimir().toByteArray();
        byte[] bcomprova3 = {0,49,64,3,-16,3,16};
        assertArrayEquals(bcomprova3, comprimit3);
    }

    @Test
    public void testcomprimeix4() throws IOException {
        InputStream prova4 = new FileInputStream("LZSS_prova4_entrada.txt");
        byte[] comprimit4 = new LZSS(prova4, 0).comprimir().toByteArray();
        byte[] bcomprova4 = {0,49,0,24,64,12,96,6,72,1,3,0,56,-128};
        assertArrayEquals(bcomprova4, comprimit4);
    }

    @Test
    public void testdescomprimeix1() throws IOException {
        InputStream prova1 = new FileInputStream("LZSS_prova1_sortida.lzss");
        byte[] descomprimit1 = new LZSS(prova1, 1).descomprimir().toByteArray();
        String sprova1 = "bacdeA";
        byte[] bprova1 = sprova1.getBytes();
        assertArrayEquals(bprova1, descomprimit1);
    }

     @Test
        public void destestcomprimeix2() throws IOException {
            InputStream prova2 = new FileInputStream("LZSS_prova2_sortida.lzss");
            byte[] descomprimit2 = new LZSS(prova2, 1).descomprimir().toByteArray();
            String sprova2 = "bacbabac";
            byte[] bprova2 = sprova2.getBytes();
            assertArrayEquals(bprova2, descomprimit2);
        }

    @Test
    public void testdescomprimeix3() throws IOException {
        InputStream prova3 = new FileInputStream("LZSS_prova3_sortida.lzss");
        byte[] descomprimit3 = new LZSS(prova3, 1).descomprimir().toByteArray();
        String sprova3 = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        byte[] bprova3 = sprova3.getBytes();
        assertArrayEquals(bprova3, descomprimit3);
    }

    @Test
    public void testdescomprimeix4() throws IOException {
        InputStream prova4 = new FileInputStream("LZSS_prova4_sortida.lzss");
        byte[] descomprimit4 = new LZSS(prova4, 1).descomprimir().toByteArray();
        String sprova4 = "bacdbacbacd";
        byte[] bprova4 = sprova4.getBytes();
        assertArrayEquals(bprova4, descomprimit4);
    }

}
