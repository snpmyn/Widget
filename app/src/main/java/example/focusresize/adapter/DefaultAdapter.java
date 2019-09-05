package example.focusresize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.focusresize.BaseFocusResizeAdapter;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.focusresize.bean.FocusResizeBean;

/**
 * Created on 2019/9/5.
 *
 * @author 郑少鹏
 * @desc 默适配器
 */
public class DefaultAdapter extends BaseFocusResizeAdapter<RecyclerView.ViewHolder> {
    /**
     * 数据
     */
    private List<FocusResizeBean> focusResizeBeans;

    /**
     * constructor
     *
     * @param context 上下文
     * @param height  高
     */
    public DefaultAdapter(Context context, int height) {
        super(context, height);
        focusResizeBeans = new ArrayList<>();
    }

    public void setDefaultData(List<FocusResizeBean> focusResizeBeans) {
        this.focusResizeBeans.addAll(focusResizeBeans);
        notifyDataSetChanged();
    }

    @Override
    public int getFooterItemCount() {
        return focusResizeBeans.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.focus_resize_item, parent, false);
        return new DefaultCustomViewHolder(v);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        FocusResizeBean focusResizeBean = focusResizeBeans.get(position);
        fill((DefaultCustomViewHolder) holder, focusResizeBean);
    }

    private void fill(DefaultCustomViewHolder holder, FocusResizeBean focusResizeBean) {
        holder.focusResizeItemIv.setImageResource(focusResizeBean.getDrawable());
        holder.focusResizeItemTvTitle.setText(focusResizeBean.getTitle());
        holder.focusResizeItemTvSubtitle.setText(focusResizeBean.getSubTitle());
    }

    @Override
    public void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {

    }

    @Override
    public void onItemBigResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {

    }

    @Override
    public void onItemSmallResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {

    }

    @Override
    public void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {

    }

    @Override
    public void onItemInit(RecyclerView.ViewHolder viewHolder) {

    }

    class DefaultCustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.focusResizeItemIv)
        ImageView focusResizeItemIv;
        @BindView(R.id.focusResizeItemTvTitle)
        TextView focusResizeItemTvTitle;
        @BindView(R.id.focusResizeItemTvSubtitle)
        TextView focusResizeItemTvSubtitle;

        DefaultCustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
