package example.textview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: TextView页
 * @author: 郑少鹏
 * @date: 2019/8/12 16:35
 */
public class TextViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textViewActivityMbFill, R.id.textViewActivityMbTimer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 填
            case R.id.textViewActivityMbFill:
                IntentUtils.jumpNoBundle(this, FillActivity.class);
                break;
            // 计时器
            case R.id.textViewActivityMbTimer:
                IntentUtils.jumpNoBundle(this, TimerActivity.class);
                break;
            default:
                break;
        }
    }
}
