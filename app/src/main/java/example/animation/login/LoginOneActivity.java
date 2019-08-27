package example.animation.login;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.animation.login.one.ProgressButton;
import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 登录一页
 * @author: 郑少鹏
 * @date: 2019/6/18 11:35
 */
public class LoginOneActivity extends AppCompatActivity {
    @BindView(R.id.loginOneActivityPb)
    ProgressButton loginOneActivityPb;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            loginOneActivityPb.stopProcessAnimationAndThen(() -> IntentUtils.jumpNoBundle(LoginOneActivity.this, HomeActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_one);
        ButterKnife.bind(this);
        step();
    }

    private void step() {
        loginOneActivityPb.setText("登录");
        loginOneActivityPb.setTextColor(Color.WHITE);
        loginOneActivityPb.setProcessColor(Color.WHITE);
        loginOneActivityPb.setBgColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @OnClick(R.id.loginOneActivityPb)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.loginOneActivityPb) {
            loginOneActivityPb.startProcessAnimation();
            Message message = handler.obtainMessage();
            handler.sendMessageDelayed(message, 2000);
        }
    }
}
