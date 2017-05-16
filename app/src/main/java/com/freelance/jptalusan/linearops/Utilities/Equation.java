package com.freelance.jptalusan.linearops.Utilities;

/**
 * Created by JPTalusan on 01/05/2017.
 */

public class Equation {
    private int ax = 0;
    private int b = 0;
    private int cx = 0;
    private int d = 0;
    private int level = 0;


    public Equation() {
    }

    public Equation(int ax, int b, int cx, int d, int level) {
        this.ax = ax;
        this.b = b;
        this.cx = cx;
        this.d = d;
        this.level = level;
    }

    public String printEquation() {
        String temp = "";
        switch(level) {
            case Constants.LEVEL_1:
                temp = ax + "x=" + b;
                break;
            case Constants.LEVEL_2:
                temp = ax + "x+" + b + "=" + cx;
                temp = temp.replace("+-", "-");
                break;
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
            case Constants.LEVEL_5:
                temp = ax + "x+" + b + "=" + cx + "x+" + d;
                temp = temp.replace("0+", "");
                temp = temp.replace("+0", "");
                temp = temp.replace("+-", "-");
                break;
            default:
                break;
        }
        return temp;
    }

    @Override
    public String toString() {
        switch(level) {
            case Constants.LEVEL_1: return "1: [ax = b]: " + printEquation();
            case Constants.LEVEL_2: return "2: [ax + b = c]: " + printEquation();
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
            case Constants.LEVEL_5: return "3-5: [ax + b = cx + d]: " + printEquation();
            default:                return "";
        }
    }

    public double getX() {
        switch(level) {
            case Constants.LEVEL_1: return b / ax;
            case Constants.LEVEL_2: return ((cx - b) / ax);
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
            case Constants.LEVEL_5: return ((b - d)/(ax - cx));
            default:                return 0;
        }
    }

    public int getAx() {
        return ax;
    }

    public int getB() {
        return b;
    }

    public int getCx() {
        return cx;
    }

    public int getD() {
        return d;
    }

    public int getLevel() {
        return level;
    }

    //TODO: use this to confirm if user has already answered correctly
    public int[] getAxEqualsBForm() {
        int[] output = {0, 0};

        switch(level) {
            case Constants.LEVEL_1:
                output[0] = ax;
                output[1] = b;
                break;
            case Constants.LEVEL_2:
                output[0] = ax;
                output[1] = cx - b;
                break;
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
            case Constants.LEVEL_5:
                output[0] = ax - cx;
                output[1] = b - d;
                break;
            default:
                break;
        }

        return output;
    }
}
