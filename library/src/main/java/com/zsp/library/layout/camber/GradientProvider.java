package com.zsp.library.layout.camber;

import android.graphics.LinearGradient;
import android.graphics.Shader;

/**
 * 弧形提供器
 */
class GradientProvider {
    static Shader getShader(int gradientStartColor, int gradientEndColor, int gradientDirection, int width, int height) {
        switch (gradientDirection) {
            case CamberImageView.Gradient.TOP_TO_BOTTOM:
                return new LinearGradient(0, 0, 0, height, gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
            case CamberImageView.Gradient.BOTTOM_TO_TOP:
                return new LinearGradient(0, height, 0, 0, gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
            case CamberImageView.Gradient.LEFT_TO_RIGHT:
                return new LinearGradient(0, 0, width, 0, gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
            case CamberImageView.Gradient.RIGHT_TO_LEFT:
                return new LinearGradient(width, 0, 0, 0, gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
            default:
                return new LinearGradient(0, 0, height, 0, gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
        }
    }
}
