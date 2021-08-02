package com.example.localchat.widgets

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.localchat.R

class MutliLineEditText : AppCompatEditText {

    val TAG = MutliLineEditText::class.java.simpleName

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode)
            initWithAttrs(context, attrs, 0)

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode)
            initWithAttrs(context, attrs, defStyleAttr)
    }

    private fun initWithAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LREditText, defStyleAttr, 0)
        val customFontIndex = a.getInt(R.styleable.LREditText_setEditTextFont, -1)
        if (customFontIndex != -1) {
            val fontPath = resources.getStringArray(R.array.FontNames)[customFontIndex]
            setCustomFont(fontPath)
        }
        a.recycle()
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP ->
                        view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return false
            }
        })
    }

    /**
     * Loads a font from the given asset path
     *
     * @param customFontPath path in the assets folder to the font file
     */

    private fun setCustomFont(customFontPath: String) {
        if (isInEditMode && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return
        }
        val typeface = Typeface.createFromAsset(context.assets, "fonts/$customFontPath")
        setTypeface(typeface)
    }


}