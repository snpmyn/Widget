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
     * 单选打包
     *
     * @param stringList          数据
     * @param selected            选否
     * @param correspondingValues 对应值
     */
    public void singleSelectPack(List<Object> stringList, boolean selected, Object... correspondingValues) {
        if (stringList.size() > 0) {
            stringList.clear();
        }
        if (selected) {
            stringList.addAll(Arrays.asList(correspondingValues));
        }
    }

    /**
     * 多选打包
     *
     * @param stringList          数据
     * @param selected            选否
     * @param correspondingValues 对应值
     */
    public void multiSelectPack(List<Object> stringList, boolean selected, Object... correspondingValues) {
        for (Object o : correspondingValues) {
            if (selected) {
                if (stringList.contains(o)) {
                    return;
                }
                stringList.add(o);
            } else {
                stringList.remove(o);
            }
        }
    }
}
