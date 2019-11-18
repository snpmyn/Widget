package example.onepartylibrary.searchbox;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 搜索框页
 * @author: 郑少鹏
 * @date: 2019/7/19 14:54
 */
public class SearchBoxActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.searchBoxActivityMbSearchBoxOne,
            R.id.searchBoxActivityMbSearchBoxTwo,
            R.id.searchBoxActivityMbSearchBoxThree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 搜索框一
            case R.id.searchBoxActivityMbSearchBoxOne:
                IntentUtils.jumpNoBundle(this, SearchBoxOneActivity.class);
                break;
            // 搜索框二
            case R.id.searchBoxActivityMbSearchBoxTwo:
                IntentUtils.jumpNoBundle(this, SearchBoxTwoActivity.class);
                break;
            // 搜索框三
            case R.id.searchBoxActivityMbSearchBoxThree:
                IntentUtils.jumpNoBundle(this, SearchBoxThreeActivity.class);
                break;
            default:
                break;
        }
    }
}
