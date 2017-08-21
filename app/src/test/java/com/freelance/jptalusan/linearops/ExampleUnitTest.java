package com.freelance.jptalusan.linearops;

import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.EquationGeneration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void generateEquation() {
        EquationGeneration.generateEqualityEquation(Constants.LEVEL_5);
//        int n = 5;
//        for(int i = 0; i < n; ++i) {
//            EquationGeneration.generateEqualityEquation(Constants.LEVEL_5);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//
//            }
//        }
    }

    @Test
    public void decimals() {
        System.out.println( 2.0f/3.0f );
        System.out.println( 3.0f * 0.66667f );

        System.out.println( 1.0f/3.0f );
        System.out.println( 3.0f * 0.33333f );
    }

//    @Test
//    public void generateLvl2Equation() {
//        System.out.println(EquationGeneration.generateEqualityEquation(Constants.LEVEL_2) + "");
//    }
//
//    @Test
//    public void generateLvl34Equation() {
//        System.out.println(EquationGeneration.generateEqualityEquation(Constants.LEVEL_4) + "");
//    }
//
//    @Test
//    public void generateLvl5Equation() {
//        System.out.println(EquationGeneration.generateEqualityEquation(Constants.LEVEL_5) + "");
//    }
//
//    @Test
//    public void isIntegerValid() {
//        IntegerAndDecimalMock iAd = new IntegerAndDecimalMock(12, 8);
//        assertTrue(iAd.isValid());
//        System.out.println(iAd);
//    }
}