package com.zsp.library.animation.one;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsp.library.R;
import com.zsp.utilone.ViewUtils;

/**
 * @decs: 过渡视图
 * @author: 郑少鹏
 * @date: 2019/4/28 17:08
 */
public class TransitionView extends RelativeLayout {
    private View transitionViewViewSpread;
    private TextView transitionViewTvLoggingIn;
    private TextView transitionViewTvSuccess;
    private View transitionViewViewLine;
    /**
     * 动画结束监听
     */
    private OnAnimationEndListener onAnimationEndListener;
    /**
     * 结果（0登录中、1成功、2失败）
     */
    private int result;

    public TransitionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransitionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 允绘背景及执行onDraw()法
        /*setWillNotDraw(false);*/
        init();
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.transtion_view, this);
        transitionViewViewSpread = rootView.findViewById(R.id.transitionViewViewSpread);
        transitionViewTvLoggingIn = rootView.findViewById(R.id.transitionViewTvLoggingIn);
        transitionViewTvSuccess = rootView.findViewById(R.id.transitionViewTvSuccess);
        transitionViewViewLine = rootView.findViewById(R.id.transitionViewViewLine);
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        this.setVisibility(View.VISIBLE);
        transitionViewTvLoggingIn.setTranslationX(0);
        transitionViewTvLoggingIn.setVisibility(View.INVISIBLE);
        transitionViewTvSuccess.setVisibility(View.INVISIBLE);
        transitionViewViewLine.setVisibility(View.INVISIBLE);
        // 扩散动画
        TransitionViewHelper.spreadAnimation(transitionViewViewSpread, getScale(), new TransitionViewHelper.BaseSimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 开始登录中文本动画
                startLoggingInTextAnimation();
            }
        });
    }

    private void startLoggingInTextAnimation() {
        TransitionViewHelper.loggingInTextAnimation(transitionViewTvLoggingIn, new TransitionViewHelper.BaseSimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 开始线条动画
                startLineAnimation();
            }
        });
    }

    private void startLineAnimation() {
        TransitionViewHelper.lineProlongAnimation(transitionViewViewLine, new TransitionViewHelper.BaseSimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switch (result) {
                    case 0:
                        // 开始动画
                        startAnimation();
                        break;
                    case 1:
                        // 开始成功文本动画
                        startSuccessTextAnimation();
                        break;
                    case 2:
                        // 开始收缩动画
                        startShrinkAnimation();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void startSuccessTextAnimation() {
        TransitionViewHelper.successTextAnimation(transitionViewTvSuccess, transitionViewTvLoggingIn, new TransitionViewHelper.BaseSimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEndListener != null) {
                    onAnimationEndListener.animationEndSuccess();
                }
            }
        });
    }

    private void startShrinkAnimation() {
        ViewUtils.hideView(transitionViewTvLoggingIn, View.INVISIBLE);
        ViewUtils.hideView(transitionViewTvSuccess, View.INVISIBLE);
        ViewUtils.hideView(transitionViewViewLine, View.INVISIBLE);
        TransitionViewHelper.shrinkAnimation(transitionViewViewSpread, new TransitionViewHelper.BaseSimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEndListener != null) {
                    onAnimationEndListener.animationEndError();
                }
            }
        });
    }

    /**
     * 扩散动画最终放大比例
     *
     * @return 扩散动画最终放大比例
     */
    private float getScale() {
        // 原始扩散圆直径
        int originalWidth = transitionViewViewSpread.getMeasuredWidth();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 扩散圆最终扩散的圆半径
        float finalDiameter = (int) (Math.sqrt(width * width + height * height));
        // 圆未居中故加1
        return finalDiameter / originalWidth + 1;
    }

    public void setResult(int result) {
        this.result = result;
    }

    /**
     * 设动画结束监听
     *
     * @param onAnimationEndListener OnAnimationEndListener
     */
    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener) {
        this.onAnimationEndListener = onAnimationEndListener;
    }

    /**
     * 动画结束监听
     */
    public interface OnAnimationEndListener {
        /**
         * 动画成功结束
         */
        void animationEndSuccess();

        /**
         * 动画失败结束
         */
        void animationEndError();
    }
}
