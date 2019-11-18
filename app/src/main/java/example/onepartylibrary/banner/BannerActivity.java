package example.onepartylibrary.banner;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.banner.IBannerView;
import com.zsp.library.banner.view.BannerView;
import com.zsp.utilone.density.DensityUtils;
import com.zsp.utilone.glide.transformation.CenterCropRoundCornerTransform;
import com.zsp.utilone.glide.util.GlideUtils;
import com.zsp.widget.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * @decs: 轮播页
 * @author: 郑少鹏
 * @date: 2019/8/20 14:38
 */
public class BannerActivity extends AppCompatActivity {
    @BindView(R.id.bannerActivityBvInterval)
    BannerView bannerActivityBvInterval;
    @BindView(R.id.bannerActivityBvSmooth)
    BannerView bannerActivityBvSmooth;
    /**
     * 数据
     */
    private List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);
        initConfiguration();
        interval();
        smooth();
    }

    private void initConfiguration() {
        stringList = new ArrayList<>();
        stringList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565003123891&di=6b99987620571a5600e681f1ed9a7e56&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FqpYuMBtI9tONDBEBXrp6Cg%3D%3D%2F6631251384142500810.jpg");
        stringList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565003123891&di=6b99987620571a5600e681f1ed9a7e56&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FqpYuMBtI9tONDBEBXrp6Cg%3D%3D%2F6631251384142500810.jpg");
        stringList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565003123891&di=6b99987620571a5600e681f1ed9a7e56&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FqpYuMBtI9tONDBEBXrp6Cg%3D%3D%2F6631251384142500810.jpg");
        stringList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565003123891&di=6b99987620571a5600e681f1ed9a7e56&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FqpYuMBtI9tONDBEBXrp6Cg%3D%3D%2F6631251384142500810.jpg");
    }

    /**
     * 间隔
     */
    private void interval() {
        bannerActivityBvInterval.setBannerViewImpl(new IBannerView() {
            @Override
            public void onPageSelected(int position) {
                Timber.d("选 %s", position);
            }

            @Override
            public boolean isDefaultAutoScroll() {
                return true;
            }

            @Override
            public View getDefaultView(@NotNull Context context) {
                View view = new View(context);
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                return view;
            }

            @Override
            public int getCount() {
                return stringList.size();
            }

            @NotNull
            @Override
            public View getItemView(@NotNull Context context) {
                return new ImageView(context);
            }

            @Override
            public void onBindView(@NotNull View itemView, int position) {
                if (itemView instanceof ImageView) {
                    ImageView imageView = (ImageView) itemView;
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    GlideUtils.loadByObject(itemView.getContext(), stringList.get(position), imageView);
                    imageView.setOnClickListener(view -> Timber.d("onBindView onClick %s", position));
                }
            }
        });
    }

    /**
     * 平滑
     */
    private void smooth() {
        bannerActivityBvSmooth.setBannerViewImpl(new IBannerView() {
            @Override
            public void onPageSelected(int position) {
                Timber.d("选 %s", position);
            }

            @Override
            public boolean isDefaultAutoScroll() {
                return true;
            }

            @Override
            public View getDefaultView(@NotNull Context context) {
                View view = new View(context);
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                return view;
            }

            @Override
            public int getCount() {
                return stringList.size();
            }

            @NotNull
            @Override
            public View getItemView(@NotNull Context context) {
                return new ImageView(context);
            }

            @Override
            public void onBindView(@NotNull View itemView, int position) {
                if (itemView instanceof ImageView) {
                    ImageView imageView = (ImageView) itemView;
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    GlideUtils.loadByObjectTransformation(
                            itemView.getContext(), stringList.get(position),
                            new CenterCropRoundCornerTransform(DensityUtils.dipToPxByFloat(imageView.getContext(), 8.0f)), imageView);
                    imageView.setOnClickListener(view -> Timber.d("onBindView onClick %s", position));
                }
            }
        });
    }

    @OnClick({R.id.bannerActivityMbClear, R.id.bannerActivityMbAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 清
            case R.id.bannerActivityMbClear:
                stringList.clear();
                bannerActivityBvInterval.doRecreate();
                bannerActivityBvSmooth.doRecreate();
                break;
            // 添
            case R.id.bannerActivityMbAdd:
                stringList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565003123891&di=6b99987620571a5600e681f1ed9a7e56&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FqpYuMBtI9tONDBEBXrp6Cg%3D%3D%2F6631251384142500810.jpg");
                bannerActivityBvInterval.doRecreate();
                bannerActivityBvSmooth.doRecreate();
                break;
            default:
                break;
        }
    }
}
