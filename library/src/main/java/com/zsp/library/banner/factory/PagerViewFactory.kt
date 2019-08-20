package com.zsp.library.banner.factory

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zsp.library.R
import com.zsp.library.banner.IBannerViewInstance
import com.zsp.library.banner.IPagerViewFactory
import com.zsp.library.banner.IPagerViewInstance
import com.zsp.library.banner.pager.PagerRecyclerView

/**
 * @decs: PagerView工厂
 * @author: 郑少鹏
 * @date: 2019/8/19 16:08
 */
internal class PagerViewFactory(
        private val bannerView: IBannerViewInstance,
        private val intervalUseViewPager: Boolean = false) : IPagerViewFactory {
    /**
     * 据参创对应PagerView实例
     */
    override fun getPagerView(): IPagerViewInstance {
        return if (bannerView.isSmoothMode()) {
            casePagerRecycler(true)
        } else {
            if (intervalUseViewPager) {
                // 此处可据需用ViewPager作底层实现
                throw IllegalStateException("此处未用ViewPager作底层实现")
            } else {
                casePagerRecycler(false)
            }
        }
    }

    /**
     * 处理PagerRecyclerView
     */
    private fun casePagerRecycler(isSmoothMode: Boolean): IPagerViewInstance {
        val recyclerView = PagerRecyclerView(bannerView.getContext())
        recyclerView.layoutManager = LinearLayoutManager(bannerView.getContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun getItemCount(): Int {
                return Int.MAX_VALUE
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                if (!isActivityDestroyed(holder.itemView.context)) {
                    val realPos = position % bannerView.getCount()
                    bannerView.onBindView(holder.itemView.findViewById(R.id.realItem), realPos)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemWrapper = LayoutInflater.from(parent.context).inflate(
                        R.layout.banner_item_wrapper,
                        parent,
                        false
                ) as RelativeLayout
                // 处理ItemWrapperView宽
                itemWrapper.layoutParams.width = bannerView.getItemViewWidth() + bannerView.getItemViewMargin()
                // 外部实际ItemView
                val itemView = bannerView.getItemView(parent.context)
                itemView.id = R.id.realItem
                val ivParams = RelativeLayout.LayoutParams(
                        bannerView.getItemViewWidth(),
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
                ivParams.addRule(bannerView.getItemViewAlign())
                // 添ItemView至Wrapper
                itemWrapper.addView(itemView, ivParams)
                return object : RecyclerView.ViewHolder(itemWrapper) {}
            }
        }
        // 初始位
        recyclerView.scrollToPosition(bannerView.getCount() * 100)
        recyclerView.setSmoothMode(isSmoothMode)
        return recyclerView
    }

    private fun isActivityDestroyed(context: Context?): Boolean {
        if (context == null) return true
        if (context !is Activity) {
            throw IllegalStateException("上下文对应Activity实例")
        }
        if (context.isFinishing || context.isDestroyed) {
            return true
        }
        return false
    }
}
