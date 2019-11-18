package example.onepartylibrary.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.contact.bean.ContactBean;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2019/9/12.
 *
 * @author 郑少鹏
 * @desc 联系人适配器
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private List<ContactBean> contactBeans;
    private OnRecyclerViewItemClickListener onItemClickListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public ContactAdapter(Context context) {
        this.context = context;
        this.contactBeans = new ArrayList<>();
    }

    public void setContactData(List<ContactBean> contactBeans) {
        this.contactBeans = contactBeans;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        view.setOnClickListener(view1 -> onItemClickListener.onItemClick(view1, (ContactBean) view1.getTag()));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ContactBean contactBean = contactBeans.get(position);
        holder.itemView.setTag(contactBean);
        holder.contractItemTvName.setText(contactBean.getName());
        holder.contractItemTvCellPhoneNumber.setText(contactBean.getPhoneNumber());
        if (position == 0 || !contactBeans.get(position - 1).getIndex().equals(contactBean.getIndex())) {
            holder.contractItemTvStickyDecoration.setVisibility(View.VISIBLE);
            holder.contractItemTvStickyDecoration.setText(contactBean.getIndex());
        } else {
            holder.contractItemTvStickyDecoration.setVisibility(View.GONE);
        }
    }

    /**
     * 据当前位获分类首字母char ASCII值
     *
     * @param position 位
     * @return 分类首字母char ASCII值
     */
    public int getSectionForPosition(int position) {
        return contactBeans.get(position).getIndex().charAt(0);
    }

    /**
     * 据分类首字母char ASCII值获该首字母首现位
     *
     * @param section 分类首字母char ASCII值
     * @return 位
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = contactBeans.get(i).getIndex();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (contactBeans != null && contactBeans.size() > 0) {
            return contactBeans.size();
        }
        return 0;
    }

    public interface OnRecyclerViewItemClickListener {
        /**
         * 短点
         *
         * @param view        视图
         * @param contactBean 数据
         */
        void onItemClick(View view, ContactBean contactBean);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contractItemTvStickyDecoration)
        TextView contractItemTvStickyDecoration;
        @BindView(R.id.contractItemTvName)
        TextView contractItemTvName;
        @BindView(R.id.contractItemTvCellPhoneNumber)
        TextView contractItemTvCellPhoneNumber;

        ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
