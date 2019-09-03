package example.sidedrag;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.recyclerview.RecyclerViewConfigure;
import com.zsp.library.recyclerview.RecyclerViewDisplayKit;
import com.zsp.library.sidedrag.callback.SimpleItemTouchHelperCallback;
import com.zsp.library.sidedrag.listener.ItemDragListener;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.sidedrag.adapter.SideDragAdapter;

/**
 * @decs: 侧拖页
 * @author: 郑少鹏
 * @date: 2019/8/16 14:11
 */
public class SideDragActivity extends AppCompatActivity implements ItemDragListener {
    @BindView(R.id.sideDragActivityRv)
    RecyclerView sideDragActivityRv;
    /**
     * 侧拖适配器
     */
    private SideDragAdapter sideDragAdapter;
    /**
     * 数据
     */
    private List<String> list;
    /**
     * ItemTouchHelper
     */
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_drag);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
    }

    private void initConfiguration() {
        // RecyclerViewConfigure
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(this, sideDragActivityRv);
        recyclerViewConfigure.linearVerticalLayout(true, 36, false, false);
        // 侧拖适配器
        sideDragAdapter = new SideDragAdapter(this, this);
        // 数据
        list = new ArrayList<>();
        list.add("一");
        list.add("二");
        list.add("三");
        list.add("四");
        list.add("五");
        list.add("六");
        list.add("七");
        list.add("八");
        list.add("九");
        // ItemTouchHelper
        SimpleItemTouchHelperCallback simpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(this,
                sideDragAdapter, R.color.red, R.drawable.delete_24dp_background);
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(sideDragActivityRv);
    }

    private void startLogic() {
        sideDragAdapter.setSideDragData(list);
        RecyclerViewDisplayKit.display(sideDragActivityRv, sideDragAdapter);
    }

    /**
     * 拖拽
     *
     * @param viewHolder RecyclerView.ViewHolder
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
