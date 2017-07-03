package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JPTalusan on 07/05/2017.
 */

//TODO: Add animations here?
public class LinearOpsImageView extends AppCompatTextView {
    private String type = "";
    protected SharedPreferences prefs;

    public LinearOpsImageView(Context context) {
        super(context);
    }

    public LinearOpsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearOpsImageView, 0, 0);
            try {
            type = a.getString(R.styleable.LinearOpsImageView_type);
        } finally {
            a.recycle();
        }
    }

    public LinearOpsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearOpsImageView, 0, 0);
        try {
            type = a.getString(R.styleable.LinearOpsImageView_type);
        } finally {
            a.recycle();
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setValueText(String text) {
        prefs = getContext().getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if (prefs.getInt(Constants.LINEAR_EQ_LEVEL, 1) >= Constants.LEVEL_4) {
            setText(text);
            setTextColor(Color.BLACK);
            setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        } else {
            if (text.equals("X")) {
                setText("?");
                setTextColor(Color.BLACK);
                setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            }
        }
    }
}
