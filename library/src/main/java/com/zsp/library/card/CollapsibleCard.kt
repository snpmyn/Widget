package com.zsp.library.card

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.method.LinkMovementMethod
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.zsp.library.R

/**
 * @decs: CollapsibleCard
 * Collapsible card, description of which can be HTML.
 * In that case, the text specified as "cardDescription" needs to be wrapped with <![CDATA[ ... ]]>
 * @author: 郑少鹏
 * @date: 2019/9/2 14:33
 */
class CollapsibleCard @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var expanded = false
    private val collapsibleCardTvTitle: TextView
    private val collapsibleCardTvDescription: TextView
    private val collapsibleCardIv: ImageView
    private val collapsibleCardLlContainer: View
    private val toggle: Transition
    private val root: View
    private val cardTitle: String?

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleCard, 0, 0)
        cardTitle = arr.getString(R.styleable.CollapsibleCard_cardTitle)
        val cardDescription = arr.getString(R.styleable.CollapsibleCard_cardDescription)
        arr.recycle()
        root = LayoutInflater.from(context).inflate(R.layout.collapsible_card, this, true)
        collapsibleCardLlContainer = root.findViewById(R.id.collapsibleCardLlContainer)
        collapsibleCardTvTitle = root.findViewById<TextView>(R.id.collapsibleCardTvTitle).apply {
            text = cardTitle
        }
        setTitleContentDescription(cardTitle)
        collapsibleCardTvDescription = root.findViewById<TextView>(R.id.collapsibleCardTvDescription).apply {
            if (cardDescription != null) {
                text = HtmlCompat.fromHtml(cardDescription, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
            movementMethod = LinkMovementMethod.getInstance()
        }
        collapsibleCardIv = root.findViewById(R.id.collapsibleCardIv)
        toggle = TransitionInflater.from(context).inflateTransition(R.transition.collapsible_card_toggle)
        collapsibleCardLlContainer.setOnClickListener {
            toggleExpanded()
        }
    }

    private fun setTitleContentDescription(cardTitle: String?) {
        val res = resources
        collapsibleCardTvTitle.contentDescription = "$cardTitle, " + if (expanded) {
            res.getString(R.string.expanded)
        } else {
            res.getString(R.string.collapsed)
        }
    }

    private fun toggleExpanded() {
        expanded = !expanded
        toggle.duration = if (expanded) 300L else 200L
        TransitionManager.beginDelayedTransition(root.parent as ViewGroup, toggle)
        collapsibleCardTvDescription.visibility = if (expanded) View.VISIBLE else View.GONE
        collapsibleCardIv.rotationX = if (expanded) 180f else 0f
        setTitleContentDescription(cardTitle)
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.expanded = expanded
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            if (expanded != state.expanded) {
                toggleExpanded()
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {
        var expanded = false

        constructor(source: Parcel) : super(source) {
            expanded = source.readByte().toInt() != 0
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeByte((if (expanded) 1 else 0).toByte())
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
