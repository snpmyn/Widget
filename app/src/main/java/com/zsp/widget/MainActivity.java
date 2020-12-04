package com.zsp.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.onepartylibrary.OnePartyLibraryExampleActivity;
import example.tripartitelibrary.TripartiteLibraryExampleActivity;

/**
 * @decs: 主页
 * @author: 郑少鹏
 * @date: 2019/6/5 10:38
 */
public class MainActivity extends AppCompatActivity {
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
    }

    private void initConfiguration() {
        soulPermissionUtils = new SoulPermissionUtils();
    }

    private void startLogic() {
        checkAndRequestPermission();
    }

    private void checkAndRequestPermission() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, soulPermissionUtils,
                true, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {

                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(MainActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {

                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.mainActivityMbOnePartyLibraryExample, R.id.mainActivityMbTripartiteLibraryExample})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 一方库示例
            case R.id.mainActivityMbOnePartyLibraryExample:
                IntentUtils.jumpNoBundle(this, OnePartyLibraryExampleActivity.class);
                break;
            // 三方库示例
            case R.id.mainActivityMbTripartiteLibraryExample:
                IntentUtils.jumpNoBundle(this, TripartiteLibraryExampleActivity.class);
                break;
            default:
                break;
        }
    }
}
