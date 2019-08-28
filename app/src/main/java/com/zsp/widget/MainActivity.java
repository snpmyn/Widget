package com.zsp.widget;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.animation.AnimationActivity;
import example.banner.BannerActivity;
import example.chart.RadarChartActivity;
import example.contract.ContactActivity;
import example.dialog.DialogActivity;
import example.floatingactionbutton.CounterActivity;
import example.layout.LayoutActivity;
import example.location.LocationActivity;
import example.picture.PreviewExampleActivity;
import example.progressbar.ProgressBarActivity;
import example.pudding.PuddingActivity;
import example.screen.ScreenActivity;
import example.searchbox.SearchBoxActivity;
import example.sidedrag.SideDragActivity;
import example.sms.SmsActivity;
import example.spannablestringbuilder.SpannableStringBuilderActivity;
import example.spruce.SpruceActivity;
import example.telephony.TelephonyActivity;
import example.textview.TextViewActivity;
import example.voice.VoiceActivity;

/**
 * @decs: 主页
 * @author: 郑少鹏
 * @date: 2019/6/5 10:38
 */
public class MainActivity extends AppCompatActivity {
    private SoulPermissionUtils soulPermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initConfiguration();
        execute();
    }

    private void initConfiguration() {
        soulPermissionUtils = new SoulPermissionUtils();
    }

    private void execute() {
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

    @OnClick({R.id.mainActivityMbPreviewExample,
            R.id.mainActivityMbSearchBox,
            R.id.mainActivityMbTextView,
            R.id.mainActivityMbAnimation,
            R.id.mainActivityMbDialog,
            R.id.mainActivityMbSpannableStringBuilder,
            R.id.mainActivityMbScreen,
            R.id.mainActivityMbSpruce,
            R.id.mainActivityMbVoice,
            R.id.mainActivityMbLocation,
            R.id.mainActivityMbPudding,
            R.id.mainActivityMbCounter,
            R.id.mainActivityMbSms,
            R.id.mainActivityMbContact,
            R.id.mainActivityMbTelephony,
            R.id.mainActivityMbProgressBar,
            R.id.mainActivityMbLayout,
            R.id.mainActivityMbSideDrag,
            R.id.mainActivityMbBanner,
            R.id.mainActivityMbRadarChart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 预览示例
            case R.id.mainActivityMbPreviewExample:
                IntentUtils.jumpNoBundle(this, PreviewExampleActivity.class);
                break;
            // 搜索框
            case R.id.mainActivityMbSearchBox:
                IntentUtils.jumpNoBundle(this, SearchBoxActivity.class);
                break;
            // TextView
            case R.id.mainActivityMbTextView:
                IntentUtils.jumpNoBundle(this, TextViewActivity.class);
                break;
            // 动画
            case R.id.mainActivityMbAnimation:
                IntentUtils.jumpNoBundle(this, AnimationActivity.class);
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
            // 计数
            case R.id.mainActivityMbCounter:
                IntentUtils.jumpNoBundle(this, CounterActivity.class);
                break;
            // 短信
            case R.id.mainActivityMbSms:
                IntentUtils.jumpNoBundle(this, SmsActivity.class);
                break;
            // 联系人
            case R.id.mainActivityMbContact:
                IntentUtils.jumpNoBundle(this, ContactActivity.class);
                break;
            // 电话
            case R.id.mainActivityMbTelephony:
                IntentUtils.jumpNoBundle(this, TelephonyActivity.class);
                break;
            // 进度条
            case R.id.mainActivityMbProgressBar:
                IntentUtils.jumpNoBundle(this, ProgressBarActivity.class);
                break;
            // 布局
            case R.id.mainActivityMbLayout:
                IntentUtils.jumpNoBundle(this, LayoutActivity.class);
                break;
            // 侧拖
            case R.id.mainActivityMbSideDrag:
                IntentUtils.jumpNoBundle(this, SideDragActivity.class);
                break;
            // 轮播
            case R.id.mainActivityMbBanner:
                IntentUtils.jumpNoBundle(this, BannerActivity.class);
                break;
            // 雷达图
            case R.id.mainActivityMbRadarChart:
                IntentUtils.jumpNoBundle(this, RadarChartActivity.class);
                break;
            default:
                break;
        }
    }
}
