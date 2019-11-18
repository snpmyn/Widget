package example.onepartylibrary.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 对话框页
 * @author: 郑少鹏
 * @date: 2019/7/19 14:48
 */
public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.dialogActivityMbDialogOne, R.id.dialogActivityMbDialogTwo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 对话框一
            case R.id.dialogActivityMbDialogOne:
                IntentUtils.jumpNoBundle(this, DialogOneActivity.class);
                break;
            // 对话框二
            case R.id.dialogActivityMbDialogTwo:
                IntentUtils.jumpNoBundle(this, DialogTwoActivity.class);
                break;
            default:
                break;
        }
    }
}
