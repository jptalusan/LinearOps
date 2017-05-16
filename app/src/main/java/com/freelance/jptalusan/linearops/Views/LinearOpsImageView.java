package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.freelance.jptalusan.linearops.R;

/**
 * Created by JPTalusan on 07/05/2017.
 */

//TODO: Add animations here?
public class LinearOpsImageView extends AppCompatImageView {
    private String type = "";

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
}
