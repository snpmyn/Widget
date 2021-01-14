package example.onepartylibrary.animation.circular;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.zsp.library.animation.circular.CircularFlex;
import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 圆形伸缩页
 * @author: 郑少鹏
 * @date: 2019/9/10 17:03
 */
public class CircularFlexActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.circularFlexActivityPbChange)
    ProgressBar circularFlexActivityPbChange;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.circularFlexActivityMbChange)
    MaterialButton circularFlexActivityMbChange;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.circularFlexActivityLogin)
    ProgressBar circularFlexActivityLogin;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.circularFlexActivityMbLogin)
    MaterialButton circularFlexActivityMbLogin;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.circularFlexActivityTvContent)
    TextView circularFlexActivityTvContent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.circularFlexActivityIv)
    ImageView circularFlexActivityIv;
    /**
     * 内容可见
     */
    private boolean contentVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_flex);
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.circularFlexActivityMbStartActivityWithPicture,
            R.id.circularFlexActivityMbStartActivityWithColor,
            R.id.circularFlexActivityMbChange,
            R.id.circularFlexActivityMbLogin,
            R.id.circularFlexActivityIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // start Activity with picture
            case R.id.circularFlexActivityMbStartActivityWithPicture:
                startActivityWithPicture(view);
                break;
            // start Activity with color
            case R.id.circularFlexActivityMbStartActivityWithColor:
                startActivityWithColor(view);
                break;
            // 变
            case R.id.circularFlexActivityMbChange:
                change();
                break;
            // 登录
            case R.id.circularFlexActivityMbLogin:
                login();
                break;
            // ImageView
            case R.id.circularFlexActivityIv:
                imageView(view);
                break;
            default:
                break;
        }
    }

    /**
     * Start Activity with picture.
     *
     * @param view View
     */
    private void startActivityWithPicture(View view) {
        // 图展满后启新Activity
        CircularFlex.fullActivity(this, view)
                .colorOrImageResource(R.mipmap.ic_launcher)
                .go(() -> IntentUtils.jumpNoBundle(CircularFlexActivity.this, CircularFlexContentActivity.class));
    }

    /**
     * Start Activity with color.
     * <p>
     * default R.color.colorPrimary
     *
     * @param view View
     */
    private void startActivityWithColor(View view) {
        // 色展满后启新Activity
        CircularFlex.fullActivity(this, view)
                .colorOrImageResource(R.color.red)
                .deployReturnAnimator(animator -> {
                    // override CircularAnim.setDuration()
                    animator.setDuration(600L);
                    animator.setInterpolator(new AccelerateInterpolator());
                })
                .go(() -> IntentUtils.jumpNoBundle(CircularFlexActivity.this, CircularFlexContentActivity.class));
    }

    /**
     * 变
     */
    private void change() {
        circularFlexActivityPbChange.setVisibility(View.VISIBLE);
        // 收缩按钮
        CircularFlex.hide(circularFlexActivityMbChange).triggerView(circularFlexActivityMbLogin).go();
    }

    /**
     * 登录
     */
    private void login() {
        CircularFlex.hide(circularFlexActivityMbLogin)
                .endRadius(new BigDecimal(circularFlexActivityLogin.getHeight() / 2).floatValue())
                .go(() -> {
                    circularFlexActivityLogin.setVisibility(View.VISIBLE);
                    circularFlexActivityLogin.postDelayed(() -> CircularFlex.fullActivity(CircularFlexActivity.this, circularFlexActivityLogin)
                            .go(() -> IntentUtils.jumpNoBundle(CircularFlexActivity.this, CircularFlexContentActivity.class)), 3000);
                });
    }

    /**
     * ImageView
     *
     * @param view View
     */
    private void imageView(@NonNull View view) {
        view.animate().rotationBy(90);
        // triggerView为中心收、展内容布局
        if (contentVisible) {
            CircularFlex.hide(circularFlexActivityTvContent).duration(5000L).triggerView(circularFlexActivityIv).go();
        } else {
            CircularFlex.show(circularFlexActivityTvContent)
                    .triggerPoint(new Point(circularFlexActivityTvContent.getWidth(), 0))
                    .duration(5000L)
                    .go();
        }
        contentVisible = !contentVisible;
    }
}
