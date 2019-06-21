package com.zsp.widget;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.DialogOneActivity;
import example.LoginOneActivity;
import example.PictureActivity;
import example.SearchBoxOneActivity;
import example.SearchBoxTwoActivity;
import example.TimerActivity;

/**
 * @decs: 主页
 * @author: 郑少鹏
 * @date: 2019/6/5 10:38
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mainActivityMbPicture,
            R.id.mainActivitySearchBoxOne,
            R.id.mainActivitySearchBoxTwo,
            R.id.mainActivityTimer,
            R.id.mainActivityLoginOne,
            R.id.mainActivityDialogOne})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // Switch
            case R.id.mainActivityMbPicture:
                IntentUtils.jumpNoBundle(this, PictureActivity.class);
                break;
            // 搜索框一
            case R.id.mainActivitySearchBoxOne:
                IntentUtils.jumpNoBundle(this, SearchBoxOneActivity.class);
                break;
            // 搜索框二
            case R.id.mainActivitySearchBoxTwo:
                IntentUtils.jumpNoBundle(this, SearchBoxTwoActivity.class);
                break;
            // 计时器
            case R.id.mainActivityTimer:
                IntentUtils.jumpNoBundle(this, TimerActivity.class);
                break;
            // 登录一
            case R.id.mainActivityLoginOne:
                IntentUtils.jumpNoBundle(this, LoginOneActivity.class);
                break;
            // 对话框一
            case R.id.mainActivityDialogOne:
                IntentUtils.jumpNoBundle(this, DialogOneActivity.class);
                break;
            default:
                break;
        }
    }
}
