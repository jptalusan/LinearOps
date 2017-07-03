package com.freelance.jptalusan.linearops.Views.ComboSeekBar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
    private Paint selectedCirclePaint;
    private Paint redPaint;
    private Paint greenPaint;
    private float mDotRadius;
    private Paint textSelected;
    private int mTextSize;
    private float mTextMargin;
    private int mTextHeight;
    private float mTextBottomPadding;

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

        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(toPix(10));
        redPaint.setAlpha(255);

        greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStrokeWidth(toPix(10));
        greenPaint.setAlpha(255);

        circleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleLinePaint.setColor(color);

        selectedCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedCirclePaint.setColor(Color.BLUE);
        selectedCirclePaint.setStrokeWidth(20);

        Rect textBounds = new Rect();
        textSelected.setTextSize((int) (mTextSize * 2));
        textSelected.getTextBounds("M", 0, 1, textBounds);

        textUnselected.setTextSize(mTextSize);
        textSelected.setTextSize(mTextSize);

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

    //TODO: do some math here to change colors of seekbar line
    //Use the text of dots. in after first if statement, can see values of dots and can also count dots, half of max is mid
    @Override
    public final void draw(Canvas canvas) {
        int middleY = this.getIntrinsicHeight() / 2;
        if (mDots.size() == 0) {
            canvas.drawLine(0, middleY, getBounds().right, middleY, redPaint);
            return;
        }
        int size = mDots.size() - 1;
//        Log.d(TAG, "Dots size: " + mDots.size());
        canvas.drawLine(mDots.get(0).mX, middleY, mDots.get(size / 2).mX, middleY, redPaint);
        canvas.drawLine(mDots.get(size / 2).mX, middleY, mDots.get(size).mX, middleY, greenPaint);

        for (ComboSeekBar.Dot dot : mDots) {
            drawText(canvas, dot, dot.mX, middleY);
            if (dot.isSelected) {
//                Log.d(TAG, "dot selected: " + dot.text);
//                if (Integer.parseInt(dot.text) < 0) {
//                    canvas.drawLine(mDots.get(size / 2).mX, middleY, dot.mX, middleY, selectLinePaint);
//                } else {
//                    canvas.drawLine(mDots.get(size / 2).mX, middleY, dot.mX, middleY, selectLinePaint);
//                }
                canvas.drawCircle(dot.mX, middleY, 20, selectedCirclePaint);

//                if (dot.isSelected) {
//                    canvas.drawLine(mDots.get(0).mX, middleY, dot.mX, middleY, selectLinePaint);
//                    canvas.drawLine(dot.mX, middleY, mDots.get(mDots.size() - 1).mX, middleY, unselectLinePaint);
//                }
            }
            canvas.drawCircle(dot.mX, middleY, mDotRadius, circleLinePaint);
        }
    }

    /**
     * @param canvas canvas.
     * @param dot    current dot.
     * @param x      x cor.
     * @param y      y cor.
     */
    private void drawText(Canvas canvas, ComboSeekBar.Dot dot, float x, float y) {
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

        canvas.drawText(dot.text, xres, yres, dot.isSelected ? textSelected : textUnselected);
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
