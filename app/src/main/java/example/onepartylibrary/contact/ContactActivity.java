package example.onepartylibrary.contact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.contact.bean.ContactBean;
import com.zsp.library.contact.extractor.ContactExtractor;
import com.zsp.library.recyclerview.configure.RecyclerViewConfigure;
import com.zsp.library.recyclerview.controller.RecyclerViewDisplayController;
import com.zsp.library.recyclerview.controller.RecyclerViewScrollController;
import com.zsp.library.sidebar.AcronymSorting;
import com.zsp.library.sidebar.WaveSideBar;
import com.zsp.library.status.listener.BaseStatusListener;
import com.zsp.library.status.manager.StatusManager;
import com.zsp.utilone.list.ListUtils;
import com.zsp.utilone.net.NetManager;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.utilone.view.ViewUtils;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.onepartylibrary.contact.adapter.ContactAdapter;
import value.WidgetConstant;
import value.WidgetMagic;

/**
 * @decs: 联系人页
 * @author: 郑少鹏
 * @date: 2019/7/22 17:37
 */
public class ContactActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.contactActivityRv)
    RecyclerView contactActivityRv;
    @SuppressLint("NonConstantResourceId")
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
     * 状态管理器
     */
    private StatusManager statusManager;
    /**
     * RecyclerViewScrollController
     */
    private RecyclerViewScrollController recyclerViewScrollController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initConfiguration();
        startLogic();
        setListener();
    }

    private void initConfiguration() {
        // RecyclerViewConfigure
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(this, contactActivityRv);
        recyclerViewConfigure.linearVerticalLayout(false, 0, false, false, true);
        // SoulPermissionUtils
        soulPermissionUtils = new SoulPermissionUtils();
        // ContactExtractor
        contactExtractor = new ContactExtractor(this);
        // 展示
        contactBeanList = new ArrayList<>();
        contactAdapter = new ContactAdapter(this);
        // 状态管理器
        statusManager = StatusManager.generate(contactActivityRv, new BaseStatusListener() {
            @Override
            public void setRetryEvent(View retryView) {
                View view = retryView.findViewById(R.id.statusRetryMb);
                view.setOnClickListener(v -> {
                    // 1连接失败、2加载失败（0无网络）
                    if (statusManager.status == 1 || statusManager.status == WidgetMagic.INT_TWO) {
                        contact();
                    } else {
                        startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), statusManager.requestCode);
                    }
                });
            }
        });
        // RecyclerViewScrollController
        recyclerViewScrollController = new RecyclerViewScrollController();
    }

    private void startLogic() {
        // ContactExtractor
        contactExtractor.setContractExtractorListener(contactBeans -> {
            if (null == contactBeans || contactBeans.size() == 0) {
                statusManager.showEmpty();
                return;
            }
            contactBeanList = acronymSorting(contactBeans);
            statusManager.showContent();
            contactAdapter.setContactData(contactBeanList);
            RecyclerViewDisplayController.display(contactActivityRv, contactAdapter);
            ViewUtils.showView(contactActivityWsb);
            ListUtils.saveListToData(this, contactBeanList, WidgetConstant.CONTACT);
        });
        // 联系人
        contact();
    }

    private void setListener() {
        /*
          WaveSideBar
         */
        contactActivityWsb.setOnTouchLetterChangeListener(letter -> {
            // 该字母首现位
            int position = contactAdapter.getPositionForSection(letter.charAt(0));
            if (position != -1) {
                recyclerViewScrollController.smoothScrollToTargetPosition(contactActivityRv, position);
            }
        });
        /*
          联系人适配器
         */
        contactAdapter.setOnItemClickListener((view, contactBean) -> ToastUtils.shortShow(ContactActivity.this, contactBean.getName()));
    }

    /**
     * 联系人
     */
    private void contact() {
        if (NetManager.isNetConnected(this)) {
            statusManager.showLoading();
            contactBeanList = (List<ContactBean>) ListUtils.getListFromData(this, WidgetConstant.CONTACT);
            if (null != contactBeanList && contactBeanList.size() > 0) {
                statusManager.showContent();
                contactAdapter.setContactData(contactBeanList);
                RecyclerViewDisplayController.display(contactActivityRv, contactAdapter);
                ViewUtils.showView(contactActivityWsb);
            } else {
                // 检请权限
                checkAndRequestPermission();
            }
        } else {
            statusManager.showRetry(0);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == statusManager.requestCode) {
            contact();
        }
    }
}
