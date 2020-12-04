package example.onepartylibrary.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 引导页
 * @author: 郑少鹏
 * @date: 2019/9/24 11:27
 */
public class GuideActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.guideActivityMbGuideOne, R.id.guideActivityMbGuideTwo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 引导一
            case R.id.guideActivityMbGuideOne:
                IntentUtils.jumpNoBundle(this, GuideOneActivity.class);
                break;
            // 引导二
            case R.id.guideActivityMbGuideTwo:
                IntentUtils.jumpNoBundle(this, GuideTwoActivity.class);
                break;
            default:
                break;
        }
    }
}
