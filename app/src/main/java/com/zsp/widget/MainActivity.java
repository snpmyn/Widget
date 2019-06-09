package com.zsp.widget;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.PictureActivity;

/**
 * @decs: 主页
 * @author: 郑少鹏
 * @date: 2019/6/5 10:38
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.mainActivityMbPicture)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.mainActivityMbPicture) {
            IntentUtils.jumpNoBundle(this, PictureActivity.class);
        }
    }
}
