package com.zsp.library.picture.luban;

import java.io.IOException;
import java.io.InputStream;

/**
 * @decs: InputStreamProvider
 * 通此接口获输入流兼容文件、FileProvider方式所获图。
 * @author: 郑少鹏
 * @date: 2019/8/28 19:06
 */
public interface InputStreamProvider {
    /**
     * open
     *
     * @return InputStream
     * @throws IOException IOException
     */
    InputStream open() throws IOException;

    /**
     * close
     */
    void close();

    /**
     * Get path.
     *
     * @return path
     */
    String getPath();
}
