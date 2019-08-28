package com.zsp.library.picture.preview.activity;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zsp.library.R;
import com.zsp.library.picture.preview.kit.DragKit;
import com.zsp.utilone.glide.util.GlideUtils;
import com.zsp.utilone.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;
import value.WidgetLibraryRxBusConstant;

/**
 * @decs: 预览页
 * @author: 郑少鹏
 * @date: 2019/6/9 10:45
 */
public class PreviewActivity extends AppCompatActivity {
    /**
     * 控件
     */
    private ViewPager previewActivityVp;
    private ConstraintLayout previewActivityCl;
    /**
     * DragKit
     */
    private DragKit dragKit;
    /**
     * 数据
     */
    private List<View> views;
    /**
     * 滑否
     */
    private boolean scrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        // 拖拽返关时导航栏显拖拽视图可如下（同微信一致）
        // 隐状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 初始化控件
        stepUi();
        // 初始化配置
        initConfiguration();
        // 初始化数据
        initData();
        // 逻辑操作
        startLogic();
        // 添监听事件
        setListener();
    }

    /**
     * 初始化控件
     */
    private void stepUi() {
        previewActivityCl = findViewById(R.id.previewActivityCl);
        previewActivityVp = findViewById(R.id.previewActivityVp);
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        // DragKit
        dragKit = new DragKit(this);
        dragKit.setShareElementMode(true);
        dragKit.setDragCloseView(previewActivityCl, previewActivityVp);
        // 数据
        views = new ArrayList<>();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (Object o : DragKit.data) {
            ImageView imageView = new ImageView(this);
            if (o instanceof String) {
                GlideUtils.loadByStringPlaceHolderColor(this, String.valueOf(o), R.color.transparent, imageView);
            } else if (o instanceof Integer) {
                GlideUtils.loadByIntPlaceHolderColor(this, (Integer) o, R.color.transparent, imageView);
            }
            imageView.setAdjustViewBounds(true);
            views.add(imageView);
        }
    }

    /**
     * 逻辑操作
     */
    private void startLogic() {
        // ViewPager关联适配器
        previewActivityVp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(views.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }
        });
        // ViewPager当前项
        previewActivityVp.setCurrentItem(DragKit.index);
    }

    /**
     * 添监听事件
     */
    private void setListener() {
        dragCloseListener();
        onPageChangeListener();
        enterSharedElementCallback();
    }

    private void dragCloseListener() {
        dragKit.setDragCloseListener(new DragKit.DragCloseListener() {
            @Override
            public boolean intercept() {
                // 默false不拦截
                // 图放大状或滑返状需拦截
                return scrolling;
            }

            @Override
            public void dragStart() {
                // 拖拽开始
                RxBus.get().post(WidgetLibraryRxBusConstant.PICTURE_PREVIEW_$_UPDATE_VIEW, DragKit.index);
            }

            @Override
            public void dragging(float percent) {
                // 拖拽中（percent当前进度0-1）
                // 此处可处理一些逻辑
            }

            @Override
            public void dragCancel() {
                // 拖拽取消（会自动复原）
                // 此处可处理一些逻辑
            }

            @Override
            public void dragClose(boolean isShareElementMode) {
                // 拖拽关闭
                // 共享元素页需执行Activity之onBackPressed，用finish则返时无共享元素返回动画
                if (isShareElementMode) {
                    onBackPressed();
                }
            }
        });
    }

    private void onPageChangeListener() {
        previewActivityVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RxBus.get().post(WidgetLibraryRxBusConstant.PICTURE_PREVIEW_$_UPDATE_INDEX, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                scrolling = state != 0;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void enterSharedElementCallback() {
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                Timber.d("enterSharedElement: %s", "onSharedElementStart");
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                Timber.d("enterSharedElement: %s", "onSharedElementEnd");
            }

            @Override
            public void onRejectSharedElements(List<View> rejectedSharedElements) {
                super.onRejectSharedElements(rejectedSharedElements);
                Timber.d("enterSharedElement: %s", "onRejectSharedElements");
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Timber.d("enterSharedElement: %s", "onMapSharedElements");
            }

            @Override
            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                Timber.d("enterSharedElement: %s", "onCaptureSharedElementSnapshot");
                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
            }

            @Override
            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
                Timber.d("enterSharedElement: %s", "onCreateSnapshotView");
                return super.onCreateSnapshotView(context, snapshot);
            }

            @Override
            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                Timber.d("enterSharedElement: %s", "onSharedElementsArrived");
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragKit.handleMotionEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }
}
