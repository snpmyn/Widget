package example.chart;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.chart.RadarChart;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 雷达图页
 * @author: 郑少鹏
 * @date: 2019/8/21 15:41
 */
public class RadarChartActivity extends AppCompatActivity {
    @BindView(R.id.radarChartActivityRc)
    RadarChart radarChartActivityRc;
    /**
     * 标题
     */
    private List<String> titleList;
    /**
     * 进度
     */
    private List<Integer> originalProgressList;
    private List<Integer> temporaryProgressList;
    private List<Integer> finallyProgressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);
        ButterKnife.bind(this);
        initConfiguration();
        initData();
        execute();
    }

    private void initConfiguration() {
        // 标题
        titleList = new ArrayList<>(6);
        // 进度
        originalProgressList = new ArrayList<>(6);
        temporaryProgressList = new ArrayList<>(6);
        finallyProgressList = new ArrayList<>(6);
    }

    private void initData() {
        // 标题
        titleList.add("数学抽象");
        titleList.add("逻辑推理");
        titleList.add("数据分析");
        titleList.add("数学建模");
        titleList.add("直观想象");
        titleList.add("数学运算");
        // 原始进度
        originalProgressList.add(30);
        originalProgressList.add(30);
        originalProgressList.add(30);
        originalProgressList.add(30);
        originalProgressList.add(30);
        originalProgressList.add(30);
        // 临时进度
        temporaryProgressList.add(100);
        temporaryProgressList.add(20);
        temporaryProgressList.add(30);
        temporaryProgressList.add(40);
        temporaryProgressList.add(50);
        temporaryProgressList.add(60);
        // 最终进度
        finallyProgressList.add(30);
        finallyProgressList.add(30);
        finallyProgressList.add(30);
        finallyProgressList.add(30);
        finallyProgressList.add(30);
        finallyProgressList.add(30);
    }

    @OnClick(R.id.radarChartActivityRc)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.radarChartActivityRc) {
            finallyProgressList.clear();
            finallyProgressList.addAll(originalProgressList);
            expand(0);
        }
    }

    private void execute() {
        radarChartActivityRc.setTextArray(titleList);
        radarChartActivityRc.setOldProgressList(originalProgressList);
        radarChartActivityRc.setProgressList(finallyProgressList);
        radarChartActivityRc.doInvalidate();
    }

    /**
     * 展开
     * <p>
     * 各属性动画依次执行。
     *
     * @param position 位
     */
    private void expand(int position) {
        boolean flag = (finallyProgressList.size() == temporaryProgressList.size()) && (position >= 0) && (position < finallyProgressList.size());
        if (flag) {
            finallyProgressList.set(position, temporaryProgressList.get(position));
            radarChartActivityRc.setOldProgressList(originalProgressList);
            radarChartActivityRc.setProgressList(finallyProgressList);
            radarChartActivityRc.doInvalidate(position, integer -> {
                expand((integer + 1));
                return null;
            });
        }
    }
}
