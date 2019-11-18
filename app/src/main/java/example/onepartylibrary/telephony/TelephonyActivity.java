package example.onepartylibrary.telephony;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.telephony.TelephonyService;
import com.zsp.utilone.service.ServiceUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import value.WidgetMagic;

/**
 * @decs: 电话页
 * @author: 郑少鹏
 * @date: 2019/7/28 10:18
 */
public class TelephonyActivity extends AppCompatActivity implements TelephonyService.TelephonyServiceListener {
    @BindView(R.id.telephonyActivityTv)
    TextView telephonyActivityTv;
    /**
     * 意图
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony);
        ButterKnife.bind(this);
        execute();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
        TelephonyService.destroy();
    }

    private void setListener() {
        TelephonyService.setTelephonyServiceListener(this);
    }

    private void execute() {
        if (null == intent) {
            intent = new Intent(this, TelephonyService.class);
        }
        if (!ServiceUtils.isServiceRunning(this, WidgetMagic.STRING_SERVICE_NAME, WidgetMagic.INT_THIRTY)) {
            startService(intent);
        }
    }

    /**
     * 响铃
     * <p>
     * 呼入调，不调{@link #incomingCall()}。
     *
     * @param startTime 开始时间
     */
    @Override
    public void callStateRinging(long startTime) {
        telephonyActivityTv.setText(String.format(getString(R.string.callStateRingingStartTime), startTime));
    }

    /**
     * 呼入
     * <p>
     * 呼入不调，调{@link #callStateRinging(long)}。
     */
    @Override
    public void incomingCall() {
        telephonyActivityTv.setText("呼入");
    }

    /**
     * 呼出
     *
     * @param startTime 开始时间
     */
    @Override
    public void callOut(long startTime) {
        telephonyActivityTv.setText(String.format(getString(R.string.callOutStartTime), startTime));
    }

    /**
     * 无通话
     */
    @Override
    public void noCalls() {
        telephonyActivityTv.setText("无通话");
    }

    /**
     * 挂断通话
     *
     * @param endTime  结束时间
     * @param duration 时长
     */
    @Override
    public void hangUpTheCall(long endTime, long duration) {
        telephonyActivityTv.setText(String.format(getString(R.string.hangUpTheCallEndTime), duration));
    }
}
