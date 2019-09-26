package com.zsp.library.screen.bean;

/**
 * Created on 2019/9/26.
 *
 * @author 郑少鹏
 * @desc 互斥
 */
public class MutuallyExclusiveBean {
    /**
     * 组ID
     */
    private String groupId;
    /**
     * 类别
     */
    private String classification;

    /**
     * constructor
     *
     * @param groupId        组ID
     * @param classification 类别
     */
    public MutuallyExclusiveBean(String groupId, String classification) {
        this.groupId = groupId;
        this.classification = classification;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getClassification() {
        return classification;
    }
}
