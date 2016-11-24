package pgmtest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import pgmutilities.PgmUtilities;
import pgmutilities.PGM;

/**
 *
 * @author user
 */
public class ImageUtilityTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String namefile = "image.pgm";
        
        PgmUtilities pgmUtil = new PgmUtilities();

        PGM imgIn = pgmUtil.readPGM(namefile);

        if (imgIn == null) {
            return;
        }

        PGM imgOut = pgmUtil.newPGM(imgIn.getWidth(), imgIn.getHeight(), imgIn.getMax_val());
               
        //isotropic module image
        pgmUtil.resetPGM(imgOut);
        imgOut = pgmUtil.isotropicModulePGM(imgIn);
        pgmUtil.writePGM(imgOut, "isotropicModule.pgm");
        
        //cornice -> utilizzata in seguito per ricavare vanishing point al di fuori della figura
        pgmUtil.resetPGM(imgOut);
        imgOut = pgmUtil.addCornice(imgIn);
        pgmUtil.writePGM(imgOut, "cornice.pgm");
                  
        //spazio parametri
        //pgmUtil.resetPGM(imgOut);
        imgOut = pgmUtil.spazioParametri(imgOut);
        pgmUtil.writePGM(imgOut, "draw.pgm");
        
        
    }
}
