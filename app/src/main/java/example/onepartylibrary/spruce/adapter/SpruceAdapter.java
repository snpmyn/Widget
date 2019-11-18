package example.onepartylibrary.spruce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.spruce.SpruceKit;
import com.zsp.widget.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.onepartylibrary.spruce.bean.SpruceBean;

/**
 * Created on 2019/6/26.
 *
 * @author 郑少鹏
 * @desc spruce适配器
 */
public class SpruceAdapter extends RecyclerView.Adapter<SpruceAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    private List<SpruceBean> spruceBeans;
    private SpruceKit spruceKit;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.spruceItemRl)
        RelativeLayout spruceItemRl;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            spruceItemRl = itemView.findViewById(R.id.spruceItemRl);
            spruceItemRl.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            spruceKit.defaultSort(recyclerView, 100L, 800L);
        }
    }

    public SpruceAdapter(RecyclerView recyclerView, List<SpruceBean> spruceBeans) {
        this.recyclerView = recyclerView;
        this.spruceBeans = spruceBeans;
        this.spruceKit = new SpruceKit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.spruce_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return spruceBeans.size();
    }
}
