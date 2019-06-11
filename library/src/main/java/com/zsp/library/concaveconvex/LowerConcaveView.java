package com.zsp.library.concaveconvex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.zsp.library.R;

import java.math.BigDecimal;

/**
 * Created on 2019/6/11.
 *
 * @author 郑少鹏
 * @desc 下凹视图
 */
@SuppressLint("AppCompatCustomView")
public class LowerConcaveView extends ImageView {
    /**
     * 弧高
     */
    private int arcHeight;

    public LowerConcaveView(Context context) {
        this(context, null);
    }

    public LowerConcaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LowerConcaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LowerConcaveView);
        arcHeight = typedArray.getDimensionPixelSize(R.styleable.LowerConcaveView_lowerConcaveViewArcHeight, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight());
        path.quadTo(new BigDecimal(getWidth() / 2).floatValue(), getHeight() - 2 * arcHeight, getWidth(), getHeight());
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
