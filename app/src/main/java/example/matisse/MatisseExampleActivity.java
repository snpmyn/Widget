package example.matisse;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhihu.matisse.Matisse;
import com.zsp.matisse.kit.MatisseKit;
import com.zsp.utilone.glide.util.GlideUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: Matisse示例页
 * @author: 郑少鹏
 * @date: 2019/10/28 10:01
 */
public class MatisseExampleActivity extends AppCompatActivity {
    @BindView(R.id.matisseExampleActivityIv)
    ImageView matisseExampleActivityIv;
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_example);
        ButterKnife.bind(this);
        initConfiguration();
    }

    private void initConfiguration() {
        soulPermissionUtils = new SoulPermissionUtils();
    }

    @OnClick(R.id.matisseExampleActivityMb)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.matisseExampleActivityMb) {
            select();
        }
    }

    /**
     * 选择
     */
    private void select() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.CAMERA, soulPermissionUtils,
                true, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        MatisseKit.matisseOperateInActivity(MatisseExampleActivity.this, 1, getString(R.string.fileProviderAuthorities));
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(MatisseExampleActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MatisseKit.REQUEST_CODE) {
                assert data != null;
                List<String> list = new ArrayList<>(Matisse.obtainPathResult(data));
                GlideUtils.loadByStringPlaceHolderColor(this, list.get(0), R.color.transparent, matisseExampleActivityIv);
            }
        }
    }
}
