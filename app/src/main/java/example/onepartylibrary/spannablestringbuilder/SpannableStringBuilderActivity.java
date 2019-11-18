package example.onepartylibrary.spannablestringbuilder;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.spannablestringbuilder.SpannableStringBuilderCreator;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: SpannableStringBuilder页
 * @author: 郑少鹏
 * @date: 2019/6/24 17:03
 */
public class SpannableStringBuilderActivity extends AppCompatActivity {
    @BindView(R.id.spannableStringBuilderActivityTv)
    TextView spannableStringBuilderActivityTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string_builder);
        ButterKnife.bind(this);
        execute();
    }

    private void execute() {
        String source = "嗨，大家好！我是测试内容。";
        SpannableStringBuilder spannableStringBuilder =
                SpannableStringBuilderCreator.with(source)
                        .foregroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary), 7, source.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        .backgroundColorSpan(ContextCompat.getColor(this, R.color.gray), 7, source.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        .relativeSizeSpan(1.5f, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .strikethroughSpan(0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .underlineSpan(0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .superscriptSpan(1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .subscriptSpan(2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .bold(3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .italic(3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .imageSpan(ContextCompat.getDrawable(this, R.drawable.custom),
                                0, 0, spannableStringBuilderActivityTv.getLineHeight(), spannableStringBuilderActivityTv.getLineHeight(), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        .clickableSpan(() -> ToastUtils.shortShow(SpannableStringBuilderActivity.this, "点击"), 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        .urlSpan("http://www.google.com/", 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE).build();
        spannableStringBuilderActivityTv.setMovementMethod(LinkMovementMethod.getInstance());
        spannableStringBuilderActivityTv.setText(spannableStringBuilder);
    }
}
