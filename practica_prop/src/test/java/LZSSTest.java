package test.java;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.assertArrayEquals;

public class LZSSTest {
    /*@Test
    public void testcomprimeix1() {
        InputStream prova1 = new FileInputStream(prova1.txt);
        byte[] comprimit1 = new byte[LZSS(prova1, 0).toByteArray];
        byte[] bcomprova1 = {0,49,0,24,64,12,96,6,64,3,40,1,4};
        assertArrayEquals(bcomprova1, comprimit1);
    }

     @Test
        public void testcomprimeix2() {
            InputStream prova2 = new FileInputStream(prova2.txt);
            byte[] comprimit2 = new byte[LZSS(prova2, 0).toByteArray];
            byte[] bcomprova2 = {0,49,0,24,64,12,112,1,--12};
            assertArrayEquals(bcomprova2, comprimit2);
        }

    @Test
    public void testcomprimeix3() {
        InputStream prova3 = new FileInputStream(prova3.txt);
        byte[] comprimit3 = new byte[LZSS(prova3, 0).toByteArray];
        byte[] bcomprova3 = {0,49,64,3,-16,3,16};
        assertArrayEquals(bcomprova3, comprimit3);
    }

    @Test
    public void testcomprimeix4() {
        InputStream prova4 = new FileInputStream(prova4.txt);
        byte[] comprimit4 = new byte[LZSS(prova4, 0).toByteArray];
        byte[] bcomprova4 = {0,49,0,24,64,12,96,6,72,1,3,0,56};
        assertArrayEquals(bcomprova4, comprimit4);
    }

    @Test
    public void testdescomprimeix1() {
        InputStream prova1 = new FileInputStream(prova1.lzss);
        byte[] descomprimit1 = new byte[LZSS(prova1, 1).toByteArray];
        String sprova1 = "bacdeA";
        byte[] bprova1 = sprova1.getBytes();
        assertArrayEquals(bprova1, descomprimit1);
    }

     @Test
        public void destestcomprimeix2() {
            InputStream prova2 = new FileInputStream(prova2.lzss);
            byte[] descomprimit2 = new byte[LZSS(prova2, 1).toByteArray];
            String sprova2 = "bacbacbac";
            byte[] bprova2 = sprova2.getBytes();
            assertArrayEquals(bprova2, descomprimit2);
        }

    @Test
    public void testdescomprimeix3() {
        InputStream prova3 = new FileInputStream(prova3.lzss);
        byte[] descomprimit3 = new byte[LZSS(prova3, 1).toByteArray];
        String sprova3 = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        byte[] bprova3 = sprova3.getBytes();
        assertArrayEquals(bprova3, comprimit3);
    }

    @Test
    public void testdescomprimeix4() {
        InputStream prova4 = new FileInputStream(prova4.lzss);
        byte[] descomprimit4 = new byte[LZSS(prova4, 1).toByteArray];
        String sprova4 = "bacdbacbacd";
        byte[] bprova4 = sprova4.getBytes();
        assertArrayEquals(bprova4, descomprimit4);
    }
    */
}
