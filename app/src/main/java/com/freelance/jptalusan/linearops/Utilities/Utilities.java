package com.freelance.jptalusan.linearops.Utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Utilities {
    private static String TAG = "Utilities";
    public static ArrayList<Integer> getFactors(int number) {
        if (number % 2 != 0) {
            number++;
        }
        ArrayList<Integer> out = new ArrayList<>();
        out.add(1);
        out.add(number);
        for(int i = 2; i < number; i++){
            if (number % i == 0)
                out.add(i);
        }

        Collections.sort(out);
        String temp = "";
        for (int i : out) {
            temp += i + ",";
        }
        Log.d(TAG, number + ":" + temp);
        return out;
    }
}
