package example.onepartylibrary.spruce;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.recyclerview.configure.RecyclerViewConfigure;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.onepartylibrary.spruce.bean.SpruceBean;
import example.onepartylibrary.spruce.adapter.SpruceAdapter;

/**
 * @decs: spruce页
 * @author: 郑少鹏
 * @date: 2019/6/26 15:18
 */
public class SpruceActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spruceActivityRv)
    RecyclerView spruceActivityRv;
    /**
     * 数据
     */
    private List<SpruceBean> spruceBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spruce);
        ButterKnife.bind(this);
        initConfiguration();
        initData();
        startLogic();
    }

    private void initConfiguration() {
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(this, spruceActivityRv);
        recyclerViewConfigure.spruceKitConfigure(50L, 800L, false, com.willowtreeapps.spruce.sort.LinearSort.Direction.TOP_TO_BOTTOM);
        recyclerViewConfigure.linearVerticalLayout(false, 0, false, false, true);
    }

    private void initData() {
        // Mock data objects
        spruceBeanList = new ArrayList<>(10);
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
        spruceBeanList.add(new SpruceBean());
    }

    private void startLogic() {
        spruceActivityRv.setAdapter(new SpruceAdapter(spruceActivityRv, spruceBeanList));
    }
}
