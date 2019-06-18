package com.zsp.widget;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.PictureActivity;
import example.SearchBoxOneActivity;
import example.SearchBoxTwoActivity;

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

    @OnClick({R.id.mainActivityMbPicture,
            R.id.mainActivitySearchBoxOne,
            R.id.mainActivitySearchBoxTwo,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // Switch
            case R.id.mainActivityMbPicture:
                IntentUtils.jumpNoBundle(this, PictureActivity.class);
                break;
            // 搜索框一
            case R.id.mainActivitySearchBoxOne:
                IntentUtils.jumpNoBundle(this, SearchBoxOneActivity.class);
                break;
            // 搜索框二
            case R.id.mainActivitySearchBoxTwo:
                IntentUtils.jumpNoBundle(this, SearchBoxTwoActivity.class);
                break;
            default:
                break;
        }
    }
}
