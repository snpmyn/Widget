package example.onepartylibrary.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.zsp.library.dialog.materialalertdialog.MyMaterialAlertDialogBuilder;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @desc: 对话框三页
 * @author: zsp
 * @date: 2021/1/13 11:13 AM
 */
public class DialogThreeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_three);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.dialogThreeActivityMbDialog})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.dialogThreeActivityMbDialog) {
            new MyMaterialAlertDialogBuilder(this)
                    .setTitle("标题")
                    .setMessage("消息")
                    .setNeutralButton("取消", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("确定", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
        }
    }
}