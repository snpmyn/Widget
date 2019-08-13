package example.progressbar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 进度条页
 * @author: 郑少鹏
 * @date: 2019/8/12 16:54
 */
public class ProgressBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.progressBarActivityMbMultiProgressBar, R.id.progressBarActivityMbProgressWheel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 多样进度条
            case R.id.progressBarActivityMbMultiProgressBar:
                IntentUtils.jumpNoBundle(this, MultiProgressBarActivity.class);
                break;
            // 进度轮
            case R.id.progressBarActivityMbProgressWheel:
                IntentUtils.jumpNoBundle(this, ProgressWheelActivity.class);
                break;
            default:
                break;
        }
    }
}
