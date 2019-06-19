package example;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.dialog.SweetAlertDialog;
import com.zsp.library.dialog.listener.DialogValueListener;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 对话框一页
 * @author: 郑少鹏
 * @date: 2019/6/18 11:48
 */
public class DialogOneActivity extends AppCompatActivity implements DialogValueListener {
    /**
     * 对话框
     */
    private SweetAlertDialog showMaterialProgressSweetAlertDialog;
    private SweetAlertDialog basicMessageSweetAlertDialog;
    private SweetAlertDialog titleWithTextUnderSweetAlertDialog;
    private SweetAlertDialog showErrorMessageSweetAlertDialog;
    private SweetAlertDialog successMessageSweetAlertDialog;
    private SweetAlertDialog warningMessageWithConfirmButtonSweetAlertDialog;
    private SweetAlertDialog warningMessageWithCancelAndConfirmButtonSweetAlertDialog;
    private SweetAlertDialog messageWithCustomImageSweetAlertDialog;
    /**
     * 计数
     */
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_one);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.dialogOneActivityMbShowMaterialProgress, R.id.dialogOneActivityMbBasicMessage, R.id.dialogOneActivityMbTitleWithTextUnder, R.id.dialogOneActivityMbShowErrorMessage, R.id.dialogOneActivityMbSuccessMessage, R.id.dialogOneActivityMbWarningMessageWithConfirmButton, R.id.dialogOneActivityMbWarningMessageWithCancelAndConfirmButton, R.id.dialogOneActivityMbMessageWithCustomImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogOneActivityMbShowMaterialProgress:
                showMaterialProgress();
                break;
            case R.id.dialogOneActivityMbBasicMessage:
                basicMessage();
                break;
            case R.id.dialogOneActivityMbTitleWithTextUnder:
                titleWithTextUnder();
                break;
            case R.id.dialogOneActivityMbShowErrorMessage:
                showErrorMessage();
                break;
            case R.id.dialogOneActivityMbSuccessMessage:
                successMessage();
                break;
            case R.id.dialogOneActivityMbWarningMessageWithConfirmButton:
                warningMessageWithConfirmButton();
                break;
            case R.id.dialogOneActivityMbWarningMessageWithCancelAndConfirmButton:
                warningMessageWithCancelAndConfirmButton();
                break;
            case R.id.dialogOneActivityMbMessageWithCustomImage:
                messageWithCustomImage();
                break;
            default:
                break;
        }
    }

    private void showMaterialProgress() {
        showMaterialProgressSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
        showMaterialProgressSweetAlertDialog.setCancelable(false);
        showMaterialProgressSweetAlertDialog.setListener(this);
        showMaterialProgressSweetAlertDialog.show();
        new CountDownTimer(800 * 7, 800) {
            @Override
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i) {
                    case 0:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorPrimary));
                        break;
                    case 1:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorAccent));
                        break;
                    case 2:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorPrimary));
                        break;
                    case 3:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorAccent));
                        break;
                    case 4:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorPrimary));
                        break;
                    case 5:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorAccent));
                        break;
                    case 6:
                        showMaterialProgressSweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(DialogOneActivity.this, R.color.colorPrimary));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFinish() {
                i = -1;
                showMaterialProgressSweetAlertDialog.setTitleText("Success!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        }.start();
    }

    private void basicMessage() {
        // default title "Here's a message!"
        basicMessageSweetAlertDialog = new SweetAlertDialog(this);
        basicMessageSweetAlertDialog.setCancelable(true);
        basicMessageSweetAlertDialog.setCanceledOnTouchOutside(true);
        basicMessageSweetAlertDialog.setListener(this);
        basicMessageSweetAlertDialog.show();
    }

    private void titleWithTextUnder() {
        titleWithTextUnderSweetAlertDialog = new SweetAlertDialog(this).setContentText("It's pretty, isn't it?");
        titleWithTextUnderSweetAlertDialog.setListener(this);
        titleWithTextUnderSweetAlertDialog.show();
    }

    private void showErrorMessage() {
        showErrorMessageSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong!");
        showErrorMessageSweetAlertDialog.setListener(this);
        showErrorMessageSweetAlertDialog.show();
    }

    private void successMessage() {
        successMessageSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("You clicked the button!");
        successMessageSweetAlertDialog.setListener(this);
        successMessageSweetAlertDialog.show();
    }

    private void warningMessageWithConfirmButton() {
        warningMessageWithConfirmButtonSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(sDialog -> {
                    // reuse previous dialog instance
                    sDialog.setTitleText("Deleted!")
                            .setContentText("Your imaginary file has been deleted!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                });
        warningMessageWithConfirmButtonSweetAlertDialog.setListener(this);
        warningMessageWithConfirmButtonSweetAlertDialog.show();
    }

    private void warningMessageWithCancelAndConfirmButton() {
        warningMessageWithCancelAndConfirmButtonSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setCancelText("cancel")
                .setConfirmText("delete")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> {
                    // reuse previous dialog instance, keep widget user state, reset them if you need
                    sDialog.setTitleText("Cancelled!")
                            .setContentText("Your imaginary file is safe :)")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    // or you can new a SweetAlertDialog to show
                })
                .setConfirmClickListener(sDialog -> sDialog.setTitleText("Deleted!")
                        .setContentText("Your imaginary file has been deleted!")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE));
        warningMessageWithCancelAndConfirmButtonSweetAlertDialog.setListener(this);
        warningMessageWithCancelAndConfirmButtonSweetAlertDialog.show();
    }

    private void messageWithCustomImage() {
        messageWithCustomImageSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Sweet!")
                .setContentText("Here's a custom image.")
                .setCustomImage(R.drawable.custom);
        messageWithCustomImageSweetAlertDialog.setListener(this);
        messageWithCustomImageSweetAlertDialog.show();
    }

    /**
     * 关对话框
     *
     * @param flag flag
     */
    @Override
    public void dialogDismiss(int flag) {
        if (flag == 1) {
            if (showMaterialProgressSweetAlertDialog != null && showMaterialProgressSweetAlertDialog.isShowing()) {
                showMaterialProgressSweetAlertDialog.dismiss();
                showMaterialProgressSweetAlertDialog = null;
            } else if (basicMessageSweetAlertDialog != null && basicMessageSweetAlertDialog.isShowing()) {
                basicMessageSweetAlertDialog.dismiss();
                basicMessageSweetAlertDialog = null;
            } else if (titleWithTextUnderSweetAlertDialog != null && titleWithTextUnderSweetAlertDialog.isShowing()) {
                titleWithTextUnderSweetAlertDialog.dismiss();
                titleWithTextUnderSweetAlertDialog = null;
            } else if (showErrorMessageSweetAlertDialog != null && showErrorMessageSweetAlertDialog.isShowing()) {
                showErrorMessageSweetAlertDialog.dismiss();
                showErrorMessageSweetAlertDialog = null;
            } else if (successMessageSweetAlertDialog != null && successMessageSweetAlertDialog.isShowing()) {
                successMessageSweetAlertDialog.dismiss();
                successMessageSweetAlertDialog = null;
            } else if (warningMessageWithConfirmButtonSweetAlertDialog != null && warningMessageWithConfirmButtonSweetAlertDialog.isShowing()) {
                warningMessageWithConfirmButtonSweetAlertDialog.dismiss();
                warningMessageWithConfirmButtonSweetAlertDialog = null;
            } else if (warningMessageWithCancelAndConfirmButtonSweetAlertDialog != null && warningMessageWithCancelAndConfirmButtonSweetAlertDialog.isShowing()) {
                warningMessageWithCancelAndConfirmButtonSweetAlertDialog.dismiss();
                warningMessageWithCancelAndConfirmButtonSweetAlertDialog = null;
            } else if (messageWithCustomImageSweetAlertDialog != null && messageWithCustomImageSweetAlertDialog.isShowing()) {
                messageWithCustomImageSweetAlertDialog.dismiss();
                messageWithCustomImageSweetAlertDialog = null;
            }
        }
    }
}
