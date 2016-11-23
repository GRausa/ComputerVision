/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgmutilities;

/**
 *
 * @author Giovanni
 */
public class Line {
    
    private int rho, theta;
    //y altezza
    //x larghezza

    public Line(int rho, int theta) {
        this.theta = theta;
        this.rho = rho;          
    }

    public int getTheta() {
        return theta;
    }

    public int getRho() {
        return rho;
    }

    
    
    
}
