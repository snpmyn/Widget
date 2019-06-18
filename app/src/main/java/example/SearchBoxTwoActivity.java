package example;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.searchbox.two.fragment.SearchDialogFragment;
import com.zsp.library.textview.DrawableCenterTextView;
import com.zsp.utilone.ToastUtils;
import com.zsp.utilone.ViewUtils;
import com.zsp.utilone.animation.AnimationManager;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 搜索框二页
 * @author: 郑少鹏
 * @date: 2019/4/29 18:57
 */
public class SearchBoxTwoActivity extends AppCompatActivity implements SearchDialogFragment.OnSearchClickListener {
    @BindView(R.id.searchBoxTwoActivityDctvSearch)
    DrawableCenterTextView searchBoxTwoActivityDctvSearch;
    /**
     * 搜索对话框碎片
     */
    private SearchDialogFragment searchDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box_two);
        ButterKnife.bind(this);
        step();
    }

    private void step() {
        searchDialogFragment = SearchDialogFragment.newInstance();
        searchDialogFragment.setOnSearchClickListener(this);
        searchDialogFragment.setOnSearchDialogHideListener(() -> {
            ViewUtils.showView(searchBoxTwoActivityDctvSearch);
            AnimationManager.xGradual(searchBoxTwoActivityDctvSearch, 0, searchBoxTwoActivityDctvSearch.getWidth(), 300, 1);
        });
    }

    @OnClick({R.id.searchBoxTwoActivityIvTopBack, R.id.searchBoxTwoActivityDctvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 返
            case R.id.searchBoxTwoActivityIvTopBack:
                finish();
                break;
            // 搜索框
            case R.id.searchBoxTwoActivityDctvSearch:
                ViewUtils.hideView(searchBoxTwoActivityDctvSearch, View.GONE);
                searchDialogFragment.showFragment(getSupportFragmentManager(), SearchDialogFragment.TAG);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSearchClick(String keyword) {
        ToastUtils.shortShow(this, keyword);
    }
}
