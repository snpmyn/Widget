package com.zsp.library.dialog.sweetalertdialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.zsp.library.R;

/**
 * Created on 2017/11/3.
 *
 * @author 郑少鹏
 * @desc Rotate3dAnimation
 */
public class Rotate3dAnimation extends Animation {
    private static final int ROLL_BY_X = 0;
    private static final int ROLL_BY_Y = 1;
    private static final int ROLL_BY_Z = 2;
    private int xPivotType = ABSOLUTE;
    private int yPivotType = ABSOLUTE;
    private float xPivotValue = 0.0f;
    private float yPivotValue = 0.0f;
    private float mFromDegrees;
    private float mToDegrees;
    private float xPivot;
    private float yPivot;
    private Camera mCamera;
    private int mRollType;

    public Rotate3dAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Rotate3dAnimation);
        mFromDegrees = a.getFloat(R.styleable.Rotate3dAnimation_fromDeg, 0.0f);
        mToDegrees = a.getFloat(R.styleable.Rotate3dAnimation_toDeg, 0.0f);
        mRollType = a.getInt(R.styleable.Rotate3dAnimation_rollType, ROLL_BY_X);
        Description d = parseValue(a.peekValue(R.styleable.Rotate3dAnimation_valuePivotX));
        xPivotType = d.type;
        xPivotValue = d.value;
        d = parseValue(a.peekValue(R.styleable.Rotate3dAnimation_valuePivotY));
        yPivotType = d.type;
        yPivotValue = d.value;
        a.recycle();
        initializePivotPoint();
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees) {
        mRollType = rollType;
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        xPivot = 0.0f;
        yPivot = 0.0f;
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees, float xPivot, float yPivot) {
        mRollType = rollType;
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        xPivotType = ABSOLUTE;
        yPivotType = ABSOLUTE;
        xPivotValue = xPivot;
        yPivotValue = yPivot;
        initializePivotPoint();
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees, int xPivotType, float xPivotValue, int yPivotType, float yPivotValue) {
        mRollType = rollType;
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        this.xPivotValue = xPivotValue;
        this.xPivotType = xPivotType;
        this.yPivotValue = yPivotValue;
        this.yPivotType = yPivotType;
        initializePivotPoint();
    }

    private Description parseValue(TypedValue value) {
        Description d = new Description();
        if (value == null) {
            d.type = ABSOLUTE;
            d.value = 0;
        } else {
            if (value.type == TypedValue.TYPE_FRACTION) {
                d.type = (value.data & TypedValue.COMPLEX_UNIT_MASK) == TypedValue.COMPLEX_UNIT_FRACTION_PARENT ? RELATIVE_TO_PARENT : RELATIVE_TO_SELF;
                d.value = TypedValue.complexToFloat(value.data);
                return d;
            } else if (value.type == TypedValue.TYPE_FLOAT) {
                d.type = ABSOLUTE;
                d.value = value.getFloat();
                return d;
            } else if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT) {
                d.type = ABSOLUTE;
                d.value = value.data;
                return d;
            }
        }
        d.type = ABSOLUTE;
        d.value = 0.0f;
        return d;
    }

    private void initializePivotPoint() {
        if (xPivotType == ABSOLUTE) {
            xPivot = xPivotValue;
        }
        if (yPivotType == ABSOLUTE) {
            yPivot = yPivotValue;
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
        xPivot = resolveSize(xPivotType, xPivotValue, width, parentWidth);
        yPivot = resolveSize(yPivotType, yPivotValue, height, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
        final Matrix matrix = t.getMatrix();
        mCamera.save();
        switch (mRollType) {
            case ROLL_BY_X:
                mCamera.rotateX(degrees);
                break;
            case ROLL_BY_Y:
                mCamera.rotateY(degrees);
                break;
            case ROLL_BY_Z:
                mCamera.rotateZ(degrees);
                break;
            default:
                break;
        }
        mCamera.getMatrix(matrix);
        mCamera.restore();
        matrix.preTranslate(-xPivot, -yPivot);
        matrix.postTranslate(xPivot, yPivot);
    }

    protected static class Description {
        float value;
        int type;
    }
}