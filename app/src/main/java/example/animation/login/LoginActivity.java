package example.animation.login;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 登录页
 * @author: 郑少鹏
 * @date: 2019/7/19 14:59
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.loginActivityMbLoginOne, R.id.loginActivityMbLoginTwo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 登录一
            case R.id.loginActivityMbLoginOne:
                IntentUtils.jumpNoBundle(this, LoginOneActivity.class);
                break;
            // 登录二
            case R.id.loginActivityMbLoginTwo:
                IntentUtils.jumpNoBundle(this, LoginTwoActivity.class);
                break;
            default:
                break;
        }
    }
}
