package example.animation.reveal;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zsp.library.animation.reveal.Reveal;
import com.zsp.library.layout.circularreveal.layout.RevealFrameLayout;
import com.zsp.utilone.snackbar.SnackbarUtils;
import com.zsp.utilone.view.ViewUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 揭示页
 * @author: 郑少鹏
 * @date: 2019/8/27 11:15
 */
public class RevealActivity extends AppCompatActivity {
    @BindView(R.id.revealActivityFab)
    FloatingActionButton revealActivityFab;
    @BindView(R.id.revealActivityRfl)
    RevealFrameLayout revealActivityRfl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.revealActivityFab, R.id.revealActivityTvPackUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 揭示
            case R.id.revealActivityFab:
                reveal(view);
                break;
            // 收起
            case R.id.revealActivityTvPackUp:
                packUp(view);
                break;
            default:
                break;
        }
    }

    /**
     * 揭示
     *
     * @param view 视图
     */
    private void reveal(View view) {
        Reveal.reveal(revealActivityRfl)
                .from(revealActivityFab)
                .withCurvedTranslation()
                .withChildsAnimation()
                .withDelayBetweenChildAnimation(1000)
                .withChildAnimationDuration(2000)
                .withTranslateDuration(2000)
                .withRevealDuration(2000)
                .withEndAction(() -> {
                    ViewUtils.hideView(revealActivityFab, View.GONE);
                    SnackbarUtils.snackbarCreateByCharSequence(view, "揭示", false);
                }).start();
    }

    /**
     * 收起
     *
     * @param view 视图
     */
    private void packUp(View view) {
        Reveal.unreveal(revealActivityRfl)
                .to(revealActivityFab)
                .withCurvedTranslation()
                .withUnrevealDuration(2000)
                .withTranslateDuration(2000)
                .withEndAction(() -> {
                    ViewUtils.showView(revealActivityFab);
                    SnackbarUtils.snackbarCreateByCharSequence(view, "收起", false);
                }).start();
    }
}
