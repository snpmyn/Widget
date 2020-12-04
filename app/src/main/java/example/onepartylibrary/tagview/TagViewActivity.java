package example.onepartylibrary.tagview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zsp.library.tagview.Tag;
import com.zsp.library.tagview.TagView;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import value.WidgetMagic;

/**
 * @decs: 标签视图页
 * @author: 郑少鹏
 * @date: 2019/11/12 15:02
 */
public class TagViewActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tagViewActivityTv)
    TagView tagViewActivityTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_view);
        ButterKnife.bind(this);
        // 执行
        execute();
        // 添监听事件
        setListener();
    }

    /**
     * 执行
     */
    private void execute() {
        ArrayList<Tag> tags = new ArrayList<>();
        for (int i = 0; i < WidgetMagic.INT_THIRTY; i++) {
            Tag tag = new Tag("测试");
            tag.setRadius(10.0f);
            tag.setLayoutColor(ContextCompat.getColor(this, R.color.blue));
            if (i % 2 == 0) {
                // you can set deletable or not
                tag.setDeletable(true);
            }
            tags.add(tag);
        }
        tagViewActivityTv.addTags(tags);
    }

    /**
     * 添监听事件
     */
    private void setListener() {
        tagViewActivityTv.setOnTagClickListener((tag, position) -> ToastUtils.shortShow(TagViewActivity.this, tag.getText()));
        tagViewActivityTv.setOnTagLongClickListener((tag, position) -> ToastUtils.shortShow(TagViewActivity.this, tag.getText()));
        tagViewActivityTv.setOnTagDeleteListener((view, tag, position) -> new MaterialAlertDialogBuilder(TagViewActivity.this)
                .setTitle(tag.getText())
                .setMessage(getString(R.string.tagViewDeleteHint))
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    view.remove(position);
                    ToastUtils.shortShow(TagViewActivity.this, "\"" + tag.getText() + "\" deleted");
                })
                .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss())
                .show());
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.tagViewActivityMbRestore, R.id.tagViewActivityMbClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 复原
            case R.id.tagViewActivityMbRestore:
                execute();
                break;
            // 清
            case R.id.tagViewActivityMbClear:
                tagViewActivityTv.removeAll();
                break;
            default:
                break;
        }
    }
}
