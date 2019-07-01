package example.location;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.zsp.library.location.Location;
import com.zsp.library.location.LocationKit;
import com.zsp.utilone.permission.SoulPermissionUtils;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.removeLocationUpdatesListener();
    }

    private void initConfiguration() {
        // 权限
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                // if you want do noting or no need all the callbacks you may use SimplePermissionsAdapter instead
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {

                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                        SoulPermissionUtils soulPermissionUtils = new SoulPermissionUtils();
                        soulPermissionUtils.multiPermissionsDenied(LocationActivity.this, refusedPermissions);
                    }
                });
        // 定位
        location = new Location(this);
        locationKit = new LocationKit(this);
    }

    private void startLogic() {
        locationKit.modeCheck();
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
