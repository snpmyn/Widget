package example.onepartylibrary.connection;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.conection.ConnectionStatusView;
import com.zsp.library.conection.DefaultConnectionConnectionStatusView;
import com.zsp.library.conection.Status;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 连接页
 * @author: 郑少鹏
 * @date: 2019/10/18 17:34
 */
public class ConnectionActivity extends AppCompatActivity {
    @BindView(R.id.connectionActivityDccsv)
    DefaultConnectionConnectionStatusView connectionActivityDccsv;
    @BindView(R.id.connectionActivityCcsv)
    ConnectionStatusView connectionActivityCcsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        View.OnClickListener onCompleteClickListener = view -> ToastUtils.shortShow(ConnectionActivity.this, getString(R.string.complete));
        connectionActivityDccsv.setOnCompleteClickListener(onCompleteClickListener);
        connectionActivityCcsv.setOnCompleteClickListener(onCompleteClickListener);
        View.OnClickListener onErrorClickListener = view -> ToastUtils.shortShow(ConnectionActivity.this, getString(R.string.error));
        connectionActivityDccsv.setOnErrorClickListener(onErrorClickListener);
        connectionActivityCcsv.setOnErrorClickListener(onErrorClickListener);
        View.OnClickListener onLoadingClickListener = view -> ToastUtils.shortShow(ConnectionActivity.this, getString(R.string.connecting));
        connectionActivityDccsv.setOnLoadingClickListener(onLoadingClickListener);
        connectionActivityCcsv.setOnLoadingClickListener(onLoadingClickListener);
    }

    @OnClick({R.id.connectionActivityMbComplete,
            R.id.connectionActivityMbError,
            R.id.connectionActivityMbConnecting,
            R.id.connectionActivityMbIdle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 完成
            case R.id.connectionActivityMbComplete:
                connectionActivityDccsv.setStatus(Status.COMPLETE);
                connectionActivityCcsv.setStatus(Status.COMPLETE);
                break;
            // 错误
            case R.id.connectionActivityMbError:
                connectionActivityDccsv.setStatus(Status.ERROR);
                connectionActivityCcsv.setStatus(Status.ERROR);
                break;
            // 连接中
            case R.id.connectionActivityMbConnecting:
                connectionActivityDccsv.setStatus(Status.LOADING);
                connectionActivityCcsv.setStatus(Status.LOADING);
                break;
            // 空闲
            case R.id.connectionActivityMbIdle:
                connectionActivityDccsv.setStatus(Status.IDLE);
                connectionActivityCcsv.setStatus(Status.IDLE);
                break;
            default:
                break;
        }
    }
}
