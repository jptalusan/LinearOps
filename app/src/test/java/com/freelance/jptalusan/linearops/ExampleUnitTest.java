package com.freelance.jptalusan.linearops;

import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.EquationGeneration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
//        int n = 10;
//        for(int i = 0; i < n; ++i) {
//            EquationGeneration.generateEqualityEquation(Constants.LEVEL_1);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//
//            }
//        }
    }

    @Test
    public void generateLvl2Equation() {
        System.out.println(EquationGeneration.generateEqualityEquation(Constants.LEVEL_2) + "");
    }

    @Test
    public void generateLvl34Equation() {
        System.out.println(EquationGeneration.generateEqualityEquation(Constants.LEVEL_4) + "");
    }

    @Test
    public void generateLvl5Equation() {
        System.out.println(EquationGeneration.generateEqualityEquation(Constants.LEVEL_5) + "");
    }

    @Test
    public void isIntegerValid() {
        IntegerAndDecimalMock iAd = new IntegerAndDecimalMock(12, 8);
        assertTrue(iAd.isValid());
        System.out.println(iAd);
    }
}