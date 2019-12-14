import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class JPEG {
        private InputStream input;
        private OutputStream output;

        //Matriz para cálculo de DCT y DCT Inversa
        private final static double[][] dctMat = {  {0.3536,   0.3536,   0.3536,   0.3536,   0.3536,   0.3536,   0.3536,   0.3536},
                {0.4904,   0.4157,   0.2778,   0.0975,  -0.0975,   -0.2778,  -0.4157,  -0.4904},
                {0.4619,   0.1913,   -0.1913,  -0.4619, -0.4619,   -0.1913,  0.1913,   0.4619},
                {0.4157,   -0.0975,  -0.4904,  -0.2778, 0.2778,    0.4904,   0.0975,   -0.4157},
                {0.3536,   -0.3536,  -0.3536,  0.3536,  0.3536,    -0.3536,  -0.3536,  0.3536},
                {0.2778,   -0.4904,  0.0975,   0.4157,  -0.4157,   -0.0975,  0.4904,   -0.2778},
                {0.1913,   -0.4619,  0.4619,  -0.1913,  -0.1913,   0.4619,   -0.4619,  0.1913},
                {0.0975,   -0.2778,  0.4157,  -0.4904,  0.4904,    -0.4157,  0.2778,   -0.0975}};

        //Tamaño final de foto
        private int height = 0;
        private int width = 0;

        //Tamaño real de scan JPEG
        private int fake_height= 0;
        private int fake_width= 0;

        //Ajustes mas comunes, no funcionara con otros
        private int[][] sampling = {{2, 2}, {1, 1}, {1, 1}};
        private int[] tableIDs = {0, 1, 1};
        private int[] quantIDs = {0, 1, 1};

        //Codificacion entropica
        private  Map<Integer, Map<String, Integer>> huf_tables = new HashMap<>();
        private Map<Integer, Map<Integer, String>> huf_tables_inv = new HashMap<>();

        //Cuantizacion
        private Map<Integer, Integer[][]> quant_tables = new HashMap<>();

        //Matriz zigzag
        private int[][] zigzag = new int[63][2];

        //Lectura de scans
        private int[][][][][] scanMatrix;

        //Offset del componente DC
        private int[] offset = {0,0,0};

        public JPEG(InputStream input, FileOutputStream output) {
            this.input = input;
            this.output = output;
            generate_zigzag();
        }

        //Metodos públicos

        public void comprimir() throws IOException {
            read_jpeg(true);
            read_ppm_header();
            read_ppm();
            write_jpeg();
        }

        public void descomprimir() throws IOException {
            read_jpeg(false);
            read_scan();
            write_ppm();
        }

        //Funciones comunes

        private void generate_zigzag() {
            //Generate zigzag access sequence
            int pointer = 0;
            boolean horizontal = true;
            boolean boundaryX = true;
            boolean lowerHalf = false;
            int posX = 0;
            int posY = 0;
            while (posX < 7 || posY < 7) {
                if (boundaryX) {
                    posX++;
                } else {
                    posY++;
                }
                if (posX == 7 && posY == 0) {
                    boundaryX = !boundaryX;
                    lowerHalf = true;
                }
                boolean inBoundary = posX == 7 && posY == 7;

                while (true){
                    zigzag[pointer][0] = posX;
                    zigzag[pointer][1] = posY;
                    pointer++;
                    if (inBoundary) break;

                    if (horizontal) {
                        posX--;
                        posY++;
                    } else {
                        posX++;
                        posY--;
                    }
                    if (lowerHalf) {
                        if (boundaryX) inBoundary = posX == 7;
                        else inBoundary = posY == 7;
                    } else {
                        if (boundaryX) inBoundary = posX == 0;
                        else inBoundary = posY == 0;
                    }
                }

                horizontal = !horizontal;
                boundaryX = !boundaryX;
            }
        }

        private void read_jpeg(boolean header) throws IOException{
            InputStream input = this.input;
            if(header) input = JPEG.class.getResourceAsStream("header.jpg");
            int i;
            boolean scan = false;
            if(input.read() != 0xFF & input.read() != 0xD8){
                throw new IOException("Header JPEG incorrecte");
            }
            while (!scan && (i = input.read()) != -1) {
                if (i == 0xFF) {
                    int id, len = 0;
                    switch (input.read()) {
                        case 0xC0:
                            //Start of frame, baseline
                            for(int ii = 0; ii<3; ii++){
                                input.read();
                            }
                            height = (input.read() << 8) | (input.read() & 0xFF);
                            width = (input.read() << 8) | (input.read() & 0xFF);

                            break;
                        case 0xDB:
                            //Quantization table
                            len = (input.read() << 4) | (input.read() & 0xFF);
                            id = input.read() & 0xF;
                            Integer[][] temp = new Integer[8][8];
                            temp[0][0] = input.read();
                            for (int ii = 0; ii < 63; ii++) {
                                temp[zigzag[ii][1]][zigzag[ii][0]] = input.read();
                            }
                            quant_tables.put(id, temp);
                            break;
                        case 0xC4:
                            //Huffman Table
                            len = (input.read() << 4) | (input.read() & 0xFF);
                            id = input.read();

                            id = ((id & 0xF0) >> 3) | id & 0xF;
                            Integer[] counts = new Integer[16];
                            int count = 0;
                            for (int ii = 0; ii < 16; ii++) {
                                count += (counts[ii] = input.read());
                            }
                            Integer[] values = new Integer[count];
                            for (int ii = 0; ii < count; ii++) {
                                values[ii] = input.read();
                            }
                            huffman(counts, values, id);
                            break;
                        case 0xDA:
                            //Start of scan data
                            scan = true;
                            break;
                        default:
                            //Unknown Marker
                    }
                }
            }
        }

        private void dct(int component, int Y, int X, boolean inverse){
            double[][] c =new double[8][8];
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    c[i][j]=0;
                    for(int k=0;k<8;k++) {
                        if(inverse) c[i][j] += dctMat[k][i] * scanMatrix[component][Y][X][k][j]; //zT * M
                        else c[i][j]+=dctMat[i][k]*scanMatrix[component][Y][X][k][j]; //Z * M
                    }
                }
            }

            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    scanMatrix[component][Y][X][i][j]=0;
                    for(int k=0;k<8;k++)
                    {
                        if(inverse) scanMatrix[component][Y][X][i][j]+=c[i][k] * dctMat[k][j]; //  (zT * M)*Z
                        else scanMatrix[component][Y][X][i][j]+=c[i][k] * dctMat[j][k]; //  (Z * M)*zT
                    }
                    if(inverse) {
                        scanMatrix[component][Y][X][i][j] += 128;
                        if (scanMatrix[component][Y][X][i][j] < 0) scanMatrix[component][Y][X][i][j] = 0;
                        if (scanMatrix[component][Y][X][i][j] > 255) scanMatrix[component][Y][X][i][j] = 255;
                    }
                    //System.out.print(scanMatrix[component][Y][X][i][j]+" ");  //printing matrix element
                }
                //System.out.println();//new line
            }

        }

        private void huffman(Integer[] counts, Integer[] values, int id) {
            short pointer = 0;
            int count = 0;
            Map<String, Integer> table = new TreeMap<>();
            Map<Integer, String> inv_table = new TreeMap<>();
            huf_branch(counts, values, "", pointer, count, table, inv_table);
            huf_tables.put(id, table);
            huf_tables_inv.put(id,inv_table);
        }

        private int huf_branch(Integer[] counts, Integer[] values, String code, short pointer, int count, Map<String, Integer> table, Map<Integer, String> inv_table) {
            if (pointer != 0 && counts[pointer - 1] != 0) {
                table.put(code, values[count]);
                inv_table.put(values[count], code);
                counts[pointer - 1] -= 1;
                return ++count;
            } else {
                pointer++;
                if (pointer <= 16) {
                    count = huf_branch(counts, values, code + "0", pointer, count, table, inv_table);
                    count = huf_branch(counts, values, code + "1", pointer, count, table, inv_table);
                }
            }
            return count;
        }

        //Compresión PPM -> JPEG

        private void read_ppm_header() throws IOException{
            int i;
            int fields = 0;
            boolean scan = false;
            if(input.read() != 'P' && input.read() != '6'){
                throw new IOException("Header PPM incorrecte");
            }
            while (!scan && (i = input.read()) != -1) {
                if(i == 0x0a || i == 0x20){
                    if((i = input.read()) == '#'){
                        //Comentari
                        do{
                            i = input.read();
                        }while(i != 0x0a);
                        i = input.read();
                    }
                    if (fields == 0) { //Width x Height field
                        String data = (char) i + "";
                        while ((i = input.read()) != -1 && i != 0x0a && i != 0x20) {
                            data = data + (char) i;
                        }
                        width = Integer.parseInt(data);
                        data = "";
                        while ((i = input.read()) != -1 && i != 0x0a && i != 0x20) {
                            data = data + (char) i;
                        }
                        height = Integer.parseInt(data);
                        fields++;
                    }
                    if (fields == 1) { //MaxColor Field
                        String data = "" + (char) i;
                        while ((i = input.read()) != -1 && i != 0x0a && i != 0x20) {
                            data = data + (char) i;
                        }
                        fields++;
                    }
                    if (fields == 2) { //SCAN Field
                        scan = true;
                    }

                }
            }
        }

        private void read_ppm() throws  IOException {

            fake_height = height;
            if (height % 16 != 0) fake_height += 16 - (height % 16);
            fake_width = width;
            if (width % 16 != 0) fake_width += 16 - (width % 16);

            int scanH = fake_width / 16;
            if (fake_width % 16 != 0) scanH += 1;
            scanH *= 2;

            int scanV = fake_height / 16;
            if (fake_height % 16 != 0) scanV += 1;
            scanV *= 2;

            scanMatrix = new int[3][scanV][scanH][8][8];
            for (int i = 0; i < fake_height; i++) {

                for (int j = 0; j < fake_width; j++) {
                    int r, b, g;
                    if (i < height && j < width) {
                        r = input.read();
                        g = input.read();
                        b = input.read();
                    } else {
                        r = 255;
                        g = 255;
                        b = 255;
                    }

                    int y = (int) ((0.299 * r) + (0.587 * g) + (0.114 * b));
                    int Cb = (int) (128 - (0.168736 * r) - (0.331264 * g) + (0.5 * b));
                    int Cr = (int) (128 + (0.5 * r) - (0.418688 * g) - (0.081312 * b));


                    scanMatrix[0][i / 8][j / 8][i % 8][j % 8] = y - 128;


                    scanMatrix[1][i / 16][j / 16][(i % 16) / 2][(j % 16) / 2] = Cb - 128;
                    scanMatrix[2][i / 16][j / 16][(i % 16) / 2][(j % 16) / 2] = Cr - 128;

                }
            }
        }

        private void write_jpeg() throws IOException{

            ByteArrayOutputStream scanBuffer = new ByteArrayOutputStream();

            for(int i= 0; i< fake_height/16; i++){
                for(int j=0; j< fake_width/16; j++){

                    //System.out.println("LUM["+i+","+j+"]");
                    for(int y1= 0; y1<2; y1++) {
                        for (int x1 = 0; x1 < 2; x1++) {
                            dct(0, (i*2) + y1, (j*2) + x1, false);
                            write_scan(scanBuffer,0,(i*2)+y1,(j*2)+x1);
                        }
                    }
                    //System.out.println("Cb=");
                    dct(1,i,j,false);
                    write_scan(scanBuffer,1,i,j);
                    //System.out.println("Cr=");
                    dct(2,i,j,false);
                    write_scan(scanBuffer,2,i,j);
                }
            }
            byte[] scan = scanBuffer.toByteArray();
            if(scan.length % 8 != 0){
                for(int i=0; i< 8- (scan.length %8); i++) scanBuffer.write(0);
            }
            scan = scanBuffer.toByteArray();
            InputStream header = JPEG.class.getResourceAsStream("header.jpg");
            int i = 0;
            int counter = 0;
            while((i = header.read()) != -1){
                if(counter == 163){
                    output.write((height >> 8) & 0xFF);
                }else if(counter == 164) {
                    output.write( height & 0xFF);
                }else if(counter == 165) {
                    output.write((width >> 8) & 0xFF );
                }else if(counter == 166){
                    output.write(width & 0xFF);
                }else{
                    output.write(i);
                }
                counter++;
            }
            for(i = 0; i< scan.length; i+=8){
                byte temp = 0;
                for(int b = 0; b<8; b++){
                    temp |= scan[i+b] << (7-b);
                }
                output.write(temp);
                if(temp == -1) {
                    output.write(0x00);
                }
            }
            output.write(0xFF);
            output.write(0xD9);


        }

        private void write_scan(ByteArrayOutputStream scanBuffer,int component, int Y, int X){
            int tableID = tableIDs[component];
            int quantID = quantIDs[component];

            int dc = (scanMatrix[component][Y][X][0][0] / quant_tables.get(quantID)[0][0]) - offset[component];
            if(dc == 0) {
                String hCode = huf_tables_inv.get(tableID).get(0x00); //EOB
                for(int c = 0; c < hCode.length(); c++){
                    if(hCode.charAt(c) == '0') scanBuffer.write(0);
                    else scanBuffer.write(1);
                }
            }
            else {
                int nbits = 0;
                while (Math.abs(dc) >= Math.pow(2, nbits)) nbits++;
                offset[component] = dc + offset[component];

                String hCode = huf_tables_inv.get(tableID).get(nbits);
                for (int c = 0; c < hCode.length(); c++) {
                    if (hCode.charAt(c) == '0') scanBuffer.write(0);
                    else scanBuffer.write(1);
                }
                if (dc < 0) {
                    dc += Math.pow(2, nbits) - 1;
                }
                for (int b = nbits - 1; b >= 0; b--) scanBuffer.write((dc >> b) & 1);
            }
            int zrl = 0;
            for(int p= 0; p<63; p++){
                int posX = zigzag[p][0];
                int posY = zigzag[p][1];
                float quantized = (float) scanMatrix[component][Y][X][posY][posX]/  (float) quant_tables.get(quantID)[posY][posX];
                int ac = Math.round(quantized);
                if(ac == 0){
                    if(p != 62)zrl++;
                    else{
                        String hCode = huf_tables_inv.get(2 | tableID).get(0x00); //EOB
                        for(int c = 0; c < hCode.length(); c++){
                            if(hCode.charAt(c) == '0') scanBuffer.write(0);
                            else scanBuffer.write(1);
                        }
                    }
                }
                else{
                    while(zrl >= 16){ //Write ZRLs
                        String hCode = huf_tables_inv.get(2 | tableID).get(0xF0);
                        for(int c = 0; c < hCode.length(); c++){
                            if(hCode.charAt(c) == '0') scanBuffer.write(0);
                            else scanBuffer.write(1);
                        }
                        zrl -= 16;
                    }
                    int nbits = 0;
                    while(Math.abs(ac) >= Math.pow(2,nbits)) nbits++;
                    String hCode = huf_tables_inv.get(2 | tableID).get(  ((zrl << 4) & 0xF0) | nbits   );
                    zrl =0;
                    for(int c = 0; c < hCode.length(); c++){
                        if(hCode.charAt(c) == '0') scanBuffer.write(0);
                        else scanBuffer.write(1);
                    }
                    if(ac < 0){
                        ac += Math.pow(2,nbits) -1;
                    }
                    for(int b = nbits-1; b>=0; b--)scanBuffer.write((ac >> b) & 1);
                }


            }

        }

        //Descompresión JPEG -> PPM

        private void read_scan() throws IOException {
            int len = 0;
            ByteArrayOutputStream scanBuffer = new ByteArrayOutputStream();
            len |= input.read() << 4;
            len |= input.read() & 0xFF;
            for (int ii = 0; ii < (len - 2); ii++) input.read();

            //Y sampling is taken as reference -> it must always be >= than the other sampling factors for the decoder to work
            int scanH = (width / 8) + (sampling[0][0]);

            int scanV = (height / 8 + (sampling[0][1]));

            scanMatrix = new int[3][scanV][scanH][8][8];


            int read;
            while ((read = input.read()) != -1) {
                if (read == 0xFF) { //Byte skipping and EOI
                    read = input.read();
                    if (read == 0xD9) {
                        //End of Image
                        break;
                    } else {
                        read = 0xFF;
                    }
                }
                for (int ii = 7; ii >= 0; ii--) {
                    scanBuffer.write((read >> ii) & 1);
                }
            }

            byte[] scan = scanBuffer.toByteArray();
            int pointer = 0;
            for (int y = 0; y < height; y += sampling[0][1] * 8)
                for (int x=0; x < width; x += sampling[0][0] * 8) { //MCU
                    pointer = read_component(0, tableIDs[0], quantIDs[0], x, y, sampling[0][0], sampling[0][1], pointer, scan);
                    pointer = read_component(1, tableIDs[1], quantIDs[1], x, y, sampling[1][0], sampling[1][0], pointer, scan);
                    pointer = read_component(2, tableIDs[2], quantIDs[2], x, y, sampling[2][0], sampling[2][0], pointer, scan);
                }
        }

        private int read_component(int component, int tableID, int quantID, int x, int y, int scanH, int scanV, int pointer, byte[] scan) {
            for (int y1 = 0; y1 < scanH; y1++)
                for (int x1 = 0; x1 < scanV; x1++) {
                    int offsetX = (x / (8 * (sampling[0][0] / sampling[component][0]))  ) + x1;
                    int offsetY = (y / (8 * (sampling[0][1] / sampling[component][1]))) + y1;

                    String hCode = "";
                    while (!huf_tables.get(tableID).containsKey(hCode)) {//DC Table 00
                        hCode = hCode + Integer.toString(scan[pointer]);
                        pointer++;
                    }
                    int lenDC = huf_tables.get(tableID).get(hCode);

                    int valDC = 0;
                    for (int b = lenDC - 1; b >= 0; b--) {
                        valDC |= scan[pointer] << b;
                        pointer++;
                    }
                    if ((valDC & (1 << lenDC - 1)) == 0) {
                        valDC -= Math.pow(2, lenDC) - 1;
                    }
                    valDC = (valDC * quant_tables.get(quantID)[0][0]) + offset[component];
                    offset[component] = valDC;
                    scanMatrix[component][offsetY][offsetX][0][0] = valDC;

                    int zrl = 0;
                    int valAC = 0;
                    for (int i = 0; i < 63; i++) {
                        int posX = zigzag[i][0];
                        int posY = zigzag[i][1];
                        //Read phase
                        if (zrl == 0) {
                            hCode = "";
                            while (!huf_tables.get(2 | tableID).containsKey(hCode)) { //AC Table 10
                                hCode = hCode + Integer.toString(scan[pointer]);
                                pointer++;
                            }
                            if(huf_tables.get(2| tableID).get(hCode) == 0xF0){
                                zrl = 15;
                                valAC = 0;
                            }else {
                                int acBits = huf_tables.get(2 | tableID).get(hCode) & 0xF;
                                zrl = huf_tables.get(2 | tableID).get(hCode) >> 4;
                                valAC = 0;
                                for (int b = acBits - 1; b >= 0; b--) {
                                    valAC |= scan[pointer] << b;
                                    pointer++;
                                }
                                if ((valAC & (1 << acBits - 1)) == 0) {
                                    valAC -= Math.pow(2, acBits) - 1;
                                }
                                if (valAC == 0) zrl = 64;
                            }
                        } else {
                            zrl--;
                        }

                        //Write phase
                        if (zrl == 0) {
                            scanMatrix[component][offsetY][offsetX][posY][posX] = valAC * quant_tables.get(quantID)[posY][posX];
                        } else {
                            scanMatrix[component][offsetY][offsetX][posY][posX] = 0;
                        }
                    }

                    dct(component,offsetY,offsetX,true);
                }
            return pointer;
        }

        private void write_ppm() throws IOException{
            output.write("P6".getBytes());
            output.write(0xa);
            output.write(Integer.toString(width).getBytes());
            output.write(0x20);
            output.write(Integer.toString(height).getBytes());
            output.write(0xa);
            output.write("255".getBytes());
            output.write(0xa);
            for(int i = 0; i<height; i++){

                for(int j = 0; j<width; j++){

                    int y =  scanMatrix[0][i/8][j/8][i%8][j%8];
                    int downsampleX = sampling[0][0]/sampling[1][0];
                    int downsampleY = sampling[0][1]/sampling[1][1];


                    int Cb = scanMatrix[1][i/ (8* downsampleY) ] [j/ (8* downsampleX ) ][  (i% (8* downsampleY )) /downsampleY  ] [(j% (8* downsampleX ))/downsampleX ];
                    int Cr = scanMatrix[2][i/ (8* downsampleY) ] [j/ (8* downsampleX ) ][  (i% (8* downsampleY )) /downsampleY  ] [(j% (8* downsampleX ))/downsampleX ];

                    int r = (int) (y + (1.402 * (Cr - 128)));
                    if(r >255) r = 255;
                    if(r <0) r = 0;
                    int g = (int) (y - (0.34414 * (Cb-128)) - (0.71414 * (Cr-128)));
                    if(g >255) g = 255;
                    if(g <0) g = 0;
                    int b = (int) (y + (1.772 * (Cb - 128)));
                    if(b >255) b = 255;
                    if(b <0) b = 0;
                    output.write(r);
                    output.write(g);
                    output.write(b);
                }
            }

        }
    }

