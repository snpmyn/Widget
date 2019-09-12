package com.zsp.library.sidebar;

import com.zsp.library.contact.bean.ContactBean;

import java.util.Comparator;

/**
 * @decs: 首字母排序
 * @author: 郑少鹏
 * @date: 2019/9/12 16:48
 */
public class AcronymSorting implements Comparator<ContactBean> {
    @Override
    public int compare(ContactBean contactBean, ContactBean contactBean1) {
        if (contactBean == null || contactBean1 == null) {
            return 0;
        }
        String lhsSortLetters = contactBean.getIndex().substring(0, 1).toUpperCase();
        String rhsSortLetters = contactBean1.getIndex().substring(0, 1).toUpperCase();
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
