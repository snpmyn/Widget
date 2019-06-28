package com.zsp.widget;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.dialog.DialogOneActivity;
import example.LoginOneActivity;
import example.PictureActivity;
import example.SearchBoxOneActivity;
import example.SearchBoxTwoActivity;
import example.SpannableStringActivity;
import example.TimerActivity;
import example.dialog.DialogTwoActivity;
import example.screen.ScreenActivity;
import example.spruce.SpruceActivity;

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
            R.id.mainActivityMbSearchBoxOne,
            R.id.mainActivityMbSearchBoxTwo,
            R.id.mainActivityMbTimer,
            R.id.mainActivityMbLoginOne,
            R.id.mainActivityMbDialogOne,
            R.id.mainActivityMbDialogTwo,
            R.id.mainActivityMbSpannableString,
            R.id.mainActivityMbScreen,
            R.id.mainActivityMbSpruce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // Switch
            case R.id.mainActivityMbPicture:
                IntentUtils.jumpNoBundle(this, PictureActivity.class);
                break;
            // 搜索框一
            case R.id.mainActivityMbSearchBoxOne:
                IntentUtils.jumpNoBundle(this, SearchBoxOneActivity.class);
                break;
            // 搜索框二
            case R.id.mainActivityMbSearchBoxTwo:
                IntentUtils.jumpNoBundle(this, SearchBoxTwoActivity.class);
                break;
            // 计时器
            case R.id.mainActivityMbTimer:
                IntentUtils.jumpNoBundle(this, TimerActivity.class);
                break;
            // 登录一
            case R.id.mainActivityMbLoginOne:
                IntentUtils.jumpNoBundle(this, LoginOneActivity.class);
                break;
            // 对话框一
            case R.id.mainActivityMbDialogOne:
                IntentUtils.jumpNoBundle(this, DialogOneActivity.class);
                break;
            // 对话框二
            case R.id.mainActivityMbDialogTwo:
                IntentUtils.jumpNoBundle(this, DialogTwoActivity.class);
                break;
            // SpannableString
            case R.id.mainActivityMbSpannableString:
                IntentUtils.jumpNoBundle(this, SpannableStringActivity.class);
                break;
            // 筛选
            case R.id.mainActivityMbScreen:
                IntentUtils.jumpNoBundle(this, ScreenActivity.class);
                break;
            // spruce
            case R.id.mainActivityMbSpruce:
                IntentUtils.jumpNoBundle(this, SpruceActivity.class);
                break;
            default:
                break;
        }
    }
}
