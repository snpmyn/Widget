package example.onepartylibrary;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.animation.AnimationActivity;
import example.banner.BannerActivity;
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
import example.picture.PictureActivity;
import example.progressbar.ProgressBarActivity;
import example.pudding.PuddingActivity;
import example.screen.ScreenActivity;
import example.searchbox.SearchBoxActivity;
import example.sidedrag.SideDragActivity;
import example.sms.SmsActivity;
import example.spannablestringbuilder.SpannableStringBuilderActivity;
import example.spruce.SpruceActivity;
import example.tagview.TagViewActivity;
import example.telephony.TelephonyActivity;
import example.textview.TextViewActivity;
import example.tipview.TipViewActivity;
import example.toolbar.ToolbarActivity;
import example.voice.VoiceActivity;

/**
 * @decs: 一方库示例页
 * @author: 郑少鹏
 * @date: 2019/11/18 11:24
 */
public class OnePartyLibraryExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_party_library_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.onePartyLibraryExampleActivityMbPicture,
            R.id.onePartyLibraryExampleActivityMbSearchBox,
            R.id.onePartyLibraryExampleActivityMbTextView,
            R.id.onePartyLibraryExampleActivityMbAnimation,
            R.id.onePartyLibraryExampleActivityMbDialog,
            R.id.onePartyLibraryExampleActivityMbSpannableStringBuilder,
            R.id.onePartyLibraryExampleActivityMbScreen,
            R.id.onePartyLibraryExampleActivityMbSpruce,
            R.id.onePartyLibraryExampleActivityMbVoice,
            R.id.onePartyLibraryExampleActivityMbLocation,
            R.id.onePartyLibraryExampleActivityMbPudding,
            R.id.onePartyLibraryExampleActivityMbCounter,
            R.id.onePartyLibraryExampleActivityMbSms,
            R.id.onePartyLibraryExampleActivityMbContact,
            R.id.onePartyLibraryExampleActivityMbTelephony,
            R.id.onePartyLibraryExampleActivityMbProgressBar,
            R.id.onePartyLibraryExampleActivityMbLayout,
            R.id.onePartyLibraryExampleActivityMbSideDrag,
            R.id.onePartyLibraryExampleActivityMbBanner,
            R.id.onePartyLibraryExampleActivityMbRadarChart,
            R.id.onePartyLibraryExampleActivityMbRadarCard,
            R.id.onePartyLibraryExampleActivityMbFocusResize,
            R.id.onePartyLibraryExampleActivityMbGuide,
            R.id.onePartyLibraryExampleActivityMbToolbar,
            R.id.onePartyLibraryExampleActivityMbConnection,
            R.id.onePartyLibraryExampleActivityMbClock,
            R.id.onePartyLibraryExampleActivityMbTipView,
            R.id.onePartyLibraryExampleActivityMbTagView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 图片
            case R.id.onePartyLibraryExampleActivityMbPicture:
                IntentUtils.jumpNoBundle(this, PictureActivity.class);
                break;
            // 搜索框
            case R.id.onePartyLibraryExampleActivityMbSearchBox:
                IntentUtils.jumpNoBundle(this, SearchBoxActivity.class);
                break;
            // TextView
            case R.id.onePartyLibraryExampleActivityMbTextView:
                IntentUtils.jumpNoBundle(this, TextViewActivity.class);
                break;
            // 动画
            case R.id.onePartyLibraryExampleActivityMbAnimation:
                IntentUtils.jumpNoBundle(this, AnimationActivity.class);
                break;
            // 对话框
            case R.id.onePartyLibraryExampleActivityMbDialog:
                IntentUtils.jumpNoBundle(this, DialogActivity.class);
                break;
            // SpannableStringBuilder
            case R.id.onePartyLibraryExampleActivityMbSpannableStringBuilder:
                IntentUtils.jumpNoBundle(this, SpannableStringBuilderActivity.class);
                break;
            // 筛选
            case R.id.onePartyLibraryExampleActivityMbScreen:
                IntentUtils.jumpNoBundle(this, ScreenActivity.class);
                break;
            // spruce
            case R.id.onePartyLibraryExampleActivityMbSpruce:
                IntentUtils.jumpNoBundle(this, SpruceActivity.class);
                break;
            // 语音
            case R.id.onePartyLibraryExampleActivityMbVoice:
                IntentUtils.jumpNoBundle(this, VoiceActivity.class);
                break;
            // 定位
            case R.id.onePartyLibraryExampleActivityMbLocation:
                IntentUtils.jumpNoBundle(this, LocationActivity.class);
                break;
            // pudding
            case R.id.onePartyLibraryExampleActivityMbPudding:
                IntentUtils.jumpNoBundle(this, PuddingActivity.class);
                break;
            // 计数
            case R.id.onePartyLibraryExampleActivityMbCounter:
                IntentUtils.jumpNoBundle(this, CounterActivity.class);
                break;
            // 短信
            case R.id.onePartyLibraryExampleActivityMbSms:
                IntentUtils.jumpNoBundle(this, SmsActivity.class);
                break;
            // 联系人
            case R.id.onePartyLibraryExampleActivityMbContact:
                IntentUtils.jumpNoBundle(this, ContactActivity.class);
                break;
            // 电话
            case R.id.onePartyLibraryExampleActivityMbTelephony:
                IntentUtils.jumpNoBundle(this, TelephonyActivity.class);
                break;
            // 进度条
            case R.id.onePartyLibraryExampleActivityMbProgressBar:
                IntentUtils.jumpNoBundle(this, ProgressBarActivity.class);
                break;
            // 布局
            case R.id.onePartyLibraryExampleActivityMbLayout:
                IntentUtils.jumpNoBundle(this, LayoutActivity.class);
                break;
            // 侧拖
            case R.id.onePartyLibraryExampleActivityMbSideDrag:
                IntentUtils.jumpNoBundle(this, SideDragActivity.class);
                break;
            // 轮播
            case R.id.onePartyLibraryExampleActivityMbBanner:
                IntentUtils.jumpNoBundle(this, BannerActivity.class);
                break;
            // 雷达图
            case R.id.onePartyLibraryExampleActivityMbRadarChart:
                IntentUtils.jumpNoBundle(this, RadarChartActivity.class);
                break;
            // 卡片
            case R.id.onePartyLibraryExampleActivityMbRadarCard:
                IntentUtils.jumpNoBundle(this, CardActivity.class);
                break;
            // 聚焦调整
            case R.id.onePartyLibraryExampleActivityMbFocusResize:
                IntentUtils.jumpNoBundle(this, FocusResizeActivity.class);
                break;
            // 引导
            case R.id.onePartyLibraryExampleActivityMbGuide:
                IntentUtils.jumpNoBundle(this, GuideActivity.class);
                break;
            // 工具栏
            case R.id.onePartyLibraryExampleActivityMbToolbar:
                IntentUtils.jumpNoBundle(this, ToolbarActivity.class);
                break;
            // 连接
            case R.id.onePartyLibraryExampleActivityMbConnection:
                IntentUtils.jumpNoBundle(this, ConnectionActivity.class);
                break;
            // 时钟
            case R.id.onePartyLibraryExampleActivityMbClock:
                IntentUtils.jumpNoBundle(this, ClockActivity.class);
                break;
            // 提示视图
            case R.id.onePartyLibraryExampleActivityMbTipView:
                IntentUtils.jumpNoBundle(this, TipViewActivity.class);
                break;
            // 标签视图
            case R.id.onePartyLibraryExampleActivityMbTagView:
                IntentUtils.jumpNoBundle(this, TagViewActivity.class);
                break;
            default:
                break;
        }
    }
}
