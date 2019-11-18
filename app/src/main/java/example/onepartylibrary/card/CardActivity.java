package example.onepartylibrary.card;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.utilone.intent.IntentUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import example.onepartylibrary.card.collapsible.CollapsibleCardActivity;
import example.onepartylibrary.card.elastic.ElasticCardViewActivity;

/**
 * @decs: 卡片页
 * @author: 郑少鹏
 * @date: 2019/9/2 15:17
 */
public class CardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cardActivityMbCollapsibleCard, R.id.cardActivityMbElasticCardView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 折叠卡片
            case R.id.cardActivityMbCollapsibleCard:
                IntentUtils.jumpNoBundle(this, CollapsibleCardActivity.class);
                break;
            // 弹性卡片视图
            case R.id.cardActivityMbElasticCardView:
                IntentUtils.jumpNoBundle(this, ElasticCardViewActivity.class);
                break;
            default:
                break;
        }
    }
}
