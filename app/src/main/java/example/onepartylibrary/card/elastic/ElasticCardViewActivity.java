package example.onepartylibrary.card.elastic;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.zsp.library.card.elastic.ElasticCardView;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 弹性卡片视图页
 * @author: 郑少鹏
 * @date: 2019/10/29 17:29
 */
public class ElasticCardViewActivity extends AppCompatActivity {
    @BindView(R.id.elasticCardViewActivityEcv)
    ElasticCardView elasticCardViewActivityEcv;
    @BindView(R.id.elasticCardViewActivityTv)
    TextView elasticCardViewActivityTv;
    @BindView(R.id.elasticCardViewActivityAcsb)
    AppCompatSeekBar elasticCardViewActivityAcsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elastic_card_view);
        ButterKnife.bind(this);
        stepUi();
        setListener();
    }

    private void stepUi() {
        elasticCardViewActivityEcv.setDebugPathEnabled(true);
    }

    private void setListener() {
        elasticCardViewActivityAcsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                elasticCardViewActivityEcv.setFlexibility(i / 10.0f + 1.0f);
                elasticCardViewActivityTv.setText(String.format(getString(R.string.flexibility), elasticCardViewActivityEcv.getFlexibility()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        elasticCardViewActivityAcsb.setProgress(40);
    }
}
