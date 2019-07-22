package example.contract;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.contact.extractor.ContactExtractor;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 联系人页
 * @author: 郑少鹏
 * @date: 2019/7/22 17:37
 */
public class ContactActivity extends AppCompatActivity {
    private SoulPermissionUtils soulPermissionUtils;
    private ContactExtractor contactExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initConfiguration();
        setListener();
    }

    private void initConfiguration() {
        soulPermissionUtils = new SoulPermissionUtils();
        contactExtractor = new ContactExtractor(this);
    }

    private void setListener() {
        contactExtractor.setContractExtractorListener(contactBeans -> {
            if (null == Looper.myLooper()) {
                Looper.getMainLooper();
            }
            Looper.prepare();
            ToastUtils.shortShow(ContactActivity.this, contactBeans.toString());
            Looper.loop();
        });
    }

    @OnClick(R.id.contactActivityExtract)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.contactActivityExtract) {
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
    }
}
