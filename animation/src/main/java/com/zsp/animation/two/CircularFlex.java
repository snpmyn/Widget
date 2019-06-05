package com.zsp.animation.two;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @decs: 圆形伸缩
 * @author: 郑少鹏
 * @date: 2019/5/6 17:49
 */
public class CircularFlex {
    private static final long PERFECT_MILLS = 618;
    private static final int MINI_RADIUS = 0;
    private static Long perfectMills;
    private static Long fullActivityPerfectMills;
    private static Integer colorOrImageRes;
    /**
     * view visible OnAnimatorDeployListener
     */
    private static OnAnimatorDeployListener showAnimatorDeployListener, hideAnimatorDeployListener;
    /**
     * activity OnAnimatorDeployListener
     */
    private static OnAnimatorDeployListener startAnimatorDeployListener, returnAnimatorDeployListener;

    private static long getPerfectMills() {
        if (perfectMills != null) {
            return perfectMills;
        } else {
            return PERFECT_MILLS;
        }
    }

    private static long getFullActivityMills() {
        if (fullActivityPerfectMills != null) {
            return fullActivityPerfectMills;
        } else {
            return PERFECT_MILLS;
        }
    }

    private static int getColorOrImageRes() {
        if (colorOrImageRes != null) {
            return colorOrImageRes;
        } else {
            return android.R.color.white;
        }
    }

    public interface OnAnimationEndListener {
        /**
         * 动画结束
         */
        void onAnimationEnd();
    }

    public interface OnAnimatorDeployListener {
        /**
         * 展开动画
         *
         * @param animator Animator
         */
        void deployAnimator(Animator animator);
    }

    public static class VisibleBuilder {
        private View mAnimView, mTriggerView;
        private Float mStartRadius, mEndRadius;
        private Point mTriggerPoint;
        private long mDurationMills = getPerfectMills();
        private boolean isShow;
        private OnAnimatorDeployListener mOnAnimatorDeployListener;
        private OnAnimationEndListener mOnAnimationEndListener;

        VisibleBuilder(View animView, boolean isShow) {
            mAnimView = animView;
            this.isShow = isShow;
            if (isShow) {
                mStartRadius = MINI_RADIUS + 0F;
            } else {
                mEndRadius = MINI_RADIUS + 0F;
            }
        }

        /**
         * Setting the trigger view.
         * If {@link VisibleBuilder#mTriggerPoint} is null, then will set the mTriggerView's center as trigger point.
         *
         * @param triggerView 触发视图
         * @return VisibleBuilder
         */
        public VisibleBuilder triggerView(View triggerView) {
            mTriggerView = triggerView;
            return this;
        }

        /**
         * Setting the trigger point.
         *
         * @param triggerPoint 触发点
         * @return VisibleBuilder
         */
        public VisibleBuilder triggerPoint(Point triggerPoint) {
            mTriggerPoint = triggerPoint;
            return this;
        }

        public VisibleBuilder startRadius(float startRadius) {
            mStartRadius = startRadius;
            return this;
        }

        public VisibleBuilder endRadius(float endRadius) {
            mEndRadius = endRadius;
            return this;
        }

        public VisibleBuilder duration(long durationMills) {
            mDurationMills = durationMills;
            return this;
        }

        public VisibleBuilder deployAnimator(OnAnimatorDeployListener onAnimatorDeployListener) {
            mOnAnimatorDeployListener = onAnimatorDeployListener;
            return this;
        }

        public void go() {
            go(null);
        }

        void go(OnAnimationEndListener onAnimationEndListener) {
            mOnAnimationEndListener = onAnimationEndListener;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                doOnEnd();
                return;
            }
            if (mTriggerPoint == null) {
                if (mTriggerView != null) {
                    int[] tvLocation = new int[2];
                    mTriggerView.getLocationInWindow(tvLocation);
                    final int tvCX = tvLocation[0] + mTriggerView.getWidth() / 2;
                    final int tvCY = tvLocation[1] + mTriggerView.getHeight() / 2;
                    int[] avLocation = new int[2];
                    mAnimView.getLocationInWindow(avLocation);
                    final int avLX = avLocation[0];
                    final int avTY = avLocation[1];
                    int triggerX = Math.max(avLX, tvCX);
                    triggerX = Math.min(triggerX, avLX + mAnimView.getWidth());
                    int triggerY = Math.max(avTY, tvCY);
                    triggerY = Math.min(triggerY, avTY + mAnimView.getHeight());
                    // 以上全绝对坐标
                    mTriggerPoint = new Point(triggerX - avLX, triggerY - avTY);
                } else {
                    int centerX = (mAnimView.getLeft() + mAnimView.getRight()) / 2;
                    int centerY = (mAnimView.getTop() + mAnimView.getBottom()) / 2;
                    mTriggerPoint = new Point(centerX, centerY);
                }
            }
            // 算水波中心点至mAnimView边最大距
            int maxW = Math.max(mTriggerPoint.x, mAnimView.getWidth() - mTriggerPoint.x);
            int maxH = Math.max(mTriggerPoint.y, mAnimView.getHeight() - mTriggerPoint.y);
            // 勾股定理和进一法
            int maxRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;
            if (isShow && mEndRadius == null) {
                mEndRadius = maxRadius + 0F;
            } else if (!isShow && mStartRadius == null) {
                mStartRadius = maxRadius + 0F;
            }
            Animator anim = ViewAnimationUtils.createCircularReveal(mAnimView, mTriggerPoint.x, mTriggerPoint.y, mStartRadius, mEndRadius);
            mAnimView.setVisibility(View.VISIBLE);
            anim.setDuration(mDurationMills);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    doOnEnd();
                }
            });
            if (mOnAnimatorDeployListener == null) {
                mOnAnimatorDeployListener = isShow ? showAnimatorDeployListener : hideAnimatorDeployListener;
            }
            if (mOnAnimatorDeployListener != null) {
                mOnAnimatorDeployListener.deployAnimator(anim);
            }
            anim.start();
        }

        private void doOnEnd() {
            if (isShow) {
                mAnimView.setVisibility(View.VISIBLE);
            } else {
                mAnimView.setVisibility(View.INVISIBLE);
            }
            if (mOnAnimationEndListener != null) {
                mOnAnimationEndListener.onAnimationEnd();
            }
        }
    }

    public static class FullActivityBuilder {
        private Activity mActivity;
        private Point mTriggerPoint;
        private float mStartRadius = MINI_RADIUS;
        private int mColorOrImageResource = getColorOrImageRes();
        private Drawable mDrawable;
        private Long mDurationMills;
        private OnAnimatorDeployListener mStartAnimatorDeployListener;
        private OnAnimatorDeployListener mReturnAnimatorDeployListener;
        private OnAnimationEndListener mOnAnimationEndListener;
        private int mEnterAnimation = android.R.anim.fade_in, mExitAnimation = android.R.anim.fade_out;

        FullActivityBuilder(Activity activity, View triggerView) {
            mActivity = activity;
            int[] location = new int[2];
            triggerView.getLocationInWindow(location);
            final int cx = location[0] + triggerView.getWidth() / 2;
            final int cy = location[1] + triggerView.getHeight() / 2;
            mTriggerPoint = new Point(cx, cy);
        }

        public FullActivityBuilder(Activity activity, Point triggerPoint) {
            mActivity = activity;
            mTriggerPoint = triggerPoint;
        }

        public FullActivityBuilder startRadius(float startRadius) {
            mStartRadius = startRadius;
            return this;
        }

        /**
         * Setting the ripple background drawableRes.
         * This will be override by {@link FullActivityBuilder#drawable(Drawable)}.
         *
         * @param colorOrImageRes 色或图资源
         * @return FullActivityBuilder
         */
        public FullActivityBuilder colorOrImageResource(int colorOrImageRes) {
            mColorOrImageResource = colorOrImageRes;
            return this;
        }

        /**
         * Setting the ripple background drawable.
         *
         * @param drawable 位图
         * @return FullActivityBuilder
         */
        public FullActivityBuilder drawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }

        public FullActivityBuilder duration(long durationMills) {
            mDurationMills = durationMills;
            return this;
        }

        public FullActivityBuilder overridePendingTransition(int enterAnim, int exitAnim) {
            mEnterAnimation = enterAnim;
            mExitAnimation = exitAnim;
            return this;
        }

        /**
         * Setting the start animation interceptor.
         *
         * @param onAnimatorDeployListener 展开动画监听
         * @return FullActivityBuilder
         */
        public FullActivityBuilder deployStartAnimator(OnAnimatorDeployListener onAnimatorDeployListener) {
            mStartAnimatorDeployListener = onAnimatorDeployListener;
            return this;
        }

        /**
         * Setting the return animation interceptor.
         *
         * @param onAnimatorDeployListener 展开动画监听
         * @return FullActivityBuilder
         */
        public FullActivityBuilder deployReturnAnimator(OnAnimatorDeployListener onAnimatorDeployListener) {
            mReturnAnimatorDeployListener = onAnimatorDeployListener;
            return this;
        }

        public void go(OnAnimationEndListener onAnimationEndListener) {
            mOnAnimationEndListener = onAnimationEndListener;
            // 小5.0无动画
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                doOnEnd();
                return;
            }
            final ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 优先用mDrawable
            if (mDrawable != null) {
                view.setImageDrawable(mDrawable);
            } else {
                view.setImageResource(mColorOrImageResource);
            }
            final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            int w = decorView.getWidth();
            int h = decorView.getHeight();
            decorView.addView(view, w, h);
            // 算中心点至View边最大距
            int maxW = Math.max(mTriggerPoint.x, w - mTriggerPoint.x);
            int maxH = Math.max(mTriggerPoint.y, h - mTriggerPoint.y);
            final int finalRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;
            Animator anim = ViewAnimationUtils.createCircularReveal(view, mTriggerPoint.x, mTriggerPoint.y, mStartRadius, finalRadius);
            int maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
            // 未设时长以PERFECT_MILLS为基准据水波扩散距算实际时长
            if (mDurationMills == null) {
                // 算实际边距与最大边距比率
                double rate = 1d * finalRadius / maxRadius;
                // 为让用户便于感触水波，速度应随最大边距变小而变慢，时长随最大边距变小而变小。故比率应于@rate与1间。
                mDurationMills = (long) (getFullActivityMills() * Math.sqrt(rate));
            }
            final long finalDuration = mDurationMills;
            // thisActivity.startActivity()会有所停顿，故进水波动画应比退水波动画时长短才可保视觉一致。
            anim.setDuration((long) (finalDuration * 0.9));
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    doOnEnd();
                    mActivity.overridePendingTransition(mEnterAnimation, mExitAnimation);
                    // 默显返至当前Activity动画，not support。
                    decorView.postDelayed(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            if (mActivity.isFinishing()) {
                                return;
                            }
                            Animator returnAnim = ViewAnimationUtils.createCircularReveal(view, mTriggerPoint.x, mTriggerPoint.y, finalRadius, mStartRadius);
                            returnAnim.setDuration(finalDuration);
                            returnAnim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (!mActivity.isFinishing() && view.getParent() != null) {
                                        ((ViewGroup) view.getParent()).removeView(view);
                                    }
                                }
                            });
                            if (mReturnAnimatorDeployListener == null) {
                                mReturnAnimatorDeployListener = returnAnimatorDeployListener;
                            }
                            if (mReturnAnimatorDeployListener != null) {
                                mReturnAnimatorDeployListener.deployAnimator(returnAnim);
                            }
                            returnAnim.start();
                        }
                    }, 1000);

                }
            });
            if (mStartAnimatorDeployListener == null) {
                mStartAnimatorDeployListener = startAnimatorDeployListener;
            }
            if (mStartAnimatorDeployListener != null) {
                mStartAnimatorDeployListener.deployAnimator(anim);
            }
            anim.start();
        }

        private void doOnEnd() {
            mOnAnimationEndListener.onAnimationEnd();
        }
    }
    /*上实现逻辑，下外部调法*/

    /**
     * 伸显
     *
     * @param animView 动画视图
     * @return VisibleBuilder
     */
    public static VisibleBuilder show(View animView) {
        return new VisibleBuilder(animView, true);
    }

    /**
     * 收隐
     *
     * @param animView 动画视图
     * @return VisibleBuilder
     */
    public static VisibleBuilder hide(View animView) {
        return new VisibleBuilder(animView, false);
    }

    /**
     * 以触发视图为触发点铺满整活动
     *
     * @param activity    活动
     * @param triggerView 触发视图
     * @return FullActivityBuilder
     */
    public static FullActivityBuilder fullActivity(Activity activity, View triggerView) {
        return new FullActivityBuilder(activity, triggerView);
    }

    /**
     * 初始默时长、充满活动默色或图资源
     *
     * @param perfectMills             perfectMills
     * @param fullActivityPerfectMills fullActivityPerfectMills
     * @param colorOrImageRes          色或图资源
     */
    public static void step(long perfectMills, long fullActivityPerfectMills, int colorOrImageRes) {
        CircularFlex.perfectMills = perfectMills;
        CircularFlex.fullActivityPerfectMills = fullActivityPerfectMills;
        CircularFlex.colorOrImageRes = colorOrImageRes;
    }

    /**
     * 初始默时长、充满活动默色或图资源
     *
     * @param showListener           显监听
     * @param hideListener           隐监听
     * @param startActivityListener  开始活动监听
     * @param returnActivityListener 返回活动监听
     */
    public static void stepDefaultDeployAnimators(
            OnAnimatorDeployListener showListener
            , OnAnimatorDeployListener hideListener
            , OnAnimatorDeployListener startActivityListener
            , OnAnimatorDeployListener returnActivityListener) {
        showAnimatorDeployListener = showListener;
        hideAnimatorDeployListener = hideListener;
        startAnimatorDeployListener = startActivityListener;
        returnAnimatorDeployListener = returnActivityListener;
    }
}
