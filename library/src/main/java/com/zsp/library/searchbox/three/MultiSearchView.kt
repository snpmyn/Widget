package com.zsp.library.searchbox.three

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.zsp.library.R
import com.zsp.library.databinding.MultiSearchViewBinding
import com.zsp.library.searchbox.three.extensions.inflate

/**
 * @decs: MultiSearchView
 * @author: 郑少鹏
 * @date: 2019/10/12 17:56
 */
class MultiSearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        RelativeLayout(context, attrs, defStyleAttr) {
    interface MultiSearchViewListener {
        fun onTextChanged(index: Int, s: CharSequence)
        fun onSearchComplete(index: Int, s: CharSequence)
        fun onSearchItemRemoved(index: Int)
        fun onItemSelected(index: Int, s: CharSequence)
    }

    private val binding = inflate<MultiSearchViewBinding>(R.layout.multi_search_view)

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.MultiSearchView, defStyleAttr, defStyleAttr)
        val searchTextStyle = typedArray.getResourceId(R.styleable.MultiSearchView_searchTextStyle, 0)
        binding.searchViewContainer.apply {
            this.searchTextStyle = searchTextStyle
        }
        binding.imageViewSearch.setOnClickListener {
            if (binding.searchViewContainer.isInSearchMode().not()) {
                binding.searchViewContainer.search()
            } else {
                binding.searchViewContainer.completeSearch()
            }
        }
    }

    fun setSearchViewListener(multiSearchViewListener: MultiSearchViewListener) {
        binding.searchViewContainer.setSearchViewListener(multiSearchViewListener)
    }
}