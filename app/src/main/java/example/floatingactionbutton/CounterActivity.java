package example.floatingactionbutton;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.zsp.library.floatingactionbutton.CounterFloatingActionButton;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @decs: 计数页
 * @author: 郑少鹏
 * @date: 2019/7/19 14:10
 */
public class CounterActivity extends AppCompatActivity {
    @BindView(R.id.counterActivityMt)
    MaterialToolbar counterActivityMt;
    @BindView(R.id.counterActivityRg)
    RadioGroup counterActivityRg;
    @BindView(R.id.counterActivityCfab)
    CounterFloatingActionButton counterActivityCfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        ButterKnife.bind(this);
        stepUi();
        setListener();
    }

    private void stepUi() {
        setSupportActionBar(counterActivityMt);
    }

    private void setListener() {
        // RadioGroup
        counterActivityRg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.counterActivityMrbIncrease:
                    counterActivityCfab.setImageResource(R.drawable.ic_add);
                    counterActivityCfab.increase();
                    break;
                case R.id.counterActivityMrbDecrease:
                    counterActivityCfab.setImageResource(R.drawable.ic_remove);
                    counterActivityCfab.decrease();
                    break;
                case R.id.counterActivityMrbClear:
                    counterActivityCfab.setImageResource(0);
                    counterActivityCfab.clear();
                    break;
                default:
                    break;
            }
        });
        // CounterFloatingActionButton
        counterActivityCfab.setOnClickListener(view -> {
            if (counterActivityRg.getCheckedRadioButtonId() == R.id.counterActivityMrbIncrease) {
                counterActivityCfab.increase();
            } else {
                counterActivityCfab.decrease();
            }
        });
    }
}
