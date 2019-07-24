package com.zsp.library.bottomappbar.kit;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.zsp.library.bottomappbar.bottomappbartopedgetreatment.BottomAppBarCutCornersTopEdge;

/**
 * Created on 2019/7/24.
 *
 * @author 郑少鹏
 * @desc BottomAppBarKit
 */
public class BottomAppBarKit {
    /**
     * 建BottomAppBar形状外观
     *
     * @param bottomAppBar         BottomAppBar
     * @param floatingActionButton FloatingActionButton
     */
    public static void setUpBottomAppBarShapeAppearance(BottomAppBar bottomAppBar, FloatingActionButton floatingActionButton) {
        ShapeAppearanceModel shapeAppearanceModel = floatingActionButton.getShapeAppearance();
        boolean cutCornersFab = shapeAppearanceModel.getBottomLeftCorner() instanceof CutCornerTreatment
                && shapeAppearanceModel.getBottomRightCorner() instanceof CutCornerTreatment;
        BottomAppBarTopEdgeTreatment bottomAppBarTopEdgeTreatment = cutCornersFab
                ? new BottomAppBarCutCornersTopEdge(
                bottomAppBar.getFabCradleMargin(),
                bottomAppBar.getFabCradleRoundedCornerRadius(),
                bottomAppBar.getCradleVerticalOffset())
                : new BottomAppBarTopEdgeTreatment(
                bottomAppBar.getFabCradleMargin(),
                bottomAppBar.getFabCradleRoundedCornerRadius(),
                bottomAppBar.getCradleVerticalOffset());
        MaterialShapeDrawable materialShapeDrawable = (MaterialShapeDrawable) bottomAppBar.getBackground();
        materialShapeDrawable.getShapeAppearanceModel().setTopEdge(bottomAppBarTopEdgeTreatment);
        materialShapeDrawable.invalidateSelf();
    }
}
