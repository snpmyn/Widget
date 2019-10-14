package com.zsp.library.searchbox.three

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.zsp.library.R
import com.zsp.library.databinding.MultiSearchViewContainerBinding
import com.zsp.library.databinding.MultiSearchViewItemBinding
import com.zsp.library.searchbox.three.extensions.*
import com.zsp.library.searchbox.three.helper.SimpleTextWatcher
import com.zsp.utiltwo.keyboard.KeyboardUtils
import kotlinx.android.synthetic.main.multi_search_view_item.view.*

/**
 * @decs: MultiSearchContainerView
 * @author: 郑少鹏
 * @date: 2019/10/12 17:57
 */
class MultiSearchContainerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var searchTextStyle = 0
    private var searchViewWidth: Float = 0.0f
    private var viewWidth: Float = 0.0f
    private val binding: MultiSearchViewContainerBinding = inflate(R.layout.multi_search_view_container)
    private val sizeRemoveIcon = context.resources.getDimensionPixelSize(R.dimen.dp_18)
    private val defaultPadding = context.resources.getDimensionPixelSize(R.dimen.dp_16)
    private var isInSearchMode = false
    private var selectedTab: MultiSearchViewItemBinding? = null
    private var multiSearchViewListener: MultiSearchView.MultiSearchViewListener? = null
    private val searchEnterScrollAnimation = ValueAnimator.ofInt()
            .apply {
                duration = DEFAULT_ANIM_DURATION
                interpolator = LinearOutSlowInInterpolator()
                addUpdateListener {
                    binding.horizontalScrollView.smoothScrollTo(it.animatedValue as Int, 0)
                }
                endListener {
                    selectedTab?.let {
                        it.root.editTextSearch.requestFocus()
                        KeyboardUtils.showKeyboard(context)
                    }
                }
            }
    private val searchCompleteCollapseAnimator = ValueAnimator.ofInt().apply {
        duration = DEFAULT_ANIM_DURATION
        interpolator = LinearOutSlowInInterpolator()
        addUpdateListener { valueAnimator ->
            selectedTab?.let {
                val newViewLayoutParams = it.root.layoutParams
                newViewLayoutParams.width = valueAnimator.animatedValue as Int
                it.root.layoutParams = newViewLayoutParams
            }
        }
    }
    private val firstSearchTranslateAnimator = ValueAnimator.ofFloat().apply {
        duration = DEFAULT_ANIM_DURATION
        interpolator = LinearOutSlowInInterpolator()
        addUpdateListener { valueAnimator ->
            binding.horizontalScrollView.translationX = valueAnimator.animatedValue as Float
        }
        endListener {
            selectedTab?.let {
                it.root.editTextSearch.requestFocus()
                KeyboardUtils.showKeyboard(context)
            }
        }
    }
    private val indicatorAnimator = ValueAnimator.ofFloat().apply {
        duration = DEFAULT_ANIM_DURATION
        interpolator = LinearOutSlowInInterpolator()
        addUpdateListener { valueAnimator ->
            binding.viewIndicator.x = valueAnimator.animatedValue as Float
        }
    }

    init {
        binding.layoutItemContainer.layoutTransition = LayoutTransition()
                .apply {
                    disableTransitionType(LayoutTransition.APPEARING)
                    disableTransitionType(LayoutTransition.CHANGE_APPEARING)
                }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = measuredWidth.toFloat()
        searchViewWidth = viewWidth * WIDTH_RATIO
    }

    fun setSearchViewListener(multiSearchViewListener: MultiSearchView.MultiSearchViewListener) {
        this.multiSearchViewListener = multiSearchViewListener
    }

    fun search() {
        if (isInSearchMode) {
            return
        }
        selectedTab?.let { deselectTab(it) }
        isInSearchMode = true
        selectedTab = createNewSearchView()
        binding.layoutItemContainer.addView(selectedTab?.root)
        selectedTab?.root?.afterMeasured {
            val widthWithoutCurrentSearch = widthWithoutCurrentSearch()
            when {
                widthWithoutCurrentSearch == 0 -> {
                    firstSearchTranslateAnimator.setFloatValues(viewWidth, 0.0f)
                    firstSearchTranslateAnimator.start()
                }
                widthWithoutCurrentSearch < viewWidth -> {
                    val scrollEnterStartValue = 0
                    val scrollEnterEndValue = (binding.layoutItemContainer.measuredWidth - viewWidth).toInt()
                    searchEnterScrollAnimation.setIntValues(scrollEnterStartValue, scrollEnterEndValue)
                    searchEnterScrollAnimation.start()
                }
                else -> {
                    val scrollEnterStartValue = (widthWithoutCurrentSearch - viewWidth).toInt()
                    val scrollEnterEndValue = (widthWithoutCurrentSearch - viewWidth + searchViewWidth.toInt()).toInt()
                    searchEnterScrollAnimation.setIntValues(scrollEnterStartValue, scrollEnterEndValue)
                    searchEnterScrollAnimation.start()
                }
            }
        }
    }

    fun completeSearch() {
        if (isInSearchMode.not()) {
            return
        }
        isInSearchMode = false
        KeyboardUtils.hideKeyboard(context)
        selectedTab?.let {
            if (it.editTextSearch.text.length < 3) {
                removeTab(it)
                return
            }
        }
        selectedTab?.let {
            it.root.editTextSearch.isFocusable = false
            it.root.editTextSearch.isFocusableInTouchMode = false
            it.root.editTextSearch.clearFocus()
        }
        selectedTab?.let {
            val startWidthValue = it.root.measuredWidth
            val endWidthValue = it.root.editTextSearch.measuredWidth + sizeRemoveIcon + defaultPadding
            searchCompleteCollapseAnimator.setIntValues(startWidthValue, endWidthValue)
            searchCompleteCollapseAnimator.start()
            multiSearchViewListener?.onSearchComplete(
                    binding.layoutItemContainer.childCount - 1,
                    it.root.editTextSearch.text
            )
        }
        selectedTab?.let { selectTab(it) }
    }

    fun isInSearchMode() = isInSearchMode
    private fun createNewSearchView(): MultiSearchViewItemBinding {
        val viewItem: MultiSearchViewItemBinding = context.inflate(R.layout.multi_search_view_item)
        viewItem.editTextSearch.setStyle(context, searchTextStyle)
        viewItem.root.layoutParams = LinearLayout.LayoutParams(searchViewWidth.toInt(), WRAP_CONTENT)
        viewItem.root.setOnClickListener {
            if (viewItem != selectedTab) {
                multiSearchViewListener?.onItemSelected(
                        binding.layoutItemContainer.indexOfChild(viewItem.root),
                        viewItem.editTextSearch.text
                )
                changeSelectedTab(viewItem)
            }
        }
        viewItem.root.editTextSearch.setOnClickListener {
            if (viewItem != selectedTab) {
                multiSearchViewListener?.onItemSelected(
                        binding.layoutItemContainer.indexOfChild(viewItem.root),
                        viewItem.editTextSearch.text
                )
                changeSelectedTab(viewItem)
            }
        }
        viewItem.editTextSearch.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                s?.let { multiSearchViewListener?.onTextChanged(binding.layoutItemContainer.childCount - 1, it) }
            }
        })
        viewItem.root.imageViewRemove.setOnClickListener {
            selectedTab?.let { removeTab(it) }
        }
        viewItem.editTextSearch.onSearchAction(filter = isInSearchMode) { completeSearch() }
        return viewItem
    }

    private fun widthWithoutCurrentSearch(): Int {
        return when {
            binding.layoutItemContainer.childCount > 1 -> {
                var totalWith = 0
                for (i in 0 until binding.layoutItemContainer.childCount - 1) {
                    totalWith += binding.layoutItemContainer.getChildAt(i).measuredWidth
                }
                totalWith
            }
            else -> 0
        }
    }

    private fun removeTab(viewItemBinding: MultiSearchViewItemBinding) {
        val removeIndex = binding.layoutItemContainer.indexOfChild(viewItemBinding.root)
        val currentChildCount = binding.layoutItemContainer.childCount
        when {
            currentChildCount == 1 -> {
                binding.viewIndicator.visibility = View.INVISIBLE
                binding.layoutItemContainer.removeView(viewItemBinding.root)
            }
            removeIndex == currentChildCount - 1 -> {
                val newSelectedView = binding.layoutItemContainer.getChildAt(removeIndex - 1)
                val newSelectedViewBinding = DataBindingUtil.bind<MultiSearchViewItemBinding>(newSelectedView)
                selectTab(newSelectedViewBinding!!)
                binding.layoutItemContainer.removeView(viewItemBinding.root)
                selectedTab = newSelectedViewBinding
            }
            else -> {
                val newSelectedTabView = binding.layoutItemContainer.getChildAt(removeIndex + 1)
                val newSelectedViewBinding = DataBindingUtil.bind<MultiSearchViewItemBinding>(newSelectedTabView)
                selectTab(newSelectedViewBinding!!)
                binding.layoutItemContainer.removeView(viewItemBinding.root)
                selectedTab = newSelectedViewBinding
            }
        }
        multiSearchViewListener?.onSearchItemRemoved(removeIndex)
    }

    private fun selectTab(viewItemBinding: MultiSearchViewItemBinding) {
        val indicatorCurrentXPosition = binding.viewIndicator.x
        val indicatorTargetXPosition = viewItemBinding.root.x
        indicatorAnimator.setFloatValues(indicatorCurrentXPosition, indicatorTargetXPosition)
        indicatorAnimator.start()
        binding.viewIndicator.visibility = View.VISIBLE
        viewItemBinding.imageViewRemove.visibility = View.VISIBLE
        viewItemBinding.editTextSearch.alpha = 1.0f
    }

    private fun deselectTab(viewItemBinding: MultiSearchViewItemBinding) {
        binding.viewIndicator.visibility = View.INVISIBLE
        viewItemBinding.imageViewRemove.visibility = View.GONE
        viewItemBinding.editTextSearch.alpha = 0.5f
    }

    private fun changeSelectedTab(newSelectedTabItem: MultiSearchViewItemBinding) {
        selectedTab?.let { deselectTab(it) }
        selectedTab = newSelectedTabItem
        selectedTab?.let { selectTab(it) }
    }

    companion object {
        private const val WIDTH_RATIO = 0.85f
        private const val DEFAULT_ANIM_DURATION = 500L
    }
}