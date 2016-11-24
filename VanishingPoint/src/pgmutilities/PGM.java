package pgmutilities;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class PGM {

    private int width;
    private int height;
    private int max_val;
    private int[] pixels;

    public PGM(int width, int height, int max_val) {
        this.width = width;
        this.height = height;
        this.max_val = max_val;
        this.pixels = new int[width * height];
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the max_val
     */
    public int getMax_val() {
        return max_val;
    }

    /**
     * @param max_val the max_val to set
     */
    public void setMax_val(int max_val) {
        this.max_val = max_val;
    }

    /**
     * @return the pixels
     */
    public int[] getPixels() {
        return pixels;
    }

    /**
     * @param pixels the pixels to set
     */
    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
    
    /**
     *  numero pixel sotto soglia 128
     */    
    public int getNumberPixelsUnder128(){
        int count = 0;
        for(int i=0 ; i<width*height ; i++){
            if(pixels[i]<128){
                count+=1;
            }
        }
        return count;
    }
    
    /**
     * numero pixel sopra soglia 128
     */
    public int getNumberPixelsOver128(){
        int count = 0;
        for(int i=0 ; i<width*height ; i++){
            if(pixels[i]>=128){
                count+=1;
            }
        }
        return count;
    }
    
    public void printLine(ArrayList<Line> arrayLine){ 
        for(Line l : arrayLine){
            int rho = l.getRho();
            int theta = l.getTheta();
            int r,x,y;           
            switch(theta){
                case 0: //ciclo righe
                    r = rho;
                    for(int i=0 ; i<height ; i++){
                        pixels[i*width+r]=180;
                    }
                    break;
                case 90:
                    y = (int) (rho*Math.sin(Math.toRadians(theta)));
                    r=height-y;
                    for(int i=0 ; i<width ; i++){
                        pixels[r*width+i]=180;
                    }
                    break;
                default:
                    double m = Math.tan(Math.toRadians(theta));
                    x = (int) (rho*Math.cos(Math.toRadians(theta)));
                    y = (int) (rho*Math.sin(Math.toRadians(theta)));           
                    //ho tutto -> y = -1/m (x-x1)+y1
                    for (int j = 0; j < width; j++) {
                        r = (int) (height-((-1/m)*(j-x)+y));
                        if(r>=0 && r<height){
                            pixels[r*width+j]=180;
                        }
                    }
                    break;
            }
                             
        }
    
    }

    
}
