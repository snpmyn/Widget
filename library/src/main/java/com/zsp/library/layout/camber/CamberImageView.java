package com.zsp.library.layout.camber;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;

import com.zsp.library.R;

import timber.log.Timber;
import value.WidgetLibraryMagic;

/**
 * @decs: 弧形ImageView
 * @author: 郑少鹏
 * @date: 2019/10/26 14:57
 */
public class CamberImageView extends AppCompatImageView {
    Context mContext;
    Path mClipPath;
    int width = 0;
    int height = 0;
    Bitmap mBitmap;
    Paint tintPaint;
    Paint shaderPaint;
    /**
     * whether TOP or BOTTOM
     */
    int gravity = Gravity.TOP;
    /**
     * Changes the amount of curve.
     * Default is 50.
     */
    int curvatureHeight = 50;
    /**
     * varies from 0-255
     */
    int tintAmount = 0;
    /**
     * Whether manual or automatic.
     * Default is TintMode.AUTOMATIC.
     */
    int tintMode = TintMode.MANUAL;
    /**
     * color of tint to be applied
     */
    int tintColor = 0;
    int gradientDirection = 0;
    int gradientStartColor = Color.TRANSPARENT;
    int gradientEndColor = Color.TRANSPARENT;
    int curvatureDirection = CurvatureDirection.OUTWARD;

    static public class Gravity {
        static final int TOP = 0;
        static final int BOTTOM = 1;
    }

    static public class TintMode {
        static final int AUTOMATIC = 0;
        static final int MANUAL = 1;
    }

    static class Gradient {
        static final int TOP_TO_BOTTOM = 0;
        static final int BOTTOM_TO_TOP = 1;
        static final int LEFT_TO_RIGHT = 2;
        static final int RIGHT_TO_LEFT = 3;
    }

    static class CurvatureDirection {
        static final int OUTWARD = 0;
        static final int INWARD = 1;
    }

    Paint mPaint;
    private PorterDuffXfermode porterDuffXfermode;

    public CamberImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CamberImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        shaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClipPath = new Path();
        TypedArray styledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.CamberImageView, 0, 0);
        if (styledAttributes.hasValue(R.styleable.CamberImageView_CamberImageViewCurvature)) {
            curvatureHeight = (int) styledAttributes.getDimension(R.styleable.CamberImageView_CamberImageViewCurvature, getDpForPixel(curvatureHeight));
        }
        if (styledAttributes.hasValue(R.styleable.CamberImageView_CamberImageViewCamberTintAlpha)) {
            if (styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewCamberTintAlpha, 0) <= WidgetLibraryMagic.INT_TWO_HUNDRED_FIFTY_FIVE
                    && styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewCamberTintAlpha, 0) >= 0) {
                tintAmount = styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewCamberTintAlpha, 0);
            }
        }
        if (styledAttributes.hasValue(R.styleable.CamberImageView_CamberImageViewGravity)) {
            if (styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewGravity, 0) == Gravity.BOTTOM) {
                gravity = Gravity.BOTTOM;
            } else {
                gravity = Gravity.TOP;
            }
        }
        if (styledAttributes.hasValue(R.styleable.CamberImageView_CamberImageViewCamberTintMode)) {
            if (styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewCamberTintMode, 0) == TintMode.AUTOMATIC) {
                tintMode = TintMode.AUTOMATIC;
            } else {
                tintMode = TintMode.MANUAL;
            }
        }
        if (styledAttributes.hasValue(R.styleable.CamberImageView_CamberImageViewGradientDirection)) {
            gradientDirection = styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewGradientDirection, 0);
        }
        // Default start color is transparent.
        gradientStartColor = styledAttributes.getColor(R.styleable.CamberImageView_CamberImageViewGradientStartColor, Color.TRANSPARENT);
        // Default end color is transparent.
        gradientEndColor = styledAttributes.getColor(R.styleable.CamberImageView_CamberImageViewGradientEndColor, Color.TRANSPARENT);
        if (styledAttributes.hasValue(R.styleable.CamberImageView_CamberImageViewCamberTintColor)) {
            tintColor = styledAttributes.getColor(R.styleable.CamberImageView_CamberImageViewCamberTintColor, 0);
        }
        // Default curvature direction would be outward.
        curvatureDirection = styledAttributes.getInt(R.styleable.CamberImageView_CamberImageViewCurvatureDirection, 0);
        styledAttributes.recycle();
        if (getDrawable() != null) {
            BitmapDrawable mBitmapDrawable = (BitmapDrawable) getDrawable();
            mBitmap = mBitmapDrawable.getBitmap();
            pickColorFromBitmap(mBitmap);
        } else {
            if (getBackground() != null) {
                if (!(getBackground() instanceof ColorDrawable)) {
                    BitmapDrawable mBitmapDrawable = (BitmapDrawable) getBackground();
                    mBitmap = mBitmapDrawable.getBitmap();
                    pickColorFromBitmap(mBitmap);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        mClipPath = PathProvider.getClipPath(width, height, curvatureHeight, curvatureDirection, gravity);
        ViewCompat.setElevation(this, ViewCompat.getElevation(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                setOutlineProvider(getOutlineProvider());
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    private void pickColorFromBitmap(Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            if (tintMode == TintMode.AUTOMATIC) {
                int defaultColor = 0x000000;
                tintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                if (palette != null) {
                    if (palette.getDarkMutedColor(defaultColor) != 0) {
                        System.out.println(palette.getMutedColor(defaultColor));
                        tintPaint.setColor(Color.parseColor("#" + Math.abs(palette.getDarkVibrantColor(defaultColor))));
                    } else if (palette.getDarkVibrantColor(defaultColor) != 0) {
                        System.out.println(palette.getMutedColor(defaultColor));
                        tintPaint.setColor(Color.parseColor("#" + Math.abs(palette.getDarkMutedColor(defaultColor))));
                    } else {
                        tintPaint.setColor(Color.WHITE);
                    }
                }
            } else {
                tintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                tintPaint.setColor(tintColor);
            }
            tintPaint.setAlpha(tintAmount);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                try {
                    outline.setConvexPath(PathProvider.getOutlinePath(width, height, curvatureHeight, curvatureDirection, gravity));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        } else {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        }
        super.onDraw(canvas);
        mPaint.setXfermode(porterDuffXfermode);
        if (tintPaint != null) {
            canvas.drawColor(tintPaint.getColor());
        }
        Shader mShader = GradientProvider.getShader(gradientStartColor, gradientEndColor, gradientDirection, getWidth(), getHeight());
        shaderPaint.setShader(mShader);
        canvas.drawPaint(shaderPaint);
        canvas.drawPath(mClipPath, mPaint);
        canvas.restoreToCount(saveCount);
        mPaint.setXfermode(null);
    }

    private int getDpForPixel(int pixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, mContext.getResources().getDisplayMetrics());
    }

    public void setCurvature(int height) {
        curvatureHeight = getDpForPixel(height);
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void setTintMode(int tintMode) {
        this.tintMode = tintMode;
    }

    public void setTintAmount(int tintAmount) {
        this.tintAmount = tintAmount;
    }

    public void setGradientDirection(int direction) {
        this.gradientDirection = direction;
    }

    public void setGradientStartColor(int startColor) {
        this.gradientStartColor = startColor;
    }

    public void setGradientEndColor(int endColor) {
        this.gradientEndColor = endColor;
    }

    public void setCurvatureDirection(int direction) {
        this.curvatureDirection = direction;
    }
}
