package com.zsp.widget;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.animation.LoginActivity;
import example.dialog.DialogActivity;
import example.floatingactionbutton.CounterActivity;
import example.location.LocationActivity;
import example.picture.PictureActivity;
import example.pudding.PuddingActivity;
import example.screen.ScreenActivity;
import example.searchbox.SearchBoxActivity;
import example.spannablestringbuilder.SpannableStringBuilderActivity;
import example.spruce.SpruceActivity;
import example.textview.FillActivity;
import example.textview.TimerActivity;
import example.voice.VoiceActivity;

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
            R.id.mainActivityMbSearchBox,
            R.id.mainActivityMbTimer,
            R.id.mainActivityMbLogin,
            R.id.mainActivityMbDialog,
            R.id.mainActivityMbSpannableStringBuilder,
            R.id.mainActivityMbScreen,
            R.id.mainActivityMbSpruce,
            R.id.mainActivityMbVoice,
            R.id.mainActivityMbLocation,
            R.id.mainActivityMbPudding,
            R.id.mainActivityMbFill,
            R.id.mainActivityMbCounter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // Switch
            case R.id.mainActivityMbPicture:
                IntentUtils.jumpNoBundle(this, PictureActivity.class);
                break;
            // 搜索框
            case R.id.mainActivityMbSearchBox:
                IntentUtils.jumpNoBundle(this, SearchBoxActivity.class);
                break;
            // 计时器
            case R.id.mainActivityMbTimer:
                IntentUtils.jumpNoBundle(this, TimerActivity.class);
                break;
            // 登录一
            case R.id.mainActivityMbLogin:
                IntentUtils.jumpNoBundle(this, LoginActivity.class);
                break;
            // 对话框
            case R.id.mainActivityMbDialog:
                IntentUtils.jumpNoBundle(this, DialogActivity.class);
                break;
            // SpannableStringBuilder
            case R.id.mainActivityMbSpannableStringBuilder:
                IntentUtils.jumpNoBundle(this, SpannableStringBuilderActivity.class);
                break;
            // 筛选
            case R.id.mainActivityMbScreen:
                IntentUtils.jumpNoBundle(this, ScreenActivity.class);
                break;
            // spruce
            case R.id.mainActivityMbSpruce:
                IntentUtils.jumpNoBundle(this, SpruceActivity.class);
                break;
            // 语音
            case R.id.mainActivityMbVoice:
                IntentUtils.jumpNoBundle(this, VoiceActivity.class);
                break;
            // 定位
            case R.id.mainActivityMbLocation:
                IntentUtils.jumpNoBundle(this, LocationActivity.class);
                break;
            // pudding
            case R.id.mainActivityMbPudding:
                IntentUtils.jumpNoBundle(this, PuddingActivity.class);
                break;
            // 填
            case R.id.mainActivityMbFill:
                IntentUtils.jumpNoBundle(this, FillActivity.class);
                break;
            // 计数
            case R.id.mainActivityMbCounter:
                IntentUtils.jumpNoBundle(this, CounterActivity.class);
                break;
            default:
                break;
        }
    }
}
