package com.zsp.library.contact.extractor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.zsp.library.contact.bean.ContactBean;
import com.zsp.utilone.thread.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created on 2019/7/22.
 *
 * @author 郑少鹏
 * @desc 联系人提取器
 */
public class ContactExtractor {
    private Context context;
    private List<ContactBean> contactBeans;
    private ScheduledExecutorService scheduledExecutorService;
    private ContractExtractorListener contractExtractorListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public ContactExtractor(Context context) {
        this.context = context.getApplicationContext();
        this.contactBeans = new ArrayList<>();
        this.scheduledExecutorService = ThreadManager.stepScheduledExecutorService();
    }

    /**
     * 提取
     */
    public void extract() {
        scheduledExecutorService.execute(new CustomContactExtractor());
    }

    /**
     * 设联系人提取器监听
     *
     * @param contractExtractorListener 联系人提取器监听
     */
    public void setContractExtractorListener(ContractExtractorListener contractExtractorListener) {
        this.contractExtractorListener = contractExtractorListener;
    }

    class CustomContactExtractor implements Runnable {
        @Override
        public void run() {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String contactId;
                    String name;
                    String phoneNumber = null;
                    String emailAddress = null;
                    // 联系人ID
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    // 姓名
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // 手机号码
                    Cursor phone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null);
                    if (phone != null) {
                        while (phone.moveToNext()) {
                            phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    }
                    if (phone != null) {
                        phone.close();
                    }
                    // 邮箱地址
                    Cursor email = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                            null,
                            null);
                    if (email != null) {
                        while (email.moveToNext()) {
                            emailAddress = email.getString(email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        }
                    }
                    if (email != null) {
                        email.close();
                    }
                    // 头像
                    /*getContactHead(context, contactId, R.mipmap.icon_round);*/
                    // 判空
                    if (name != null && phoneNumber != null) {
                        ContactBean contactBean = new ContactBean();
                        contactBean.setName(name);
                        contactBean.setPhoneNumber(phoneNumber);
                        contactBean.setEmailAddress(emailAddress);
                        contactBeans.add(contactBean);
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            scheduledExecutorService.shutdown();
            contractExtractorListener.complete(contactBeans);
        }
    }

    /**
     * 联系人提取器监听
     */
    public interface ContractExtractorListener {
        /**
         * 完成
         *
         * @param contactBeans 数据
         */
        void complete(List<ContactBean> contactBeans);
    }
}
