/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pgmutilities;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author user
 */
public class PgmUtilities {

    static int numLines = 0;

    //---------------------------------------------------------// 
    //------------- Create a new empty pgm image --------------//
    //---------------------------------------------------------// 
    public PGM newPGM(int width, int height, int max_val) {
        return new PGM(width, height, max_val);
    }

    // ----------------------------------------------------- //
    // Reads information from header file                    //
    // Allows for reading and writing in PGM P2 - P5 format  //
    // Version 1.1 Piercarlo Dondi & Alessandro Gaggia       //
    // ----------------------------------------------------- //
    //******************* I/O FUNCTIONS *********************//
    //-------------------------------------------------------//
    //--------------- Skip Commented Lines ------------------//
    //-------------------------------------------------------// 
    public String skipComments(BufferedReader br) throws IOException {
        boolean loop = true;
        String buffer = br.readLine();

        while (loop) {
            if (buffer.charAt(0) != '#') {
                loop = false;
            } else {
                buffer = br.readLine();
                numLines++;
            }
        }

        return buffer;
    }

    //---------------------------------------------------------// 
    //------- Set to zero all the pixels of a pgm image -------//
    //---------------------------------------------------------// 
    public void resetPGM(PGM pgm) {
        int width = pgm.getWidth();
        int height = pgm.getHeight();
        int i;

        // set to zero all the pixels
        for (i = 0; i < width * height; i++) {
            pgm.getPixels()[i] = 0;
        }
    }

    //---------------------------------------------------------//
    //--------- Read Pixels From Different FileType -----------//
    //---------------------------------------------------------// 
    public PGM readPGM(String filename) {
        int width, height, max_val;
        boolean binary;

        PGM pgm;
        numLines = 3; // default number of lines of header

        try {
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String buffer;
            // Read a line till \n or 64 char
            buffer = br.readLine();

            if ("P2".equals(buffer)) {
                binary = false;
                System.out.println("\nFORMAT: P2");
            } else if ("P5".equals(buffer)) {
                binary = true;
                System.out.println("\nFORMAT: P5");
            } else {
                System.err.println("ERROR: incorrect file format\n");
                in.close();
                return null;
            }

            // Jump commented lines
            buffer = skipComments(br);

            // Read width, height and max grayscale value
            StringTokenizer st = new StringTokenizer(buffer);
            width = Integer.parseInt(st.nextToken());
            height = Integer.parseInt(st.nextToken());

            buffer = br.readLine();
            max_val = Integer.parseInt(buffer);

            // Printing information on screen
            System.out.println("\nPGM Filename: " + filename + "\nPGM Width & Height: " + width + "," + height + "\nPGM Max Val & Type: " + max_val + "," + (binary ? "P5" : "P2") + "\n");

            // Initialize PGM
            pgm = newPGM(width, height, max_val);

            // Reading Pixels
            if (binary) // P5 case
            {

                br.close();
                fstream = new FileInputStream(filename);
                in = new DataInputStream(fstream);

                int numLinesToSkip = numLines;
                System.out.println(numLinesToSkip);
                while (numLinesToSkip > 0) {
                    char c;
                    do {
                        c = (char) (in.readUnsignedByte());
                    } while (c != '\n');
                    numLinesToSkip--;
                }

                int num;
                int x = 0;

                while ((num = in.read()) != -1) {
                    pgm.getPixels()[x] = num;
                    x++;
                }
            } else // P2 case
            {
                int i = 0;
                while ((buffer = br.readLine()) != null) {
                    st = new StringTokenizer(buffer);
                    while (st.hasMoreTokens()) {
                        pgm.getPixels()[i] = Integer.parseInt(st.nextToken());
                        i++;
                    }
                }
            }

            // Ok close the file
            in.close();

            System.out.println("\nImage correctly loaded");

            return pgm;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            return null;
        } catch (IOException ex) {
            System.out.println("IOException. Please check file.");
            return null;
        }
    }

    //---------------------------------------------------------//
    //--- Write Pixels inside images for Different FileType ---//
    //---------------------------------------------------------// 
    public void writePGM(PGM pgm, String filename) {
        if (pgm == null) {
            System.err.println("Error! No data to write. Please Check.");
            return;
        }

        FileWriter fstream;
        try {
            fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("P2\n" + pgm.getWidth() + " " + pgm.getHeight() + "\n" + pgm.getMax_val() + "\n");

            int i;
            int width = pgm.getWidth();
            int height = pgm.getHeight();

            // Write image
            for (i = 0; i < width * height; i++) {
                out.write(pgm.getPixels()[i] + "\n");
            }

            System.out.println("\nImage correctly writed");

            // Ok close the file
            out.close();

        } catch (IOException ex) {
            System.err.println("\nIOException. Check input Data.");
        }
    }
    
    //--------------------------------------------------------//
    //---------------------- Bordo nero ----------------------//
    //--------------------------------------------------------// 
    public PGM addCornice(PGM pgmIn) {
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }
        //add 100px alto destra sinistra
        int addPixel = 200;
        int pgmInWidth = pgmIn.getWidth(); 
        int pgmInHeight = pgmIn.getHeight(); 
        
        int pgmOutWidth = pgmInWidth+(2*addPixel);//destra sinistra
        int pgmOutHeight = pgmInHeight+(2*addPixel);//alto basso
        PGM pgmOut = new PGM(pgmOutWidth, pgmOutHeight, pgmIn.getMax_val());
        
        int[] outPixels = new int[pgmOutHeight * pgmOutWidth];
        int[] inPixels = pgmIn.getPixels();
        
        //aggiungo cornice superiore righe = pgmOutHeight/2 , colonne = pgmOutWidth + pgmInWidth
        for(int i = 0 ; i<addPixel ; i++){
            for(int j = 0 ; j < pgmOutWidth ; j++){
                outPixels[ i * pgmOutWidth + j] = 0;
            }
        }
        
        //aggiungo le 2 colonne destra centro sinistra 
        for(int i = addPixel ; i<pgmOutHeight-addPixel ; i++){
            //aggiungo sinistra
            for(int z=0 ; z<addPixel ; z++){
                outPixels[ i * pgmOutWidth + z]=0;
            }
            
            //aggiungo centro
            for(int j = addPixel ; j < addPixel+pgmInWidth ; j++){
                outPixels[ i * pgmOutWidth + j] = inPixels[(i-addPixel) * pgmInWidth + (j-addPixel)];
            }
            
            //aggiungo destra
            for(int z=pgmOutWidth-addPixel ; z < pgmOutWidth ; z++){
                outPixels[ i * pgmOutWidth + z]=0;
            }
        }
        
        //aggiungo cornice inferiore righe = pgmOutHeight/2 , colonne = pgmOutWidth + pgmInWidth
        for(int i = pgmOutHeight-addPixel ; i < pgmOutHeight ; i++){
            for(int j = 0 ; j < pgmOutWidth ; j++){
                outPixels[ i * pgmOutWidth + j] = 0;
            }
        }
        
        pgmOut.setPixels(outPixels);
        return pgmOut;       
    }
    
    //--------------------------------------------------------//
    //------------------ Isotropic Operation -----------------//
    //--------------------------------------------------------// 
    public PGM isotropicModulePGM(PGM pgmIn) {
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }
        
        //Isotropic delete one row and one coloumn
        PGM pgmOut = new PGM(pgmIn.getWidth(), pgmIn.getHeight(), pgmIn.getMax_val());

        int i,j;
        double Gx, Gy, Gm, sumGm=0;

        int width = pgmIn.getWidth();
        int height = pgmIn.getHeight();

        int[] inPixels = pgmIn.getPixels();
        int[] outPixels = new int[width * height];
        
        // Modify Pixels
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                
                //No Bordi
                if(i!=0 & j!=0 & j!=width-1 & i!=height-1){
                    //maschera
                    Gx = ( - inPixels[(i-1) * width + (j-1)] - Math.sqrt(2)*inPixels[i * width + (j-1)] - inPixels[(i+1) * width + (j-1)] + inPixels[(i-1) * width + (j+1)] + Math.sqrt(2)*inPixels[i * width + (j+1)] + inPixels[(i+1) * width + (j+1)]);
                    Gy = ( + inPixels[(i-1) * width + (j-1)] + Math.sqrt(2)*inPixels[(i-1) * width + j] + inPixels[(i-1) * width + (j+1)] - inPixels[(i+1) * width + (j-1)] - Math.sqrt(2)*inPixels[(i+1) * width + j] - inPixels[(i+1) * width + (j+1)]);
                    Gm = Math.sqrt(Math.pow(Gx, 2)+Math.pow(Gy, 2));
                    outPixels[i * width + j]=(int) Math.round(Gm); //arrotonda intero più vicino       
                    sumGm+=Gm;
                }
                else{
                    outPixels[i * width + j]=0;
                }
            }
        }
        
        //media Gm
        double mediaGm = sumGm/inPixels.length;
        //System.out.println("MediaGm: "+mediaGm);
        
        //threshold in base alla media Gm
        //Gm * 2 il risultato mi sembra migliore
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                if(outPixels[i*width + j]<mediaGm*2.5){
                    outPixels[i * width + j]=0;                                    
                }
                else{
                    outPixels[i * width + j]=255;
                }
                System.out.print(outPixels[i * width + j]+"\t");    
            }
            System.out.println("");
        }
        
        pgmOut.setPixels(outPixels);        
        return pgmOut;
    }
    

    public PGM isotropicPhasePGM(PGM pgmIn) {
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }
        
        //Isotropic delete one row and one coloumn
        PGM pgmOut = new PGM(pgmIn.getWidth(), pgmIn.getHeight(), pgmIn.getMax_val());

        int i,j, phase=0;
        double Gx, Gy;

        int width = pgmIn.getWidth();
        int height = pgmIn.getHeight();

        int[] inPixels = pgmIn.getPixels();
        int[] outPixels = new int[width * height];
        
        // Modify Pixels
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                
                //No Bordi
                if(i!=0 & j!=0 & j!=width-1 & i!=height-1){
                    //maschera
                    Gx = ( - inPixels[(i-1) * width + (j-1)] - Math.sqrt(2)*inPixels[i * width + (j-1)] - inPixels[(i+1) * width + (j-1)] + inPixels[(i-1) * width + (j+1)] + Math.sqrt(2)*inPixels[i * width + (j+1)] + inPixels[(i+1) * width + (j+1)]);
                    Gy = ( + inPixels[(i-1) * width + (j-1)] + Math.sqrt(2)*inPixels[(i-1) * width + j] + inPixels[(i-1) * width + (j+1)] - inPixels[(i+1) * width + (j-1)] - Math.sqrt(2)*inPixels[(i+1) * width + j] - inPixels[(i+1) * width + (j+1)]);
                    phase = (int) Math.round(Math.toDegrees(Math.atan2(Gy, Gx)));         
                    //shift di +90 da gradiente a bordo
                    phase+=90;
                    //rendo fase a 0 a 360
                    if(phase<0)
                        phase=360+phase;   
                    if(phase==360)
                        phase=0;
                    outPixels[i * width + j] = phase;
                }
                else{
                    outPixels[i * width + j]=0;
                }
                System.out.print(phase+"\t");
            }
            System.out.println();
        }
               
        pgmOut.setPixels(outPixels);

        return pgmOut;
    }
    
    
    //--------------------------------------------------------//
    //------------------- Spazio Parametri -------------------//
    //--------------------------------------------------------// 
    public PGM spazioParametri (PGM pgmIn) {
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }
        int i,j;
        
        int maxPhase = 360; //vettore da 0 a 259
        int maxRho = (int) Math.ceil(Math.sqrt(Math.pow(pgmIn.getHeight(), 2)+Math.pow(pgmIn.getWidth(), 2))); //diagonale arrotondo eccesso
        
        PGM pgmModuleIsotropic = this.isotropicModulePGM(pgmIn);
        PGM pgmPhaseIsotropic = this.isotropicPhasePGM(pgmIn);
        
        int width = pgmIn.getWidth();
        int height = pgmIn.getHeight();
        
        int[] inModulePixels = pgmModuleIsotropic.getPixels();
        int[] inPhasePixels = pgmPhaseIsotropic.getPixels();
        
        int[][] matSpazioParametri = new int[maxRho][maxPhase];
        
        //azzero SpazioParametri
        for(i = 0 ; i < maxRho ; i++){
            for(j = 0 ; j < maxPhase ; j++){
                matSpazioParametri[i][j]=0;
            }
        }
        int rho, phase=0;
        
        // Riempio spazio parametri
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                if(inModulePixels[i * width + j]==255) {
                    //angolo 
                    int phaseIsotropic = inPhasePixels[i * width + j];
                    switch(phaseIsotropic){
                        case 90:
                            rho=j;
                            phase=0;
                            break;
                        case 270:
                            rho=j;
                            phase=0;
                            break;
                        case 180:
                            rho=height-i-1;
                            phase=90;
                            break;
                        case 0:
                            rho=height-i-1;
                            phase=90;
                            break;
                        default:
                            //retta trovata y=mx+q
                          
                            //trovo m                            
                            double m = Math.tan(Math.toRadians(phaseIsotropic));
                            
                            //trovo q=y-mx
                            double q = (height-i-1)-(m*j);
                            
                            //pendenza retta tangente
                            double m1 = -1/m;                          
                            
                            //trovo intersezione
                            int x=(int) Math.round(-q/(m-m1));
                            int y=(int) Math.round(m*(-q/(m-m1))+q);
                            
                            //rho
                            rho=(int) Math.round(Math.sqrt((Math.pow(x, 2))+Math.pow(y, 2)));
                            
                            //ricavo fase m1 gradi   
                            
                            int phaseTrovata = (int) Math.round(Math.toDegrees(Math.atan(m1)));
                            
                            
                            
                            if(phaseTrovata==360)
                                phaseTrovata=0;
                            //---> DA VEDERE <---- QUESTA CONDIZ
                            if(x*y>0){//semiquadro positivo o 3 quadrante
                                //if(phaseTrovata>=0)
                                    phase=phaseTrovata;
                                //else
                                 //   phase=360+phaseTrovata;
                                
                            }
                            else{
                                if(x<0){//2 semiquadro
                                    phase=phaseTrovata+180;                                      
                                }
                                else{
                                    phase=360+phaseTrovata;
                                }
                            }
                    }
                    
                    //escludo alcune circostanze
                    //if(phase !=90 & phase !=270 & phase !=180 & phase !=0)                    
                    if(phase>10 & phase <80 | phase > 100 & phase < 170 | phase > 190 & phase < 350)
                        matSpazioParametri[rho][phase]+=1;
                    
                }
            }
        }      
        
        //trovare picchi spazio parametri
        
        //esempio soglia
        ArrayList<Line> arrayLine = new ArrayList<>();
        int check=15;
        System.out.println("Picchi");
        for(i = 0 ; i < maxRho ; i++){
            for(j = 0 ; j < maxPhase ; j++){
                if(matSpazioParametri[i][j]>=check){
                    arrayLine.add(new Line(i,j));
                    System.out.println("Rho: "+i+" Theta: "+j);
                }
            }
        }    
        pgmIn.printLine(arrayLine);
        
        return pgmIn;
    }
}
