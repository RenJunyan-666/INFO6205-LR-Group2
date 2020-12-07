package Util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MathUtilTest {

    @Test
    public void stdGaussian() {
        double sigma=1;
        double u=0;
        int count1=0;
        int count2=0;
        int count3=0;
        double x[]= new double[10000];
        for (int i = 0; i < 10000; i++) {
            x[i]= MathUtil.stdGaussian(sigma,u);
        }
        for (int j = 0; j < 10000; j++) {
            if(x[j]>-(sigma+u)&&x[j]<(sigma+u)){
                count1++;
            }
        }
        for (int k = 0; k < 10000; k++) {
            if(x[k]>-(2*sigma+u)&&x[k]<(2*sigma+u)){
                count2++;
            }
        }
        for (int m = 0; m < 10000; m++) {
            if(x[m]>-(3*sigma+u)&&x[m]<(3*sigma+u)){
                count3++;
            }
        }

        assertEquals(count1,6820,200);
        assertEquals(count2,9540,200);
        assertEquals(count3,9970,200);
    }


}