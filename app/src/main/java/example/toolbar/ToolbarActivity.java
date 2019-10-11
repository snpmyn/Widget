package example.toolbar;

import android.os.Bundle;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zsp.library.toolbar.Dip;
import com.zsp.library.toolbar.WaterfallToolbar;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 工具栏页
 * @author: 郑少鹏
 * @date: 2019/10/10 17:54
 */
public class ToolbarActivity extends AppCompatActivity {
    @BindView(R.id.toolbarActivityMt)
    Toolbar toolbarActivityMt;
    @BindView(R.id.toolbarActivityWt)
    WaterfallToolbar toolbarActivityWt;
    @BindView(R.id.toolbarActivitySv)
    ScrollView toolbarActivitySv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        ButterKnife.bind(this);
        stepUi();
        startLogic();
        setListener();
    }

    private void stepUi() {
        setSupportActionBar(toolbarActivityMt);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void startLogic() {
        toolbarActivityWt.setInitialElevation(new Dip(0).toPx());
        toolbarActivityWt.setFinalElevation(new Dip(20).toPx());
        toolbarActivityWt.setScrollFinalPosition(6);
        toolbarActivityWt.setScrollView(toolbarActivitySv);
    }

    private void setListener() {
        toolbarActivityMt.setNavigationOnClickListener(view -> finish());
    }
}
