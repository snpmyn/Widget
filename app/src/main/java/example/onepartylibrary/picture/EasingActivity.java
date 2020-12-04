package example.onepartylibrary.picture;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.picture.easing.generator.RandomTransitionGenerator;
import com.zsp.library.picture.easing.transition.Transition;
import com.zsp.library.picture.easing.view.EasingView;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 缓动页
 * @author: 郑少鹏
 * @date: 2019/10/26 17:10
 */
public class EasingActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.easingActivityEv)
    EasingView easingActivityEv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easing);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        easingActivityEv.setTransitionListener(new EasingView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                ToastUtils.shortShow(EasingActivity.this, "过渡开始");
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                ToastUtils.shortShow(EasingActivity.this, "过渡结束");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.easingActivityMbPause,
            R.id.easingActivityMbResume,
            R.id.easingActivityMbRestart,
            R.id.easingActivityMbChangeInterpolator})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 暂停
            case R.id.easingActivityMbPause:
                easingActivityEv.pause();
                break;
            // 恢复
            case R.id.easingActivityMbResume:
                easingActivityEv.resume();
                break;
            // 重启
            case R.id.easingActivityMbRestart:
                easingActivityEv.restart();
                break;
            // 改变内插器
            case R.id.easingActivityMbChangeInterpolator:
                RandomTransitionGenerator randomTransitionGenerator = new RandomTransitionGenerator(5000, new AccelerateDecelerateInterpolator());
                easingActivityEv.setTransitionGenerator(randomTransitionGenerator);
                break;
            default:
                break;
        }
    }
}
