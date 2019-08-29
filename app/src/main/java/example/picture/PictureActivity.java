package example.picture;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 图片页
 * @author: 郑少鹏
 * @date: 2019/8/28 19:28
 */
public class PictureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pictureActivityMbPreviewExample, R.id.pictureActivityMbLubanCompression})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 预览示例页
            case R.id.pictureActivityMbPreviewExample:
                IntentUtils.jumpNoBundle(this, PreviewExampleActivity.class);
                break;
            // 鲁班压缩页
            case R.id.pictureActivityMbLubanCompression:
                IntentUtils.jumpNoBundle(this, LubanCompressActivity.class);
                break;
            default:
                break;
        }
    }
}
