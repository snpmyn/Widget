package example.onepartylibrary.progressbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zsp.library.progressbar.ProgressWheel;
import com.zsp.utilone.data.FloatUtils;
import com.zsp.widget.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import value.WidgetLibraryMagic;

/**
 * @decs: ProgressWheel页
 * @author: 郑少鹏
 * @date: 2019/8/12 16:56
 */
public class ProgressWheelActivity extends AppCompatActivity {
    @BindView(R.id.progressWheelActivitySpinnerProgressColorOptions)
    Spinner progressWheelActivitySpinnerProgressColorOptions;
    @BindView(R.id.progressWheelActivitySpinnerWheelColorOptions)
    Spinner progressWheelActivitySpinnerWheelColorOptions;
    @BindView(R.id.progressWheelActivityPw)
    ProgressWheel progressWheelActivityPw;
    @BindView(R.id.progressWheelActivitySpinnerProgressValueOptions)
    Spinner progressWheelActivitySpinnerProgressValueOptions;
    @BindView(R.id.progressWheelActivityPwInterpolated)
    ProgressWheel progressWheelActivityPwInterpolated;
    @BindView(R.id.progressWheelActivityTvInterpolatedValue)
    TextView progressWheelActivityTvInterpolatedValue;
    @BindView(R.id.progressWheelActivityPwLinear)
    ProgressWheel progressWheelActivityPwLinear;
    @BindView(R.id.progressWheelActivityTvLinearValue)
    TextView progressWheelActivityTvLinearValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wheel);
        ButterKnife.bind(this);
        progressValueOptionsSpinner();
        progressColorOptionsSpinner();
        wheelColorOptionsSpinner();
    }

    private void progressValueOptionsSpinner() {
        progressWheelActivitySpinnerProgressValueOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        progressWheelActivityPwLinear.setProgress(0.0f);
                        progressWheelActivityPwInterpolated.setProgress(0.0f);
                        progressWheelActivityPwInterpolated.setCallback(progress -> {
                            if (progress == 0) {
                                progressWheelActivityPwInterpolated.setProgress(1.0f);
                            } else if (FloatUtils.equal(progress, WidgetLibraryMagic.FLOAT_ONE_DOT_ZERO)) {
                                progressWheelActivityPwInterpolated.setProgress(0.0f);
                            }
                            progressWheelActivityTvInterpolatedValue.setText(String.format(Locale.CHINA, "%.2f", progress));
                        });
                        progressWheelActivityPwLinear.setCallback(progress -> {
                            if (progress == 0) {
                                progressWheelActivityPwLinear.setProgress(1.0f);
                            } else if (FloatUtils.equal(progress, WidgetLibraryMagic.FLOAT_ONE_DOT_ZERO)) {
                                progressWheelActivityPwLinear.setProgress(0.0f);
                            }
                            progressWheelActivityTvLinearValue.setText(String.format(Locale.CHINA, "%.2f", progress));
                        });
                        break;
                    case 1:
                        setProgress(0.0f);
                        break;
                    case 2:
                        setProgress(0.1f);
                        break;
                    case 3:
                        setProgress(0.25f);
                        break;
                    case 4:
                        setProgress(0.5f);
                        break;
                    case 5:
                        setProgress(0.75f);
                        break;
                    case 6:
                        setProgress(1.0f);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void progressColorOptionsSpinner() {
        final int defaultBarColor = progressWheelActivityPw.getBarColor();
        progressWheelActivitySpinnerProgressColorOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        progressWheelActivityPw.setBarColor(defaultBarColor);
                        progressWheelActivityPwInterpolated.setBarColor(defaultBarColor);
                        progressWheelActivityPwLinear.setBarColor(defaultBarColor);
                        break;
                    case 1:
                        progressWheelActivityPw.setBarColor(Color.RED);
                        progressWheelActivityPwInterpolated.setBarColor(Color.RED);
                        progressWheelActivityPwLinear.setBarColor(Color.RED);
                        break;
                    case 2:
                        progressWheelActivityPw.setBarColor(Color.MAGENTA);
                        progressWheelActivityPwInterpolated.setBarColor(Color.MAGENTA);
                        progressWheelActivityPwLinear.setBarColor(Color.MAGENTA);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void wheelColorOptionsSpinner() {
        final int defaultWheelColor = progressWheelActivityPw.getRimColor();
        progressWheelActivitySpinnerWheelColorOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        progressWheelActivityPw.setRimColor(defaultWheelColor);
                        progressWheelActivityPwInterpolated.setRimColor(defaultWheelColor);
                        progressWheelActivityPwLinear.setRimColor(defaultWheelColor);
                        break;
                    case 1:
                        progressWheelActivityPw.setRimColor(Color.LTGRAY);
                        progressWheelActivityPwInterpolated.setRimColor(Color.LTGRAY);
                        progressWheelActivityPwLinear.setRimColor(Color.LTGRAY);
                        break;
                    case 2:
                        progressWheelActivityPw.setRimColor(Color.GRAY);
                        progressWheelActivityPwInterpolated.setRimColor(Color.GRAY);
                        progressWheelActivityPwLinear.setRimColor(Color.GRAY);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.progressWheelActivityMbAbout)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.progressWheelActivityMbAbout) {
            about();
        }
    }

    /**
     * 关于
     */
    private void about() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.about)
                .setMessage(R.string.aboutHint)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    /**
     * 设进度
     *
     * @param progress 进度
     */
    private void setProgress(float progress) {
        progressWheelActivityPwLinear.setCallback(progress1 -> progressWheelActivityTvLinearValue.setText(String.format(Locale.CHINA, "%.2f", progress1)));
        progressWheelActivityPwLinear.setProgress(progress);
        progressWheelActivityPwInterpolated.setCallback(progress12 -> progressWheelActivityTvInterpolatedValue.setText(String.format(Locale.CHINA, "%.2f", progress12)));
        progressWheelActivityPwInterpolated.setProgress(progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.progress_wheel_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        // The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
