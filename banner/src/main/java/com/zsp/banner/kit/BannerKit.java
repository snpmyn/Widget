package com.zsp.banner.kit;

import androidx.viewpager.widget.ViewPager;

import com.youth.banner.Banner;
import com.zsp.banner.loader.GlideImageLoader;

import java.util.List;

/**
 * Created on 2019/8/2.
 *
 * @author 郑少鹏
 * @desc BannerKit
 */
public class BannerKit {
    /**
     * 轮播
     *
     * @param banner    控件
     * @param integers  数据
     * @param c         变压器
     * @param delayTime 延时
     */
    public void banner(Banner banner, List<Integer> integers, Class<? extends ViewPager.PageTransformer> c, int delayTime) {
        // 图加载器
        banner.setImageLoader(new GlideImageLoader());
        // 图集
        banner.setImages(integers);
        // 动效
        banner.setBannerAnimation(c);
        // 时间
        banner.setDelayTime(delayTime);
        // 设置法全调后调
        banner.start();
    }
}
