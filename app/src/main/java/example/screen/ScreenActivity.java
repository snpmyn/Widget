package example.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.screen.ScreenHandleKit;
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
        bottomSheetDialog();
    }

    private void initConfiguration() {
        // ScreenHandleKit
        screenHandleKit = new ScreenHandleKit(this);
        screenHandleKit.setScreenHandleListener(new ScreenHandleKit.ScreenHandleListener() {
            /**
             * 点
             *
             * @param view           视图
             * @param classification 类别
             * @param condition      条件
             * @param selected       选否
             */
            @Override
            public void click(View view, String classification, String condition, boolean selected) {
                ToastUtils.shortShow(ScreenActivity.this, classification + "    " + condition + "    " + selected);
                screenActivityKit.screeningDistribution(classification, condition, selected);
            }

            /**
             * 重置
             */
            @Override
            public void resetting() {
                screenHandleKit.resetting();
                screenActivityKit.resetting();
            }

            /**
             * 确定
             */
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
        // ScreenActivityKit
        screenActivityKit = new ScreenActivityKit();
    }

    private void bottomSheetDialog() {
        // 性别
        screenHandleKit.packByStringConditions("性别", 3, true, "男", "女");
        // 年龄段
        screenHandleKit.packByStringConditions("年龄段", 3, true,
                "18岁以下", "18～40岁", "40～60岁", "60岁以上");
        // 消费周期
        screenHandleKit.packByStringConditions("消费周期", 3, false,
                "1个月", "3个月", "6个月");
        List<String> list = new ArrayList<>();
        list.add("1次");
        list.add("2次");
        list.add("3次");
        // 消费次数
        screenHandleKit.packByListConditions("消费次数", 3, true, list);
        // 支持单选后取消
        screenHandleKit.supportCancelAfterSingleSelect("性别", "年龄段");
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
