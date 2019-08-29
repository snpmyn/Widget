package com.zsp.library.picture.luban;

/**
 * @decs: OnRenameListener
 * A functional interface (callback) that used to rename the file after compress.
 * @author: 郑少鹏
 * @date: 2019/8/28 19:15
 */
public interface OnRenameListener {
    /**
     * 重命名
     * <p>
     * 压前调该法改压后文件名。
     *
     * @param filePath 传入文件路径
     * @return 返重命名后字符串
     */
    String rename(String filePath);
}
