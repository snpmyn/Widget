package example.onepartylibrary.layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.layout.ripple.RippleLayout;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 波纹布局页
 * @author: 郑少鹏
 * @date: 2019/8/26 18:02
 */
public class RippleLayoutActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rippleLayoutActivityRl)
    RippleLayout rippleLayoutActivityRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_layout);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        rippleLayoutActivityRl.setOnRippleCompleteListener(rippleLayout -> ToastUtils.shortShow(RippleLayoutActivity.this, "onComplete"));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.rippleLayoutActivityRl)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.rippleLayoutActivityRl) {
            ToastUtils.shortShow(this, "点击");
        }
    }
}
