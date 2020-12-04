package example.onepartylibrary.focusresize;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.focusresize.FocusResizeScrollListener;
import com.zsp.library.recyclerview.configure.RecyclerViewConfigure;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.onepartylibrary.focusresize.adapter.FocusResizeCustomAdapter;
import example.onepartylibrary.focusresize.adapter.FocusResizeDefaultAdapter;
import example.onepartylibrary.focusresize.bean.FocusResizeBean;

/**
 * @decs: FocusResize页
 * @author: 郑少鹏
 * @date: 2019/9/5 17:20
 */
public class FocusResizeActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.focusResizeActivityRv)
    RecyclerView focusResizeActivityRv;
    /**
     * 聚焦调整自定适配器
     */
    private FocusResizeCustomAdapter focusResizeCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_resize);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
        setListener();
    }

    private void initConfiguration() {
        // 控件
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(this, focusResizeActivityRv);
        recyclerViewConfigure.linearVerticalLayout(true, 36, false, false, false);
        // 聚焦调整默适配器
        FocusResizeDefaultAdapter focusResizeDefaultAdapter = new FocusResizeDefaultAdapter(this, (int) getResources().getDimension(R.dimen.dp_100));
        focusResizeDefaultAdapter.setDefaultData(addItems());
        // 聚焦调整自定适配器
        focusResizeCustomAdapter = new FocusResizeCustomAdapter(this, (int) getResources().getDimension(R.dimen.dp_100));
        focusResizeCustomAdapter.setCustomData(addItems());
    }

    private void startLogic() {
        focusResizeActivityRv.setAdapter(focusResizeCustomAdapter);
    }

    private void setListener() {
        focusResizeActivityRv.addOnScrollListener(new FocusResizeScrollListener<>(focusResizeCustomAdapter, (LinearLayoutManager) focusResizeActivityRv.getLayoutManager()));
    }

    /**
     * 添条目
     *
     * @return 条目集
     */
    private List<FocusResizeBean> addItems() {
        List<FocusResizeBean> items = new ArrayList<>();
        items.add(new FocusResizeBean("Possibility", "The Hill", R.drawable.image_one));
        items.add(new FocusResizeBean("Finishing", "The Grid", R.drawable.image_two));
        items.add(new FocusResizeBean("Craftsmanship", "Metropolitan Center", R.drawable.image_three));
        items.add(new FocusResizeBean("Opportunity", "The Hill", R.drawable.image_four));
        items.add(new FocusResizeBean("Starting Over", "The Grid", R.drawable.image_five));
        items.add(new FocusResizeBean("Identity", "Metropolitan Center", R.drawable.image_six));
        return items;
    }
}
