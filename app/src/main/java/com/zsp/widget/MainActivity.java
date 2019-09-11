package com.zsp.widget;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.zsp.library.guide.GuideView;
import com.zsp.utilone.intent.IntentUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.animation.AnimationActivity;
import example.banner.BannerActivity;
import example.card.CardActivity;
import example.chart.RadarChartActivity;
import example.contract.ContactActivity;
import example.dialog.DialogActivity;
import example.floatingactionbutton.CounterActivity;
import example.focusresize.FocusResizeActivity;
import example.layout.LayoutActivity;
import example.location.LocationActivity;
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
    /**
     * 引导视图
     */
    private GuideView pictureGuideView;
    private GuideView animationGuideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
    }

    private void initConfiguration() {
        // SoulPermissionUtils
        soulPermissionUtils = new SoulPermissionUtils();
        // 图片引导视图
        pictureGuideView = GuideView.Builder
                // 初始（必调）
                .newInstance(this)
                // 目标图（必调）
                .setTargetView(mainActivityMbPicture)
                // 引导图（ImageView或TextView）（必调）
                .setCustomGuideView(GuideView.guideTextView(this, R.string.picture, ContextCompat.getColor(this, R.color.background)))
                // 引导图状（圆形、椭圆、矩形，矩形可圆角矩形）
                .setShape(GuideView.MyShape.CIRCULAR)
                // 引导图相对目标图位（八种，不设默屏左上角）
                .setDirection(GuideView.Direction.RIGHT_TOP)
                // 圆形引导图透明区半径，矩形引导图圆角大小
                .setRadius(170)
                // 圆心（默目标图中心）
                .setCenter(300, 300)
                // 偏移（微调引导图位）
                .setOffset(200, 60)
                // 背景色（默透明）
                .setBgColor(ContextCompat.getColor(this, R.color.blackCC))
                // 点
                .setOnClickListener(() -> {
                    pictureGuideView.hide();
                    animationGuideView.show();
                })
                // 显一次
                .showOnce()
                // Builder模式（返引导图实例）（必调）
                .build();
        // 动画引导视图
        animationGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(mainActivityMbAnimation)
                .setCustomGuideView(GuideView.guideTextView(this, R.string.animation, ContextCompat.getColor(this, R.color.background)))
                .setShape(GuideView.MyShape.CIRCULAR)
                .setDirection(GuideView.Direction.LEFT_TOP)
                .setRadius(170)
                .setBgColor(ContextCompat.getColor(this, R.color.blackCC))
                .setOnClickListener(() -> {
                    animationGuideView.hide();
                    checkAndRequestPermission();
                })
                .showOnce()
                .build();
    }

    private void startLogic() {
        if (pictureGuideView.hasShown()) {
            checkAndRequestPermission();
        } else {
            pictureGuideView.show();
        }
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
            R.id.mainActivityMbFocusResize})
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
            default:
                break;
        }
    }
}
