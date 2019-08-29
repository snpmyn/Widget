package com.zsp.library.picture.luban;

import java.io.IOException;
import java.io.InputStream;

/**
 * @decs: BaseInputStreamAdapter
 * Automatically close the previous InputStream when opening a new InputStream, and finally need to manually call {@link #close()} to release the resource.
 * @author: 郑少鹏
 * @date: 2019/8/28 19:06
 */
public abstract class BaseInputStreamAdapter implements InputStreamProvider {
    private InputStream inputStream;

    @Override
    public InputStream open() throws IOException {
        close();
        inputStream = openInternal();
        return inputStream;
    }

    /**
     * Open internal.
     *
     * @return InputStream
     * @throws IOException IOException
     */
    public abstract InputStream openInternal() throws IOException;

    @Override
    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignore) {
            } finally {
                inputStream = null;
            }
        }
    }
}