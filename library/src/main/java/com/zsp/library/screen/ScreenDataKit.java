package com.zsp.library.screen;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2019/6/12.
 *
 * @author 郑少鹏
 * @desc ScreenDataKit
 */
public class ScreenDataKit {
    /**
     * 单打包
     *
     * @param stringList          数据
     * @param correspondingValues 对应值
     */
    public void singlePack(List<Object> stringList, Object... correspondingValues) {
        if (stringList.size() > 0) {
            stringList.clear();
        }
        stringList.addAll(Arrays.asList(correspondingValues));
    }

    /**
     * 多打包
     *
     * @param stringList         数据
     * @param correspondingValue 对应值
     * @param selected           选否
     */
    public void multiPack(List<Object> stringList, Object correspondingValue, boolean selected) {
        if (selected) {
            if (!stringList.contains(correspondingValue)) {
                stringList.add(correspondingValue);
            }
        } else {
            stringList.remove(correspondingValue);
        }
    }
}
