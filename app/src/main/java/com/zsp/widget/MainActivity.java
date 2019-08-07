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
import example.animation.LoginActivity;
import example.contract.ContactActivity;
import example.dialog.DialogActivity;
import example.floatingactionbutton.CounterActivity;
import example.location.LocationActivity;
import example.picture.PictureActivity;
import example.progressbar.MultiProgressBarActivity;
import example.pudding.PuddingActivity;
import example.screen.ScreenActivity;
import example.searchbox.SearchBoxActivity;
import example.sms.SmsActivity;
import example.spannablestringbuilder.SpannableStringBuilderActivity;
import example.spruce.SpruceActivity;
import example.telephony.TelephonyActivity;
import example.textview.FillActivity;
import example.textview.TimerActivity;
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
            R.id.mainActivityMbCounter,
            R.id.mainActivityMbSms,
            R.id.mainActivityMbContact,
            R.id.mainActivityMbTelephony,
            R.id.mainActivityMbMultiProgressBar})
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
            // 多样进度条
            case R.id.mainActivityMbMultiProgressBar:
                IntentUtils.jumpNoBundle(this, MultiProgressBarActivity.class);
                break;
            default:
                break;
        }
    }
}
