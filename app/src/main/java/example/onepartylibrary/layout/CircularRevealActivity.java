package example.onepartylibrary.layout;

import android.animation.Animator;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.layout.circularreveal.CircularRevealAnimation;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 循环揭示页
 * @author: 郑少鹏
 * @date: 2019/8/27 11:54
 */
public class CircularRevealActivity extends AppCompatActivity {
    @BindView(R.id.circularRevealActivityTv)
    TextView circularRevealActivityTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_reveal);
        ButterKnife.bind(this);
        execute();
    }

    private void execute() {
        circularRevealActivityTv.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> animator());
    }

    private void animator() {
        // get the center for the clipping circle
        int cx = (circularRevealActivityTv.getLeft() + circularRevealActivityTv.getRight()) / 2;
        int cy = (circularRevealActivityTv.getTop() + circularRevealActivityTv.getBottom()) / 2;
        // get the final radius for the clipping circle
        int dx = Math.max(cx, circularRevealActivityTv.getWidth() - cx);
        int dy = Math.max(cy, circularRevealActivityTv.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);
        // android native animator
        Animator animator = CircularRevealAnimation.createCircularReveal(circularRevealActivityTv, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(2000);
        animator.start();
    }
}
