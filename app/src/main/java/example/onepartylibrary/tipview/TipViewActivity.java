package example.onepartylibrary.tipview;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.tipview.TipView;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zsp.library.tipview.TipView.LOCATION_TOP;

/**
 * @decs: 提示视图页
 * @author: 郑少鹏
 * @date: 2019/11/12 10:59
 */
public class TipViewActivity extends AppCompatActivity {
    @BindView(R.id.tipViewActivityLl)
    LinearLayout tipViewActivityLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_view);
        ButterKnife.bind(this);
        // 执行
        execute();
    }

    /**
     * 执行
     */
    private void execute() {
        // 提示视图
        TipView tipView = new TipView(this);
        tipView.setAnchoredViewId(R.id.tipViewActivityTvThree);
        tipView.setTitle("Dynamic Tool Tip Title");
        tipView.setMessage("Dynamic Tool Tip Message Body");
        tipView.setArrowPosition(TipView.LOCATION_TOP);
        // 布局参数
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 添
        tipViewActivityLl.addView(tipView, layoutParams);
    }
}
