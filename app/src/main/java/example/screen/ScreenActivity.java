package example.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.screen.kit.ScreenHandleKit;
import com.zsp.library.screen.listener.ScreenHandleListener;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.screen.kit.ScreenActivityKit;

/**
 * @decs: 筛选页
 * @author: 郑少鹏
 * @date: 2019/6/26 9:57
 */
public class ScreenActivity extends AppCompatActivity {
    @BindView(R.id.screenActivityTvResult)
    TextView screenActivityTvResult;
    /**
     * ScreenHandleKit
     */
    private ScreenHandleKit screenHandleKit;
    /**
     * ScreenActivityKit
     */
    private ScreenActivityKit screenActivityKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
        setListener();
    }

    private void initConfiguration() {
        // ScreenHandleKit
        screenHandleKit = new ScreenHandleKit(this);
        // ScreenActivityKit
        screenActivityKit = new ScreenActivityKit();
    }

    private void startLogic() {
        bottomSheetDialog();
    }

    private void setListener() {
        screenHandleKit.setScreenHandleListener(new ScreenHandleListener() {
            @Override
            public void click(View view, String classification, String condition, boolean selected) {
                ToastUtils.shortShow(ScreenActivity.this, classification + "    " + condition + "    " + selected);
                screenActivityKit.screeningDistribution(classification, condition, selected);
            }

            @Override
            public void reset() {
                // 存默选时如下顺序调避重置后无默选值
                screenActivityKit.resetting();
                screenHandleKit.reset();
            }

            @Override
            public void ensure() {
                screenHandleKit.dismiss();
                String result = screenActivityKit.sexResult + "    " +
                        screenActivityKit.ageGroupResultList + "    " +
                        screenActivityKit.consumptionCycleResultList + "    " +
                        screenActivityKit.numberOfConsumptionResult;
                screenActivityTvResult.setText(result);
            }
        });
    }

    /**
     * BottomSheetDialog
     */
    private void bottomSheetDialog() {
        // 性别
        screenHandleKit.packStringConditions("性别", 3, true, "男", "女");
        // 年龄段
        screenHandleKit.packStringConditions("年龄段", 3, true,
                "18岁以下", "18～40岁", "40～60岁", "60岁以上");
        // 消费周期
        screenHandleKit.packStringConditions("消费周期", 3, false,
                "1个月", "3个月", "6个月");
        List<String> list = new ArrayList<>();
        list.add("1次");
        list.add("2次");
        list.add("3次");
        // 消费次数
        screenHandleKit.packListConditions("消费次数", 3, true, list);
        // 单选后可反选
        screenHandleKit.canReverseSelectAfterSingleSelect("性别", "年龄段");
        // 默选
        screenHandleKit.defaultSelect("性别", "女");
        screenHandleKit.defaultSelect("消费周期", "1个月", "6个月");
        // 互斥
        screenHandleKit.mutuallyExclusive("1", "年龄段", "1", "消费次数");
        // 展开/折叠
        screenHandleKit.unfoldAndFold("年龄段", "18岁以下", "消费次数");
        // 关联
        screenHandleKit.associate();
    }

    @OnClick(R.id.screenActivityMbScreen)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.screenActivityMbScreen) {
            screenHandleKit.show();
        }
    }
}
