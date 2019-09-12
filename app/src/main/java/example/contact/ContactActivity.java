package example.contact;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.contact.bean.ContactBean;
import com.zsp.library.contact.extractor.ContactExtractor;
import com.zsp.library.recyclerview.RecyclerViewConfigure;
import com.zsp.library.recyclerview.RecyclerViewDisplayKit;
import com.zsp.library.recyclerview.RecyclerViewScrollKit;
import com.zsp.library.sidebar.AcronymSorting;
import com.zsp.library.sidebar.WaveSideBar;
import com.zsp.utilone.list.ListUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.utilone.view.ViewUtils;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.contact.adapter.ContactAdapter;
import value.WidgetConstant;

/**
 * @decs: 联系人页
 * @author: 郑少鹏
 * @date: 2019/7/22 17:37
 */
public class ContactActivity extends AppCompatActivity {
    @BindView(R.id.contactActivityRv)
    RecyclerView contactActivityRv;
    @BindView(R.id.contactActivityWsb)
    WaveSideBar contactActivityWsb;
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;
    /**
     * ContactExtractor
     */
    private ContactExtractor contactExtractor;
    /**
     * 展示
     */
    private List<ContactBean> contactBeanList;
    private ContactAdapter contactAdapter;
    /**
     * RecyclerViewScrollKit
     */
    private RecyclerViewScrollKit recyclerViewScrollKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initConfiguration();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
          ContactExtractor
         */
        contactExtractor.setContractExtractorListener(contactBeans -> {
            contactBeanList = acronymSorting(contactBeans);
            contactAdapter.setContactData(contactBeanList);
            RecyclerViewDisplayKit.display(contactActivityRv, contactAdapter);
            ViewUtils.showView(contactActivityWsb);
            ListUtils.saveListToData(this, contactBeanList, WidgetConstant.CONTACT);
        });
        /*
          判
         */
        contactBeanList = (List<ContactBean>) ListUtils.getListFromData(this, WidgetConstant.CONTACT);
        if (null != contactBeanList && contactBeanList.size() > 0) {
            contactAdapter.setContactData(contactBeanList);
            RecyclerViewDisplayKit.display(contactActivityRv, contactAdapter);
            ViewUtils.showView(contactActivityWsb);
        } else {
            // 检请权限
            checkAndRequestPermission();
        }
    }

    private void initConfiguration() {
        // RecyclerViewConfigure
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(this, contactActivityRv);
        recyclerViewConfigure.linearVerticalLayout(false, 0, false, true);
        // SoulPermissionUtils
        soulPermissionUtils = new SoulPermissionUtils();
        // ContactExtractor
        contactExtractor = new ContactExtractor(this);
        // 展示
        contactBeanList = new ArrayList<>();
        contactAdapter = new ContactAdapter(this);
        // RecyclerViewScrollKit
        recyclerViewScrollKit = new RecyclerViewScrollKit();
    }

    private void setListener() {
        /*
          WaveSideBar
         */
        contactActivityWsb.setOnTouchLetterChangeListener(letter -> {
            // 该字母首现位
            int position = contactAdapter.getPositionForSection(letter.charAt(0));
            if (position != -1) {
                recyclerViewScrollKit.flatSlidToTargetPosition(contactActivityRv, position);
            }
        });
        /*
          联系人适配器
         */
        contactAdapter.setOnItemClickListener((view, contactBean) -> ToastUtils.shortShow(ContactActivity.this, contactBean.getName()));
    }

    /**
     * 检请权限
     */
    private void checkAndRequestPermission() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.READ_CONTACTS, soulPermissionUtils,
                true, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            ToastUtils.shortShow(ContactActivity.this, getString(R.string.noExternalStorage));
                        } else {
                            contactExtractor.extract();
                        }
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(ContactActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {

                    }
                });
    }

    /**
     * 首字母排序
     *
     * @param originalList 数据
     * @return 排序后数据
     */
    private List<ContactBean> acronymSorting(List<ContactBean> originalList) {
        if (originalList.isEmpty()) {
            return originalList;
        } else {
            List<ContactBean> sortList = new ArrayList<>(originalList.size());
            for (ContactBean contactBean : originalList) {
                // 首字母
                contactBean.setIndex(contactBean.getName());
                sortList.add(contactBean);
            }
            // 首字母排序
            Collections.sort(sortList, new AcronymSorting());
            return sortList;
        }
    }
}
