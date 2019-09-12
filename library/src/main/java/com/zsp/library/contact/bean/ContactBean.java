package com.zsp.library.contact.bean;

import com.zsp.library.sidebar.FirstLetter;

import java.io.Serializable;

/**
 * Created on 2018/1/8.
 *
 * @author 郑少鹏
 * @desc 联系人
 */
public class ContactBean implements Serializable {
    private String index;
    private String name;
    private String phoneNumber;
    private String emailAddress;

    public String getIndex() {
        return index;
    }

    public void setIndex(String name) {
        this.index = FirstLetter.getFirstLetter(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
