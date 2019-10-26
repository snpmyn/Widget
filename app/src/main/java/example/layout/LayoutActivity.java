package example.layout;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 布局页
 * @author: 郑少鹏
 * @date: 2019/8/20 18:01
 */
public class LayoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.voiceActivityMbPercentLayout,
            R.id.voiceActivityMbShadowLayout,
            R.id.voiceActivityMbRippleLayout,
            R.id.voiceActivityMbCircularRevealLayout,
            R.id.voiceActivityMbProgressLayout,
            R.id.voiceActivityMbCamberLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 百分比布局
            case R.id.voiceActivityMbPercentLayout:
                IntentUtils.jumpNoBundle(this, PercentLayoutActivity.class);
                break;
            // 阴影布局
            case R.id.voiceActivityMbShadowLayout:
                IntentUtils.jumpNoBundle(this, ShadowLayoutActivity.class);
                break;
            // 波纹布局
            case R.id.voiceActivityMbRippleLayout:
                IntentUtils.jumpNoBundle(this, RippleLayoutActivity.class);
                break;
            // 循环揭示布局
            case R.id.voiceActivityMbCircularRevealLayout:
                IntentUtils.jumpNoBundle(this, CircularRevealActivity.class);
                break;
            // 进度布局
            case R.id.voiceActivityMbProgressLayout:
                IntentUtils.jumpNoBundle(this, ProgressLayoutActivity.class);
                break;
            // 弧形布局页
            case R.id.voiceActivityMbCamberLayout:
                IntentUtils.jumpNoBundle(this, CamberLayoutActivity.class);
                break;
            default:
                break;
        }
    }
}
