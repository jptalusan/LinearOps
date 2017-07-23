package com.freelance.jptalusan.linearops.Views.ComboSeekBar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;

import java.util.List;

/**
 * seekbar background with text on it.
 *
 * @author sazonov-adm
 */
public class CustomDrawable extends Drawable {
    private final static String TAG = "CustomDrawable";
    private final ComboSeekBar mySlider;
    private final Drawable myBase;
    private final Paint textUnselected;
    private float mThumbRadius;
    /**
     * paints.
     */
    private final Paint unselectLinePaint;
    private List<ComboSeekBar.Dot> mDots;
    private Paint selectLinePaint;
    private Paint circleLinePaint;
    private Paint redCircleLinePaint;
    private Paint greenCircleLinePaint;
    private Paint selectedCirclePaint;
    private Paint redPaint;
    private Paint greenPaint;
    private float mDotRadius;
    private Paint textSelected;
    private int mTextSize;
    private float mTextMargin;
    private int mTextHeight;
    private float mTextBottomPadding;
    private Paint redText;
    private Paint greenText;

    public CustomDrawable(Drawable base, ComboSeekBar slider,
                          float dotRadius, float thumbRadius, List<ComboSeekBar.Dot> dots,
                          int color, int textSize, int textBottomPadding) {
        mySlider = slider;
        myBase = base;
        mDots = dots;
        mTextSize = textSize;
        textUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
        textUnselected.setColor(color);
        textUnselected.setAlpha(255);

        redText = new Paint(Paint.ANTI_ALIAS_FLAG);
        redText.setColor(Color.RED);
        redText.setAlpha(255);

        greenText = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenText.setColor(Color.GREEN);
        greenText.setAlpha(255);

        textSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
        textSelected.setTypeface(Typeface.DEFAULT_BOLD);
        textSelected.setUnderlineText(true);
        textSelected.setStrokeWidth(15);
        textSelected.setColor(Color.DKGRAY);
        textSelected.setAlpha(255);
        textSelected.setTextSize(50);

        mThumbRadius = thumbRadius;

        unselectLinePaint = new Paint();
        unselectLinePaint.setColor(color);
        unselectLinePaint.setStrokeWidth(toPix(1));

        selectLinePaint = new Paint();
        selectLinePaint.setColor(color);
        selectLinePaint.setStrokeWidth(toPix(3));

        redCircleLinePaint = new Paint();
        redCircleLinePaint.setColor(Color.parseColor("#FFCDD2"));
        redCircleLinePaint.setStrokeWidth(toPix(1));

        greenCircleLinePaint = new Paint();
        greenCircleLinePaint.setColor(Color.parseColor("#E0F2F1"));
        greenCircleLinePaint.setStrokeWidth(toPix(1));

        redPaint = new Paint();
        redPaint.setColor(Color.parseColor("#C62828"));
        redPaint.setStrokeWidth(toPix(10));
        redPaint.setAlpha(255);

        greenPaint = new Paint();
        greenPaint.setColor(Color.parseColor("#00695C"));
        greenPaint.setStrokeWidth(toPix(10));
        greenPaint.setAlpha(255);

        circleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleLinePaint.setColor(color);

        selectedCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedCirclePaint.setColor(Color.BLUE);
        selectedCirclePaint.setStrokeWidth(20);

        Rect textBounds = new Rect();
        textSelected.setTextSize(mTextSize * 2);
        textSelected.getTextBounds("M", 0, 1, textBounds);

        greenText.setTextSize(mTextSize * 2);
        greenText.getTextBounds("M", 0, 1, textBounds);

        redText.setTextSize(mTextSize * 2);
        redText.getTextBounds("M", 0, 1, textBounds);

        textUnselected.setTextSize(mTextSize);
        textSelected.setTextSize(mTextSize);
        greenText.setTextSize(mTextSize);
        redText.setTextSize(mTextSize);

        mTextHeight = textBounds.height() + 20;
        mDotRadius = dotRadius;
        mTextMargin = toPix(3);
        mTextBottomPadding = textBottomPadding;
    }

    private float toPix(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mySlider.getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected final void onBoundsChange(Rect bounds) {
        myBase.setBounds(bounds);
    }

    @Override
    protected final boolean onStateChange(int[] state) {
        invalidateSelf();
        return false;
    }

    @Override
    public final boolean isStateful() {
        return true;
    }

    //Use the text of dots. in after first if statement, can see values of dots and can also count dots, half of max is mid
    //TODO: Fix for Level 5, must read text before coloring
    @Override
    public final void draw(@NonNull Canvas canvas) {
        Log.d(TAG, "CustomDrawable draw()");
        String s = "";
        boolean hasBothSigns = false;
        boolean allPositives = true;
        int zeroIndex = 0;
        for (int i = 0; i < mDots.size(); ++i) {
            String dotText = mDots.get(i).text;
            s += dotText + ", ";
            if (dotText.equals("0")) {
                hasBothSigns = true;
                zeroIndex = i;
            }
            if (dotText.equals("-")) {
                allPositives = false;
            }
        }


        Log.d(TAG, s);

        int middleY = this.getIntrinsicHeight() / 2;
        int size = mDots.size() - 1;

        if (allPositives && !hasBothSigns) {
            drawText(canvas, mDots.get(0), mDots.get(size).mX, middleY, "Positive");
            canvas.drawCircle(mDots.get(0).mX, middleY, mDotRadius, greenCircleLinePaint);
            canvas.drawLine(mDots.get(0).mX, middleY, mDots.get(size).mX, middleY, greenPaint);
        } else if (!allPositives && !hasBothSigns) {
            drawText(canvas, mDots.get(0), mDots.get(size).mX, middleY, "Negative");
            canvas.drawCircle(mDots.get(0).mX, middleY, mDotRadius, redCircleLinePaint);
            canvas.drawLine(mDots.get(0).mX, middleY, mDots.get(size).mX, middleY, redPaint);
        } else {
            if (mDots.size() == 0) {
                canvas.drawLine(0, middleY, getBounds().right, middleY, redPaint);
                return;
            }

            canvas.drawLine(mDots.get(0).mX, middleY, mDots.get(zeroIndex).mX, middleY, redPaint);
            canvas.drawLine(mDots.get(zeroIndex).mX, middleY, mDots.get(size).mX, middleY, greenPaint);

            for (int i = 0; i < zeroIndex; ++i) {
                drawText(canvas, mDots.get(i), mDots.get(i).mX, middleY, "Negative");
                canvas.drawCircle(mDots.get(i).mX, middleY, mDotRadius, redCircleLinePaint);
            }

            drawText(canvas, mDots.get(zeroIndex), mDots.get(zeroIndex).mX, middleY, "Zero");
            canvas.drawCircle(mDots.get(zeroIndex).mX, middleY, mDotRadius, circleLinePaint);

            for (int i = zeroIndex + 1; i <= size; ++i) {
                drawText(canvas, mDots.get(i), mDots.get(i).mX, middleY, "Positive");
                canvas.drawCircle(mDots.get(i).mX, middleY, mDotRadius, greenCircleLinePaint);
            }
        }

        for (ComboSeekBar.Dot dot : mDots) {
            if (dot.isSelected) {
                canvas.drawCircle(dot.mX, middleY, 20, selectedCirclePaint);
            }
        }
    }

    /**
     * @param canvas canvas.
     * @param dot    current dot.
     * @param x      x cor.
     * @param y      y cor.
     */
    private void drawText(Canvas canvas, ComboSeekBar.Dot dot, float x, float y, String sign) {
        final Rect textBounds = new Rect();
        textSelected.getTextBounds(dot.text, 0, dot.text.length(), textBounds);
        float xres;
        if (dot.id == (mDots.size() - 1)) {
            xres = getBounds().width() - textBounds.width();
        } else if (dot.id == 0) {
            xres = 0;
        } else {
            xres = x - (textBounds.width() / 2);
        }

        float yres = y - mThumbRadius - mTextBottomPadding;

        Paint textPaint;
        switch (sign) {
            case "Positive":
                textPaint = dot.isSelected ? textSelected : greenText;
                break;
            case "Negative":
                textPaint = dot.isSelected ? textSelected : redText;
                break;
            default:
                textPaint = dot.isSelected ? textSelected : textUnselected;
                break;
        }
        canvas.drawText(dot.text, xres, yres, textPaint);

    }


    @Override
    public final int getIntrinsicHeight() {
        return (int) (mThumbRadius + mTextHeight + 2 * mTextMargin + 2 * mTextBottomPadding);
    }

    @Override
    public final int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }
}
