package com.zsp.library.picture.easing.exception;

/**
 * @decs: 不相容比率异常
 * @author: 郑少鹏
 * @date: 2019/10/26 16:38
 */
public class IncompatibleRatioException extends RuntimeException {
    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}
