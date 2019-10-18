package example.layout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.layout.progress.ProgressLayout;
import com.zsp.library.layout.progress.ProgressLayoutListener;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 进度布局页
 * @author: 郑少鹏
 * @date: 2019/10/18 10:28
 */
public class ProgressLayoutActivity extends AppCompatActivity {
    @BindView(R.id.progressLayoutActivityPl)
    ProgressLayout progressLayoutActivityPl;
    @BindView(R.id.progressLayoutActivityTv)
    TextView progressLayoutActivityTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_layout);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
        setListener();
    }

    private void initConfiguration() {
        progressLayoutActivityPl.setMaxProgress(100);
        progressLayoutActivityPl.setCurrentProgress(20);
        progressLayoutActivityPl.setAutoProgress(true);
    }

    private void startLogic() {
        progressLayoutActivityPl.start();
    }

    private void setListener() {
        progressLayoutActivityPl.setProgressLayoutListener(new ProgressLayoutListener() {
            @Override
            public void onProgressCompleted() {
                ToastUtils.shortShow(ProgressLayoutActivity.this, getString(R.string.complete));
            }

            @Override
            public void onProgressChanged(int seconds) {
                progressLayoutActivityTv.setText(String.format(getString(R.string.second), seconds));
            }
        });
    }

    @OnClick({R.id.progressLayoutActivityMbStart, R.id.progressLayoutActivityMbStop, R.id.progressLayoutActivityMbCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 开始
            case R.id.progressLayoutActivityMbStart:
                progressLayoutActivityPl.start();
                break;
            // 停止
            case R.id.progressLayoutActivityMbStop:
                progressLayoutActivityPl.stop();
                break;
            // 取消
            case R.id.progressLayoutActivityMbCancel:
                progressLayoutActivityPl.cancel();
                break;
            default:
                break;
        }
    }
}
