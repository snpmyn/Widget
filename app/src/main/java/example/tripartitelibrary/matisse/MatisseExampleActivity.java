package example.tripartitelibrary.matisse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;
import com.zhihu.matisse.Matisse;
import com.zsp.library.picture.luban.Luban;
import com.zsp.matisse.kit.MatisseKit;
import com.zsp.ucrop.CropExecutor;
import com.zsp.utilone.glide.util.GlideUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import value.WidgetFolder;

/**
 * @decs: Matisse示例页
 * @author: 郑少鹏
 * @date: 2019/10/28 10:01
 */
public class MatisseExampleActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.matisseExampleActivityIv)
    ImageView matisseExampleActivityIv;
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;
    /**
     * CompositeDisposable
     */
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_example);
        ButterKnife.bind(this);
        initConfiguration();
    }

    private void initConfiguration() {
        // SoulPermissionUtils
        soulPermissionUtils = new SoulPermissionUtils();
        // CompositeDisposable
        compositeDisposable = new CompositeDisposable();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.matisseExampleActivityMb)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.matisseExampleActivityMb) {
            select();
        }
    }

    /**
     * 选择
     */
    private void select() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.CAMERA, soulPermissionUtils,
                true, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        MatisseKit.matisseOperateInActivity(MatisseExampleActivity.this, 1, getString(R.string.fileProviderAuthorities));
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(MatisseExampleActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MatisseKit.REQUEST_CODE:
                    assert data != null;
                    List<String> list = new ArrayList<>(Matisse.obtainPathResult(data));
                    CropExecutor.uCropInActivity(this, list.get(0), WidgetFolder.CROP_PICTURE, 350, 350);
                    break;
                case UCrop.REQUEST_CROP:
                    if (data != null) {
                        Uri uri = UCrop.getOutput(data);
                        if (uri != null) {
                            List<String> strings = new ArrayList<>(1);
                            String path = FileUtils.getPath(this, uri);
                            GlideUtils.loadByStringPlaceHolderColor(this, path, R.color.transparent, matisseExampleActivityIv);
                            strings.add(path);
                            syncCompress(strings);
                        }
                    }
                    break;
                case UCrop.RESULT_ERROR:
                    if (data != null) {
                        Throwable throwable = UCrop.getError(data);
                        if (throwable != null) {
                            Timber.e(throwable);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 同步压缩
     *
     * @param photos 图集
     * @param <T>    <T>
     */
    private <T> void syncCompress(final List<T> photos) {
        com.zsp.utilone.file.FileUtils.createFolder(WidgetFolder.COMPRESS_PICTURE, false);
        compositeDisposable.add(Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(list -> Luban.with(this)
                        .setTargetDir(WidgetFolder.COMPRESS_PICTURE)
                        .load(list)
                        .get())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .onErrorResumeNext(Flowable.empty())
                .subscribe(list -> {
                    for (File file : list) {
                        Timber.d(file.getAbsolutePath());
                        ToastUtils.shortShow(this, getString(R.string.uploadSuccess));
                    }
                }));
    }
}
