package example.onepartylibrary.focusresize.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
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
import example.onepartylibrary.focusresize.bean.FocusResizeBean;

/**
 * Created on 2019/9/5.
 *
 * @author 郑少鹏
 * @desc 聚焦调整自定适配器
 */
public class FocusResizeCustomAdapter extends BaseFocusResizeAdapter<RecyclerView.ViewHolder> {
    /**
     * 偏移量
     */
    private static final int OFFSET_TEXT_SIZE = 4;
    private static final float OFFSET_TEXT_ALPHA = 100.0F;
    /**
     * 透明度
     */
    private static final float ALPHA_SUBTITLE = 0.81F;
    private static final float ALPHA_SUBTITLE_HIDE = 0.0F;
    /**
     * 上下文
     */
    private final Context context;
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
    public FocusResizeCustomAdapter(Context context, int height) {
        super(context, height);
        this.context = context;
        focusResizeBeans = new ArrayList<>();
    }

    public void setCustomData(List<FocusResizeBean> focusResizeBeans) {
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
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        FocusResizeBean focusResizeBean = focusResizeBeans.get(position);
        fill((CustomViewHolder) holder, focusResizeBean);
    }

    private void fill(CustomViewHolder customViewHolder, FocusResizeBean focusResizeBean) {
        customViewHolder.focusResizeItemIv.setImageResource(focusResizeBean.getDrawable());
        customViewHolder.focusResizeItemTvTitle.setText(focusResizeBean.getTitle());
        customViewHolder.focusResizeItemTvSubtitle.setText(focusResizeBean.getSubTitle());
    }

    @Override
    public void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ((CustomViewHolder) viewHolder).focusResizeItemTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.min(((CustomViewHolder) viewHolder).focusResizeItemTvTitle.getTextSize() + (Integer.valueOf(dyAbs / OFFSET_TEXT_SIZE).floatValue()), context.getResources().getDimension(R.dimen.sp_32)));
        float alpha = dyAbs / OFFSET_TEXT_ALPHA;
        ((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.setAlpha(Math.min(((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.getAlpha() + alpha, ALPHA_SUBTITLE));
    }

    @Override
    public void onItemBigResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ((CustomViewHolder) viewHolder).focusResizeItemTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.sp_32));
        ((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.setAlpha(ALPHA_SUBTITLE);
    }

    @Override
    public void onItemSmallResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ((CustomViewHolder) viewHolder).focusResizeItemTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.sp_16));
        ((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.setAlpha(ALPHA_SUBTITLE_HIDE);
    }

    @Override
    public void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
        ((CustomViewHolder) viewHolder).focusResizeItemTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.max(((CustomViewHolder) viewHolder).focusResizeItemTvTitle.getTextSize() - (Integer.valueOf(dyAbs / OFFSET_TEXT_SIZE).floatValue()), context.getResources().getDimension(R.dimen.sp_16)));
        float alpha = dyAbs / OFFSET_TEXT_ALPHA;
        ((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.setAlpha(Math.max(((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.getAlpha() - alpha, ALPHA_SUBTITLE_HIDE));
    }

    @Override
    public void onItemInit(RecyclerView.ViewHolder viewHolder) {
        ((CustomViewHolder) viewHolder).focusResizeItemTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_32));
        ((CustomViewHolder) viewHolder).focusResizeItemTvSubtitle.setAlpha(ALPHA_SUBTITLE);
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.focusResizeItemIv)
        ImageView focusResizeItemIv;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.focusResizeItemTvTitle)
        TextView focusResizeItemTvTitle;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.focusResizeItemTvSubtitle)
        TextView focusResizeItemTvSubtitle;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

