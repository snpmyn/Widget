package com.zsp.library.picture.luban.predicate;

/**
 * @decs: CompressionPredicate
 * A functional interface (callback) that returns true or false for the given input path should be compressed.
 * @author: 郑少鹏
 * @date: 2019/8/28 19:02
 */
public interface CompressionPredicate {
    /**
     * Determine the given input path should be compressed and return a boolean.
     *
     * @param path input path
     * @return the boolean result
     */
    boolean apply(String path);
}
