package com.zsp.library.layout.camber;

import android.graphics.Path;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

/**
 * 路径提供器
 */
class PathProvider {
    @NonNull
    static Path getOutlinePath(int width, int height, int curvatureHeight, int direction, int gravity) {
        Path mPath = new Path();
        if (direction == CamberImageView.CurvatureDirection.OUTWARD) {
            if (gravity == CamberImageView.Gravity.TOP) {
                mPath.moveTo(0, 0);
                mPath.lineTo(0, height - curvatureHeight);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), height + curvatureHeight, width, height - curvatureHeight);
                mPath.lineTo(width, 0);
                mPath.lineTo(0, 0);
            } else {
                mPath.moveTo(0, height);
                mPath.lineTo(0, curvatureHeight);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), -curvatureHeight, width, curvatureHeight);
                mPath.lineTo(width, height);
            }
            mPath.close();
        } else if (direction == CamberImageView.CurvatureDirection.INWARD) {
            if (gravity == CamberImageView.Gravity.TOP) {
                mPath.moveTo(0, 0);
                mPath.lineTo(0, height);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), height - curvatureHeight, width, height);
                mPath.lineTo(width, 0);
                mPath.lineTo(0, 0);
            } else {
                mPath.moveTo(0, height);
                mPath.lineTo(0, 0);
                mPath.cubicTo(0, 0, new BigDecimal(width / 2).floatValue(), curvatureHeight, width, curvatureHeight);
                mPath.lineTo(width, height);
                mPath.lineTo(0, height);
            }
            mPath.close();
        }
        return mPath;
    }

    static Path getClipPath(int width, int height, int curvatureHeight, int direction, int gravity) {
        Path mPath = new Path();
        if (direction == CamberImageView.CurvatureDirection.OUTWARD) {
            if (gravity == CamberImageView.Gravity.TOP) {
                mPath.moveTo(0, height - curvatureHeight);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), height + curvatureHeight, width, height - curvatureHeight);
                mPath.lineTo(width, 0);
                mPath.lineTo(width, height);
                mPath.lineTo(0, height);
            } else {
                mPath.moveTo(0, 0);
                mPath.lineTo(width, 0);
                mPath.lineTo(width, curvatureHeight);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), -curvatureHeight, 0, curvatureHeight);
                mPath.lineTo(0, 0);
            }
        } else {
            if (gravity == CamberImageView.Gravity.TOP) {
                mPath.moveTo(0, height);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), height - 2 * curvatureHeight, width, height);
                mPath.lineTo(width, height);
            } else {
                mPath.moveTo(0, 0);
                mPath.lineTo(width, 0);
                mPath.quadTo(new BigDecimal(width / 2).floatValue(), 2 * curvatureHeight, 0, 0);
                mPath.lineTo(0, 0);
            }
        }
        mPath.close();
        return mPath;
    }
}
