package example.location;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.location.Location;
import com.zsp.library.location.LocationKit;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 定位页
 * @author: 郑少鹏
 * @date: 2019/7/1 10:54
 */
public class LocationActivity extends AppCompatActivity {
    @BindView(R.id.locationActivityTvResult)
    TextView locationActivityTvResult;
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;
    /**
     * 定位
     */
    private Location location;
    private LocationKit locationKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.removeLocationUpdatesListener();
    }

    private void initConfiguration() {
        // SoulPermissionUtils
        soulPermissionUtils = new SoulPermissionUtils();
        // 定位
        location = new Location(this);
        locationKit = new LocationKit(this);
    }

    private void startLogic() {
        soulPermissionUtils.checkAndRequestPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                soulPermissionUtils,
                false,
                new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        locationKit.modeCheck();
                    }

                    @Override
                    public void onPermissionDenied(boolean b, String s) {
                        if (b) {
                            ToastUtils.shortShow(LocationActivity.this, s);
                        } else {
                            finish();
                        }
                    }
                });
    }

    private void setListener() {

    }

    @OnClick(R.id.locationActivityMbLocationResult)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.locationActivityMbLocationResult) {
            String result = "经度：" + locationKit.longitude + "\n" +
                    "纬度：" + locationKit.latitude + "\n" +
                    "地址：" + locationKit.address();
            locationActivityTvResult.setText(result);
        }
    }
}
