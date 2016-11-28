package pgmtest;

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

        //cornice -> utilizzata in seguito per ricavare vanishing point al di fuori della figura   
        PGM img = pgmUtil.addCornice(imgIn, 300);
        pgmUtil.writePGM(img, "cornice.pgm");
        //PGM img = imgIn;
        //isotropic module image
        PGM imgOutIsotropic = pgmUtil.isotropicModulePGM(img);
        pgmUtil.writePGM(imgOutIsotropic, "isotropicModule.pgm"); 
                  
        //spazio parametri
        //pgmUtil.resetPGM(imgOut);
        PGM imgOut = pgmUtil.spazioParametri(img, 15); //check
        pgmUtil.writePGM(imgOut, "draw.pgm");
        
        
    }
}
