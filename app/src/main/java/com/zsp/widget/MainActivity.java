package com.zsp.widget;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.zsp.utilone.intent.IntentUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.animation.AnimationActivity;
import example.banner.BannerActivity;
import example.bgaqrcodeandroid.ScanCodeActivity;
import example.card.CardActivity;
import example.chart.RadarChartActivity;
import example.clock.ClockActivity;
import example.connection.ConnectionActivity;
import example.contact.ContactActivity;
import example.dialog.DialogActivity;
import example.floatingactionbutton.CounterActivity;
import example.focusresize.FocusResizeActivity;
import example.guide.GuideActivity;
import example.layout.LayoutActivity;
import example.location.LocationActivity;
import example.matisse.MatisseExampleActivity;
import example.picture.PictureActivity;
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
import example.toolbar.ToolbarActivity;
import example.voice.VoiceActivity;

/**
 * @decs: 主页
 * @author: 郑少鹏
 * @date: 2019/6/5 10:38
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainActivityMbPicture)
    MaterialButton mainActivityMbPicture;
    @BindView(R.id.mainActivityMbAnimation)
    MaterialButton mainActivityMbAnimation;
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

    @OnClick({R.id.mainActivityMbPicture,
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
            R.id.mainActivityMbRadarChart,
            R.id.mainActivityMbRadarCard,
            R.id.mainActivityMbFocusResize,
            R.id.mainActivityMbGuide,
            R.id.mainActivityMbToolbar,
            R.id.mainActivityMbConnection,
            R.id.mainActivityMbClock,
            R.id.mainActivityMbMatisse,
            R.id.mainActivityMbScanCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 图片
            case R.id.mainActivityMbPicture:
                IntentUtils.jumpNoBundle(this, PictureActivity.class);
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
            // 卡片
            case R.id.mainActivityMbRadarCard:
                IntentUtils.jumpNoBundle(this, CardActivity.class);
                break;
            // 聚焦调整
            case R.id.mainActivityMbFocusResize:
                IntentUtils.jumpNoBundle(this, FocusResizeActivity.class);
                break;
            // 引导
            case R.id.mainActivityMbGuide:
                IntentUtils.jumpNoBundle(this, GuideActivity.class);
                break;
            // 工具栏
            case R.id.mainActivityMbToolbar:
                IntentUtils.jumpNoBundle(this, ToolbarActivity.class);
                break;
            // 连接
            case R.id.mainActivityMbConnection:
                IntentUtils.jumpNoBundle(this, ConnectionActivity.class);
                break;
            // 时钟
            case R.id.mainActivityMbClock:
                IntentUtils.jumpNoBundle(this, ClockActivity.class);
                break;
            // Matisse示例
            case R.id.mainActivityMbMatisse:
                IntentUtils.jumpNoBundle(this, MatisseExampleActivity.class);
                break;
            // 扫码
            case R.id.mainActivityMbScanCode:
                IntentUtils.jumpNoBundle(this, ScanCodeActivity.class);
                break;
            default:
                break;
        }
    }
}
