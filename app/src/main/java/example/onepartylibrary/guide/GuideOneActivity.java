package example.onepartylibrary.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.guide.guideview.GuideView;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 引导一页
 * @author: 郑少鹏
 * @date: 2019/9/24 11:29
 */
public class GuideOneActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.guideOneActivityIvOne)
    ImageView guideOneActivityIvOne;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.guideOneActivityIvTwo)
    ImageView guideOneActivityIvTwo;
    /**
     * 引导视图
     */
    private GuideView guideViewOne;
    private GuideView guideViewTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_one);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
    }

    private void initConfiguration() {
        /*
          引导视图一
         */
        guideViewOne = new GuideView.Builder(this)
                // 目标图（必调）
                .setTargetView(guideOneActivityIvOne)
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
                    guideViewOne.hide();
                    guideViewTwo.show();
                })
                // 显一次
                .showOnce()
                // Builder模式（返引导图实例）（必调）
                .build();
        /*
          引导视图二
         */
        guideViewTwo = new GuideView.Builder(this)
                .setTargetView(guideOneActivityIvTwo)
                .setCustomGuideView(GuideView.guideTextView(this, R.string.animation, ContextCompat.getColor(this, R.color.background)))
                .setShape(GuideView.MyShape.CIRCULAR)
                .setDirection(GuideView.Direction.LEFT_TOP)
                .setRadius(170)
                .setBgColor(ContextCompat.getColor(this, R.color.blackCC))
                .setOnClickListener(() -> {
                    guideViewTwo.hide();
                    ToastUtils.shortShow(this, "引导结束");
                })
                .showOnce()
                .build();
    }

    private void startLogic() {
        if (guideViewOne.hasShown()) {
            ToastUtils.shortShow(this, "引导一仅显一次");
        } else {
            guideViewOne.show();
        }
    }
}
