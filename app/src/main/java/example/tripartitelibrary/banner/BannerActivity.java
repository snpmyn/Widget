package example.tripartitelibrary.banner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.zsp.banner.kit.BannerKit;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 轮播页
 * @author: 郑少鹏
 * @date: 2019/11/18 11:09
 */
public class BannerActivity extends AppCompatActivity {
    @BindView(R.id.bannerActivityBanner)
    Banner bannerActivityBanner;
    /**
     * BannerKit
     */
    private BannerKit bannerKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner2);
        ButterKnife.bind(this);
        // 初始化配置
        initConfiguration();
        // 开始逻辑
        startLogic();
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        bannerKit = new BannerKit();
    }

    /**
     * 开始逻辑
     */
    private void startLogic() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.image_one);
        integerList.add(R.drawable.image_two);
        integerList.add(R.drawable.image_three);
        bannerKit.banner(bannerActivityBanner, integerList, Transformer.DepthPage, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开播
        bannerActivityBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停播
        bannerActivityBanner.stopAutoPlay();
    }
}
