package example.animation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.animation.login.LoginActivity;

/**
 * @decs: 动画页
 * @author: 郑少鹏
 * @date: 2019/8/27 14:33
 */
public class AnimationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.loginActivityMbLogin, R.id.loginActivityMbReveal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 登录
            case R.id.loginActivityMbLogin:
                IntentUtils.jumpNoBundle(this, LoginActivity.class);
                break;
            // 揭示
            case R.id.loginActivityMbReveal:
                IntentUtils.jumpNoBundle(this, RevealActivity.class);
                break;
            default:
                break;
        }
    }
}
