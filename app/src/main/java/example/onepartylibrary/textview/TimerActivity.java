package example.onepartylibrary.textview;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.textview.TimerTextView;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @decs: 计时器页
 * @author: 郑少鹏
 * @date: 2019/6/18 11:11
 */
public class TimerActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.timerActivityTtv)
    TimerTextView timerActivityTtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
        timer();
    }

    private void timer() {
        timerActivityTtv
                .setNormalText("获取验证码")
                .setCountDownText("重新获取(", "s)")
                // 关页保倒计时否
                .setCloseKeepCountDown(false)
                // 倒计时期间点击事件响应否
                .setCountDownClickable(false)
                // 格式化时间否
                .setShowFormatTime(false)
                .setIntervalUnit(TimeUnit.SECONDS)
                .setOnCountDownStartListener(() -> ToastUtils.shortShow(this, "开始计时"))
                .setOnCountDownTickListener(untilFinished -> Timber.d("onTick: %s", untilFinished))
                .setOnCountDownFinishListener(() -> ToastUtils.shortShow(this, "计时结束"))
                .setOnClickListener(v -> {
                    ToastUtils.shortShow(this, "短信已发送");
                    timerActivityTtv.startCountDown(100);
                });
    }
}
