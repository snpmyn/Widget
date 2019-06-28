package example.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.dialog.basedialog.BaseDialog;
import com.zsp.library.dialog.basedialog.BaseViewConvertListener;
import com.zsp.library.dialog.basedialog.CustomDialog;
import com.zsp.library.dialog.basedialog.ViewHolder;
import com.zsp.utilone.keyboard.KeyboardUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 对话框二页
 * @author: 郑少鹏
 * @date: 2019/6/28 14:47
 */
public class DialogTwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_two);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.dialogTwoActivityMbShare,
            R.id.dialogTwoActivityMbFriendSetting,
            R.id.dialogTwoActivityMbComment,
            R.id.dialogTwoActivityMbRedPacket,
            R.id.dialogTwoActivityMbLoad,
            R.id.dialogTwoActivityMbPaySuccess,
            R.id.dialogTwoActivityMbAccountFreezing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 分享
            case R.id.dialogTwoActivityMbShare:
                share();
                break;
            // 好友设置
            case R.id.dialogTwoActivityMbFriendSetting:
                friendSetting();
                break;
            // 评论
            case R.id.dialogTwoActivityMbComment:
                comment();
                break;
            // 红包
            case R.id.dialogTwoActivityMbRedPacket:
                redPacket();
                break;
            // 加载
            case R.id.dialogTwoActivityMbLoad:
                load();
                break;
            // 支付成功
            case R.id.dialogTwoActivityMbPaySuccess:
                paySuccess();
                break;
            // 账号冻结
            case R.id.dialogTwoActivityMbAccountFreezing:
                accountFreezing();
                break;
            default:
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
        CustomDialog.init()
                .setLayoutId(R.layout.base_dialog_share)
                .setConvertListener(new BaseViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {
                        holder.setOnClickListener(R.id.baseDialogDctvWechat, v -> ToastUtils.shortShow(DialogTwoActivity.this, "分享成功"));
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 好友设置
     */
    private void friendSetting() {
        CustomDialog.init()
                .setLayoutId(R.layout.base_dialog_friend_setting)
                .setConvertListener(new BaseViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {

                    }
                })
                .setShowBottom(true)
                .setHeight(300)
                .show(getSupportFragmentManager());
    }

    /**
     * 评论
     */
    private void comment() {
        CustomDialog.init()
                .setLayoutId(R.layout.base_dialog_comment)
                .setConvertListener(new BaseViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {
                        final EditText editText = holder.getView(R.id.baseDialogCommentEt);
                        editText.post(() -> KeyboardUtils.closeKeyboard(DialogTwoActivity.this, editText));
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 红包
     */
    private void redPacket() {
        CustomDialog.init()
                .setLayoutId(R.layout.base_dialog_red_packet)
                .setConvertListener(new BaseViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {
                        holder.setOnClickListener(R.id.baseDialogRedPacketClose, v -> dialog.dismiss());
                    }
                })
                .setWidth(200)
                .setOutCancel(false)
                .setAnimStyle(R.style.DefaultAnimation)
                .show(getSupportFragmentManager());
    }

    /**
     * 加载
     */
    private void load() {
        CustomDialog.init()
                .setLayoutId(R.layout.base_dialog_loading)
                .setWidth(100)
                .setHeight(100)
                .setDimAmount(0)
                .show(getSupportFragmentManager());
    }

    /**
     * 支付成功
     */
    private void paySuccess() {
        ConfirmDialog.newInstance(1)
                .setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    /**
     * 账号冻结
     */
    private void accountFreezing() {
        ConfirmDialog.newInstance(2)
                .setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public static class ConfirmDialog extends BaseDialog {
        private int type;

        static ConfirmDialog newInstance(int type) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            confirmDialog.setArguments(bundle);
            return confirmDialog;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle == null) {
                return;
            }
            type = bundle.getInt("type");
        }

        @Override
        public int intLayoutId() {
            return R.layout.base_dialog_confirm;
        }

        @Override
        public void convertView(ViewHolder holder, final BaseDialog dialog) {
            switch (type) {
                case 1:
                    holder.setText(R.id.baseDialogConfirmTvTitle, "提示");
                    holder.setText(R.id.baseDialogConfirmTvContent, "您已支付成功！");
                    break;
                case 2:
                    holder.setText(R.id.baseDialogConfirmTvTitle, "警告");
                    holder.setText(R.id.baseDialogConfirmTvContent, "您的账号已被冻结！");
                    break;
                default:
                    break;
            }
            holder.setOnClickListener(R.id.baseDialogConfirmTvCancel, v -> dialog.dismiss());
            holder.setOnClickListener(R.id.baseDialogConfirmTvEnsure, v -> dialog.dismiss());
        }
    }
}
