package example.picture;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.zsp.library.picture.kit.DragKit;
import com.zsp.widget.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 图片页
 * @author: 郑少鹏
 * @date: 2019/6/9 11:22
 */
public class PictureActivity extends AppCompatActivity {
    @BindView(R.id.pictureActivityIv)
    ImageView pictureActivityIv;
    /**
     * DragKit
     */
    private DragKit dragKit;
    /**
     * 更新索引
     */
    private int updateIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                // 状态栏或导航栏显/隐布局不变
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        // 初始化配置
        initConfiguration();
        // 添监听事件
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        dragKit = new DragKit(this);
    }

    /**
     * 添监听事件
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setListener() {
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                Log.d("exitSharedElement", "onSharedElementStart");
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                Log.d("exitSharedElement", "onSharedElementEnd");
            }

            @Override
            public void onRejectSharedElements(List<View> rejectedSharedElements) {
                super.onRejectSharedElements(rejectedSharedElements);
                Log.d("exitSharedElement", "onRejectSharedElements");
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Log.d("exitSharedElement", "onMapSharedElements");
                if (updateIndex == 1) {
                    sharedElements.put(dragKit.sharedElementName, pictureActivityIv);
                }
            }

            @Override
            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                Log.d("exitSharedElement", "onCaptureSharedElementSnapshot");
                sharedElement.setAlpha(1.0f);
                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
            }

            @Override
            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
                Log.d("exitSharedElement", "onCreateSnapshotView");
                return super.onCreateSnapshotView(context, snapshot);
            }

            @Override
            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
                Log.d("exitSharedElement", "onSharedElementsArrived");
            }
        });
    }

    @OnClick(R.id.pictureActivityIv)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.pictureActivityIv) {
            dragKit.jump(this, view, new Object[]{R.mipmap.ic_launcher}, 1);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("updateIndex")})
    public void updateIndex(Integer integer) {
        // 通知对应共享元素键值对更新
        updateIndex = integer;
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("updateView")})
    public void updateView(Integer integer) {
        // 通知对应视图重显
        if (integer == 1) {
            pictureActivityIv.setAlpha(1.0f);
        }
    }
}