package example.onepartylibrary.searchbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.zsp.library.searchbox.three.MultiSearchView;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;
import com.zsp.widget.databinding.ActivitySearchBoxThreeBinding;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * @decs: 搜索框三页
 * @author: 郑少鹏
 * @date: 2019/10/12 18:05
 */
public class SearchBoxThreeActivity extends AppCompatActivity {
    private ActivitySearchBoxThreeBinding activitySearchBoxThreeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box_three);
        // ActivitySearchBoxThreeBinding据布局文件名生成
        activitySearchBoxThreeBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_box_three);
        setListener();
    }

    private void setListener() {
        activitySearchBoxThreeBinding.searchBoxThreeActivityMsv.setSearchViewListener(new MultiSearchView.MultiSearchViewListener() {
            @Override
            public void onTextChanged(int index, @NotNull CharSequence s) {
                Timber.d("onTextChanged %s", s);
            }

            @Override
            public void onSearchComplete(int index, @NotNull CharSequence s) {
                Timber.d("onSearchComplete %s", s);
            }

            @Override
            public void onSearchItemRemoved(int index) {
                Timber.d("onSearchItemRemoved %s", index);
            }

            @Override
            public void onItemSelected(int index, @NotNull CharSequence s) {
                ToastUtils.shortShow(SearchBoxThreeActivity.this, s.toString());
            }
        });
    }
}
