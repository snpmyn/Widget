package example.textview;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsp.library.textview.AText;
import com.zsp.library.textview.FillTextView;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 填页
 * @author: 郑少鹏
 * @date: 2019/7/19 11:02
 */
public class FillActivity extends AppCompatActivity {
    @BindView(R.id.fillActivityFtv)
    FillTextView fillActivityFtv;
    @BindView(R.id.fillActivityTv)
    TextView fillActivityTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        ButterKnife.bind(this);
        execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fillActivityFtv.destroy();
    }

    private void execute() {
        fillActivityFtv.setEditTag("[", "]");
        fillActivityFtv.displayUnderline(true);
        fillActivityFtv.setUnderlineColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @OnClick({R.id.fillActivityMbFilledIn,
            R.id.fillActivityMbOriginalText,
            R.id.fillActivityMbTextPassage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 所填内容
            case R.id.fillActivityMbFilledIn:
                StringBuilder filledIn = new StringBuilder();
                for (String s : fillActivityFtv.getFillTexts()) {
                    filledIn.append(s);
                    filledIn.append("、");
                }
                fillActivityTv.setText(filledIn);
                break;
            // 原始内容
            case R.id.fillActivityMbOriginalText:
                fillActivityTv.setText(fillActivityFtv.getOriginalText());
                break;
            // 段落
            case R.id.fillActivityMbTextPassage:
                StringBuilder textPassage = new StringBuilder();
                for (AText aText : fillActivityFtv.getTextPassage()) {
                    textPassage.append(aText.getText());
                    textPassage.append("|").append(aText.isFill()).append("|");
                }
                fillActivityTv.setText(textPassage);
                break;
            default:
                break;
        }
    }
}
