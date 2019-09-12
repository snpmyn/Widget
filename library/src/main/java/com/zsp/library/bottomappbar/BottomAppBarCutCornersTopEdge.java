package com.zsp.library.bottomappbar;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.android.material.shape.ShapePath;

import value.WidgetLibraryMagic;

/**
 * @decs: BottomAppBarCutCornersTopEdge
 * A {@link com.google.android.material.bottomappbar.BottomAppBar} top edge that works with a Diamond shaped {@link com.google.android.material.floatingactionbutton.FloatingActionButton}.
 * @author: 郑少鹏
 * @date: 2019/7/19 20:30
 */
public class BottomAppBarCutCornersTopEdge extends BottomAppBarTopEdgeTreatment {
    private final float fabMargin;
    private final float cradleVerticalOffset;

    public BottomAppBarCutCornersTopEdge(float fabMargin, float roundedCornerRadius, float cradleVerticalOffset) {
        super(fabMargin, roundedCornerRadius, cradleVerticalOffset);
        this.fabMargin = fabMargin;
        this.cradleVerticalOffset = cradleVerticalOffset;
    }

    @Override
    public void getEdgePath(float length, float center, float interpolation, @NonNull ShapePath shapePath) {
        @SuppressLint("RestrictedApi") float fabDiameter = getFabDiameter();
        if (fabDiameter == 0) {
            shapePath.lineTo(length, 0);
            return;
        }
        float diamondSize = fabDiameter / 2f;
        @SuppressLint("RestrictedApi") float middle = center + getHorizontalOffset();
        float verticalOffsetRatio = cradleVerticalOffset / diamondSize;
        if (verticalOffsetRatio >= WidgetLibraryMagic.FLOAT_ONE_DOT_ZERO) {
            shapePath.lineTo(length, 0);
            return;
        }
        shapePath.lineTo(middle - (fabMargin + diamondSize - cradleVerticalOffset), 0);
        shapePath.lineTo(middle, (diamondSize - cradleVerticalOffset + fabMargin) * interpolation);
        shapePath.lineTo(middle + (fabMargin + diamondSize - cradleVerticalOffset), 0);
        shapePath.lineTo(length, 0);
    }
}
