package example.pudding;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.pudding.Pudding;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: pudding页
 * @author: 郑少鹏
 * @date: 2019/7/18 10:37
 */
public class PuddingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pudding);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.puddingActivityJavaExampleOne,
            R.id.puddingActivityJavaExampleTwo,
            R.id.puddingActivityJavaExampleThree,
            R.id.puddingActivityJavaExampleFour,
            R.id.puddingActivityJavaExampleFive,
            R.id.puddingActivityJavaExampleSix,
            R.id.puddingActivityJavaExampleSeven,
            R.id.puddingActivityJavaExampleEight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 示例一
            case R.id.puddingActivityJavaExampleOne:
                exampleOne();
                break;
            // 示例二
            case R.id.puddingActivityJavaExampleTwo:
                exampleTwo();
                break;
            // 示例三
            case R.id.puddingActivityJavaExampleThree:
                exampleThree();
                break;
            // 示例四
            case R.id.puddingActivityJavaExampleFour:
                exampleFour();
                break;
            // 示例五
            case R.id.puddingActivityJavaExampleFive:
                exampleFive();
                break;
            // 示例六
            case R.id.puddingActivityJavaExampleSix:
                exampleSix();
                break;
            // 示例七
            case R.id.puddingActivityJavaExampleSeven:
                exampleSeven();
                break;
            // 示例八
            case R.id.puddingActivityJavaExampleEight:
                exampleEight();
                break;
            default:
                break;
        }
    }

    /**
     * 示例一
     */
    private void exampleOne() {
        Pudding.create(this, choco -> {
            choco.setTitle("标题");
            choco.setText("内容");
            return null;
        }).show();
    }

    /**
     * 示例二
     */
    private void exampleTwo() {
        Pudding.create(this, choco -> {
            choco.setChocoBackgroundColor(ContextCompat.getColor(PuddingActivity.this, R.color.colorAccent));
            choco.setTitle("标题");
            choco.setTitleTypeface(Typeface.DEFAULT_BOLD);
            choco.setText("内容");
            choco.setTextAppearance(R.style.TextAppearance_AppCompat_Caption);
            return null;
        }).show();
    }

    /**
     * 示例三
     */
    private void exampleThree() {
        Pudding.create(this, choco -> {
            choco.setTitle("标题");
            choco.setText("内容");
            choco.setIcon(R.drawable.ic_top_back_light_24dp_background);
            return null;
        }).show();
    }

    /**
     * 示例四
     */
    private void exampleFour() {
        Pudding.create(this, choco -> {
            choco.setTitle("标题");
            choco.setText("内容");
            choco.setEnableInfiniteDuration(true);
            return null;
        }).show();
    }

    /**
     * 示例五
     */
    private void exampleFive() {
        Pudding.create(this, choco -> {
            choco.setTitle("标题");
            choco.setText("内容");
            choco.setEnableProgress(true);
            choco.setProgressColorRes(R.color.colorAccent);
            return null;
        }).show();
    }

    /**
     * 示例六
     */
    private void exampleSix() {
        Pudding.create(this, choco -> {
            choco.setTitle("标题");
            choco.setText("内容");
            choco.setEnableInfiniteDuration(true);
            choco.enableSwipeToDismiss();
            return null;
        }).show();
    }

    /**
     * 示例七
     */
    private void exampleSeven() {
        Pudding.create(this, choco -> {
            choco.setTitle("标题");
            choco.setText("内容");
            choco.onShow(() -> {
                ToastUtils.shortShow(PuddingActivity.this, "onShow");
                return null;
            });
            choco.onDismiss(() -> {
                ToastUtils.shortShow(PuddingActivity.this, "onDismiss");
                return null;
            });
            return null;
        }).show();
    }

    /**
     * 示例八
     */
    private void exampleEight() {
        Pudding.create(this, choco -> {
            choco.setTitle("按钮");
            choco.setText("待调试");
            choco.addMaterialButton("ok", R.style.PuddingMaterialButtonStyle, view -> ToastUtils.shortShow(PuddingActivity.this, "ok"));
            choco.addMaterialButton("cancel", R.style.PuddingMaterialButtonStyle, view -> ToastUtils.shortShow(PuddingActivity.this, "cancel"));
            return null;
        }).show();
    }
}
