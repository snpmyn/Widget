package example.onepartylibrary.animation.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.animation.login.two.TransitionView;
import com.zsp.utilone.intent.IntentUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 登录二页
 * @author: 郑少鹏
 * @date: 2019/7/31 10:04
 */
public class LoginTwoActivity extends AppCompatActivity implements TransitionView.OnAnimationEndListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.loginTwoActivityTv)
    TransitionView loginTwoActivityTv;
    /**
     * 成功
     */
    private boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_two);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        loginTwoActivityTv.setOnAnimationEndListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.loginTwoActivityMbLogin)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.loginTwoActivityMbLogin) {
            loginTwoActivityTv.startAnimation();
            login();
        }
    }

    private void login() {
        if (success) {
            loginTwoActivityTv.postDelayed(() -> loginTwoActivityTv.setResult(1), 2000);
        } else {
            loginTwoActivityTv.postDelayed(() -> loginTwoActivityTv.setResult(2), 2000);
        }
    }

    /**
     * 动画成功结束
     */
    @Override
    public void animationEndSuccess() {
        success = false;
        IntentUtils.jumpNoBundle(this, HomeActivity.class);
        finish();
    }

    /**
     * 动画失败结束
     */
    @Override
    public void animationEndError() {
        success = true;
        ToastUtils.shortShow(this, getString(R.string.loginFail));
    }
}
