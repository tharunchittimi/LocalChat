package com.example.localchat.widgets

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.localchat.R


class CustomTextView : AppCompatTextView {
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

    /**
     * Loads a font from the given asset path
     *
     * @param customFontPath path in the assets folder to the font file
     */
    private fun initWithAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0)
        val customFontIndex = a.getInt(R.styleable.CustomViews_setFonts, 0)
        val fontPath = resources.getStringArray(R.array.FontNames)[customFontIndex]
        setCustomFont(fontPath)
        letterSpacing = 0.025F
        includeFontPadding = false
        a.recycle()
    }

    /**
     * Loads a font from the given asset path
     *
     * @param customFontPath path in the assets folder to the font file
     */
    private fun setCustomFont(customFontPath: String) {
        if (isInEditMode && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            //in the Android Studio stf_template preview, with SDK < 22, this throws an exception.  You'll
            // only see your custom font in the preview if you have the SDK set to 22 or above.
            return
        }
        val typeface = Typeface.createFromAsset(context.assets, "fonts/$customFontPath")
        setTypeface(typeface)
    }
}

