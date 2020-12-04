package example.onepartylibrary.picture;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.picture.luban.Luban;
import com.zsp.library.picture.luban.listener.OnCompressListener;
import com.zsp.utilone.file.FileUtils;
import com.zsp.utilone.glide.util.GlideUtils;
import com.zsp.widget.R;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
 * @decs: 鲁班压缩页
 * @author: 郑少鹏
 * @date: 2019/8/28 19:33
 */
public class LubanCompressActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lubanCompressActivityTv)
    TextView lubanCompressActivityTv;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lubanCompressActivityIv)
    ImageView lubanCompressActivityIv;
    /**
     * CompositeDisposable
     */
    private CompositeDisposable compositeDisposable;
    /**
     * 文件集
     */
    private List<File> files;
    /**
     * 统一资源标识符集
     */
    private List<Uri> uris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luban_compress);
        ButterKnife.bind(this);
        initConfiguration();
        initData();
    }

    private void initConfiguration() {
        // CompositeDisposable
        compositeDisposable = new CompositeDisposable();
        // 文件集
        files = new ArrayList<>(1);
        // 统一资源标识符集
        uris = new ArrayList<>(1);
    }

    private void initData() {
        // 文件集
        files = FileUtils.assetsToFiles(LubanCompressActivity.this, 1, "img_", "assets_transferred");
        // 统一资源标识符集
        uris = FileUtils.assetsToUris(LubanCompressActivity.this, 1, "img_", "assets_transferred");
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.lubanCompressActivityMbSyncCompress, R.id.lubanCompressActivityMbAsyncCompress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 同步压缩
            case R.id.lubanCompressActivityMbSyncCompress:
                syncCompress(files);
                break;
            // 异步压缩
            case R.id.lubanCompressActivityMbAsyncCompress:
                asyncCompress(uris);
                break;
            default:
                break;
        }
    }

    /**
     * 同步压缩
     *
     * @param photos 图集
     * @param <T>    <T>
     */
    private <T> void syncCompress(final List<T> photos) {
        FileUtils.createFolder(WidgetFolder.COMPRESS_PICTURE, false);
        compositeDisposable.add(Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(list -> Luban.with(LubanCompressActivity.this)
                        .setTargetDir(WidgetFolder.COMPRESS_PICTURE)
                        .load(list)
                        .get())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e)
                .onErrorResumeNext(Flowable.empty())
                .subscribe(list -> {
                    for (int i = 0; i < list.size(); i++) {
                        File file = list.get(i);
                        Timber.d(file.getAbsolutePath());
                        GlideUtils.loadByObject(LubanCompressActivity.this, file.getAbsolutePath(), lubanCompressActivityIv);
                        showResult(files.get(i), file);
                    }
                }));
    }

    /**
     * 异步压缩
     *
     * @param photos 图集
     * @param <T>    <T>
     */
    private <T> void asyncCompress(final List<T> photos) {
        FileUtils.createFolder(WidgetFolder.COMPRESS_PICTURE, false);
        Luban.with(this)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(WidgetFolder.COMPRESS_PICTURE)
                .setFocusAlpha(false)
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setRenameListener(filePath -> {
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(filePath.getBytes());
                        return new BigInteger(1, md.digest()).toString(32);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        Timber.d(file.getAbsolutePath());
                        GlideUtils.loadByObject(LubanCompressActivity.this, file.getAbsolutePath(), lubanCompressActivityIv);
                        showResult(files.get(0), file);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    /**
     * 显结果
     *
     * @param originalFile   原始文件
     * @param compressedFile 压缩后文件
     */
    private void showResult(File originalFile, File compressedFile) {
        int[] originalSize = FileUtils.calculatedImageFileSize(originalFile);
        int[] compressedSize = FileUtils.calculatedImageFileSize(compressedFile);
        String originalParameters = String.format(Locale.CHINA, "原图参数：%d*%d, %dk", originalSize[0], originalSize[1], originalFile.length() >> 10);
        String compressedParameters = String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", compressedSize[0], compressedSize[1], compressedFile.length() >> 10);
        lubanCompressActivityTv.setText(String.format(getString(R.string.pictureParameters), originalParameters, compressedParameters));
    }
}
