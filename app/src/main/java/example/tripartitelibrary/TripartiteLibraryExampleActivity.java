package example.tripartitelibrary;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.tripartitelibrary.banner.BannerActivity;
import example.tripartitelibrary.bgaqrcodeandroid.ScanCodeActivity;
import example.tripartitelibrary.matisse.MatisseExampleActivity;

/**
 * @decs: 三方库示例页
 * @author: 郑少鹏
 * @date: 2019/11/18 11:16
 */
public class TripartiteLibraryExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripartite_library_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tripartiteLibraryExampleActivityMbMatisse,
            R.id.tripartiteLibraryExampleActivityMbScanCode,
            R.id.tripartiteLibraryExampleActivityMbBanner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // Matisse示例
            case R.id.tripartiteLibraryExampleActivityMbMatisse:
                IntentUtils.jumpNoBundle(this, MatisseExampleActivity.class);
                break;
            // 扫码
            case R.id.tripartiteLibraryExampleActivityMbScanCode:
                IntentUtils.jumpNoBundle(this, ScanCodeActivity.class);
                break;
            // 轮播
            case R.id.tripartiteLibraryExampleActivityMbBanner:
                IntentUtils.jumpNoBundle(this, BannerActivity.class);
                break;
            default:
                break;
        }
    }
}
