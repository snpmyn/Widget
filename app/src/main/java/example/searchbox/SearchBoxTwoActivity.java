package example.searchbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.zsp.library.searchbox.two.fragment.SearchDialogFragment;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 搜索框二页
 * @author: 郑少鹏
 * @date: 2019/4/29 18:57
 */
public class SearchBoxTwoActivity extends AppCompatActivity {
    @BindView(R.id.searchBoxTwoActivityMt)
    MaterialToolbar searchBoxTwoActivityMt;
    /**
     * 搜索对话框碎片
     */
    private SearchDialogFragment searchDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box_two);
        ButterKnife.bind(this);
        stepMaterialToolbar();
        stepSearchDialogFragment();
    }

    private void stepMaterialToolbar() {
        searchBoxTwoActivityMt.inflateMenu(R.menu.search_menu);
        searchBoxTwoActivityMt.setNavigationOnClickListener(v -> finish());
        searchBoxTwoActivityMt.setOnMenuItemClickListener(item -> {
            searchBoxTwoActivityMt.getMenu().findItem(R.id.pharmaceuticalKnowledgeListActivityMenuSearch).setVisible(false);
            searchDialogFragment.showFragment(getSupportFragmentManager(), "SearchDialogFragment");
            return true;
        });
    }

    private void stepSearchDialogFragment() {
        searchDialogFragment = SearchDialogFragment.newInstance("WidgetSearchHistory.db");
        searchDialogFragment.setOnSearchClickListener(keyword -> ToastUtils.shortShow(this, keyword));
        searchDialogFragment.setOnSearchDialogHideListener(() -> searchBoxTwoActivityMt.getMenu().findItem(R.id.pharmaceuticalKnowledgeListActivityMenuSearch).setVisible(true));
    }
}
