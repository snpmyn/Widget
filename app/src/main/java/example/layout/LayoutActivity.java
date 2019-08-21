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

    @OnClick({R.id.voiceActivityMbPercentLayout, R.id.voiceActivityMbShadowLayout})
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
            default:
                break;
        }
    }
}
