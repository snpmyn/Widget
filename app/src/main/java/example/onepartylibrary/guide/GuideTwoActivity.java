package example.onepartylibrary.guide;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.zsp.library.guide.materialintroview.animation.MaterialIntroListener;
import com.zsp.library.guide.materialintroview.kit.MaterialIntroViewKit;
import com.zsp.library.guide.materialintroview.shape.Focus;
import com.zsp.library.guide.materialintroview.shape.FocusGravity;
import com.zsp.library.guide.materialintroview.shape.ShapeType;
import com.zsp.widget.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 引导二页
 * @author: 郑少鹏
 * @date: 2019/9/24 11:30
 */
public class GuideTwoActivity extends AppCompatActivity implements MaterialIntroListener {
    @BindView(R.id.guideTwoActivityMt)
    MaterialToolbar guideTwoActivityMt;
    @BindView(R.id.guideTwoActivityMcv)
    MaterialCardView guideTwoActivityMcv;
    @BindView(R.id.guideTwoActivityMbButtonOne)
    MaterialButton guideTwoActivityMbButtonOne;
    @BindView(R.id.guideTwoActivityMbButtonTwo)
    MaterialButton guideTwoActivityMbButtonTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_two);
        ButterKnife.bind(this);
        stepUi();
        startLogic();
        setListener();
    }

    private void stepUi() {
        guideTwoActivityMt.inflateMenu(R.menu.search_menu);
    }

    private void startLogic() {
        MaterialIntroViewKit.showMaterialIntroView(this,
                guideTwoActivityMcv,
                String.valueOf(guideTwoActivityMcv.getId()),
                "This is MaterialCardView!",
                ContextCompat.getColor(this, R.color.fontHint),
                FocusGravity.CENTER,
                Focus.ALL,
                ShapeType.RECTANGLE,
                false,
                true,
                this);
    }

    private void setListener() {
        guideTwoActivityMt.setNavigationOnClickListener(v -> finish());
    }

    @OnClick(R.id.guideTwoActivityMbResetting)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.guideTwoActivityMbResetting) {
            MaterialIntroViewKit.resetAll(this);
        }
    }

    /**
     * 用户点
     *
     * @param materialIntroViewId String
     */
    @Override
    public void onUserClick(String materialIntroViewId) {
        if (materialIntroViewId.equals(String.valueOf(guideTwoActivityMcv.getId()))) {
            MaterialIntroViewKit.showMaterialIntroView(this,
                    guideTwoActivityMbButtonOne,
                    String.valueOf(guideTwoActivityMbButtonOne.getId()),
                    "This is MaterialButton!",
                    ContextCompat.getColor(this, R.color.fontHint),
                    FocusGravity.LEFT,
                    Focus.NORMAL,
                    ShapeType.CIRCLE,
                    false,
                    true,
                    this);
        } else if (materialIntroViewId.equals(String.valueOf(guideTwoActivityMbButtonOne.getId()))) {
            MaterialIntroViewKit.showMaterialIntroView(this,
                    guideTwoActivityMbButtonTwo,
                    String.valueOf(guideTwoActivityMbButtonTwo.getId()),
                    "This is MaterialButton!",
                    ContextCompat.getColor(this, R.color.fontHint),
                    FocusGravity.RIGHT,
                    Focus.MINIMUM,
                    ShapeType.CIRCLE,
                    false,
                    true,
                    this);
        } else if (materialIntroViewId.equals(String.valueOf(guideTwoActivityMbButtonTwo.getId()))) {
            MaterialIntroViewKit.showMaterialIntroView(this,
                    guideTwoActivityMt.getChildAt(0),
                    String.valueOf(guideTwoActivityMt.getChildAt(0).getId()),
                    "This is MaterialButton!",
                    ContextCompat.getColor(this, R.color.fontHint),
                    FocusGravity.CENTER,
                    Focus.MINIMUM,
                    ShapeType.CIRCLE,
                    false,
                    true,
                    this);
        } else if (materialIntroViewId.equals(String.valueOf(guideTwoActivityMt.getChildAt(0).getId()))) {
            View view = findViewById(R.id.pharmaceuticalKnowledgeListActivityMenuSearch);
            MaterialIntroViewKit.showMaterialIntroView(this,
                    view,
                    String.valueOf(view.getId()),
                    "This is MaterialButton!",
                    ContextCompat.getColor(this, R.color.fontHint),
                    FocusGravity.CENTER,
                    Focus.MINIMUM,
                    ShapeType.CIRCLE,
                    false,
                    true,
                    this);
        }
    }
}
