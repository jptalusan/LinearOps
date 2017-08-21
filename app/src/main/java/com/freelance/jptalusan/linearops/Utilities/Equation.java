package com.freelance.jptalusan.linearops.Utilities;

/**
 * Created by JPTalusan on 01/05/2017.
 */

public class Equation {
    private static String TAG = "Equation";
    private double ax = 0;
    private double b = 0;
    private double cx = 0;
    private double d = 0;
    private int level = 0;


    public Equation() {
    }

    public Equation(double ax, double b, double cx, double d, int level) {
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
                temp = temp.replace(".0", "");
                temp = temp.replace("+-", " - ");
                temp = temp.replace("x+", "x + ");
                temp = temp.replace("=", " = ");
                break;
            case Constants.LEVEL_2:
                temp = ax + "x+" + b + "=" + cx;
                temp = temp.replace(".0", "");
                temp = temp.replace("+-", " - ");
                temp = temp.replace("x+", "x + ");
                temp = temp.replace("=", " = ");
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
        System.out.println("LEVEL: " + level);
        switch(level) {
            case Constants.LEVEL_1: return "1: [ax = b]: " + printEquation();
            case Constants.LEVEL_2: return "2: [ax + b = c]: " + printEquation();
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
            case Constants.LEVEL_5: return "3-5: [ax + b = cx + d]: " + printEquation();
            default:                return "FAILED";
        }
    }

    public double getX() {
        switch(level) {
            case Constants.LEVEL_1: return b / ax;
            case Constants.LEVEL_2: return ((cx - b) / ax);
            case Constants.LEVEL_3:
            case Constants.LEVEL_4: return (d - b) / (ax - cx);
            case Constants.LEVEL_5:
//                System.out.println("ax: " + ax);
//                System.out.println("b: " + b);
//                System.out.println("cx: " + cx);
//                System.out.println("d: " + d);
//                System.out.println("X: " + (-((b - d)/(ax - cx))));
                return -((b - d)/(ax - cx));
            default:                return 0;
        }
    }

    public double getAx() {
        return ax;
    }

    public double getB() {
        return b;
    }

    public double getCx() {
        return cx;
    }

    public double getD() {
        return d;
    }

    public int getLevel() {
        return level;
    }

    //TODO: use this to confirm if user has already answered correctly
    public double[] getAxEqualsBForm() {
        double[] output = {0, 0};

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

    public boolean isUserCorrect(int answer) {
        return getX() == answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Equation.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Equation other = (Equation) obj;
        if (this.ax != other.ax
                && this.b != other.b
                && this.cx != other.cx
                && this.d != other.d) {
            return false;
        }
        return true;
    }

}
