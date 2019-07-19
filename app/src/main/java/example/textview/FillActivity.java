package example.textview;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

    private void execute() {
        fillActivityFtv.setEditTag("[", "]");
        fillActivityFtv.displayUnderline(true);
        fillActivityFtv.setUnderlineColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @OnClick(R.id.fillActivityMb)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.fillActivityMb) {
            StringBuilder content = new StringBuilder();
            for (String s : fillActivityFtv.getFillTexts()) {
                content.append(s);
                content.append("、");
            }
            fillActivityTv.setText(content);
        }
    }
}
