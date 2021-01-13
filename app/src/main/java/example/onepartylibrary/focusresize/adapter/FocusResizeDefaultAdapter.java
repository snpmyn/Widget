package example.onepartylibrary.focusresize.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.focusresize.BaseFocusResizeAdapter;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.onepartylibrary.focusresize.bean.FocusResizeBean;

/**
 * Created on 2019/9/5.
 *
 * @author 郑少鹏
 * @desc 聚焦调整默适配器
 */
public class FocusResizeDefaultAdapter extends BaseFocusResizeAdapter<RecyclerView.ViewHolder> {
    /**
     * 数据
     */
    private final List<FocusResizeBean> focusResizeBeans;

    /**
     * constructor
     *
     * @param context 上下文
     * @param height  高
     */
    public FocusResizeDefaultAdapter(Context context, int height) {
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
    public RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.focus_resize_item, parent, false);
        return new DefaultCustomViewHolder(v);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        FocusResizeBean focusResizeBean = focusResizeBeans.get(position);
        fill((DefaultCustomViewHolder) holder, focusResizeBean);
    }

    private void fill(@NonNull DefaultCustomViewHolder holder, @NonNull FocusResizeBean focusResizeBean) {
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

    static class DefaultCustomViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.focusResizeItemIv)
        ImageView focusResizeItemIv;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.focusResizeItemTvTitle)
        TextView focusResizeItemTvTitle;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.focusResizeItemTvSubtitle)
        TextView focusResizeItemTvSubtitle;

        DefaultCustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
