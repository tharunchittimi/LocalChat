package com.example.localchat.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.localchat.R
import com.example.localchat.util.Utils


class MyCustomTextInputLayout : LinearLayout {
    private var roorLayout: ConstraintLayout? = null
    private var tvLabel: CustomTextView? = null
    private var editText: CustomEditText? = null
    private var viewUnderLine: View? = null
    private var tvError: CustomTextView? = null
    private var tilIcon: ImageView? = null
    private var ivToggleIcon: ImageView? = null
    private var labelFocusedColor: String? = null
    private var labelUnFocusedColor: String? = null
    private var lineFocusedColor: String? = null
    private var lineUnFocusedColor: String? = null
    private var hintText: String? = null

    constructor(context: Context) : super(context) {
        initWithAttrs(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!isInEditMode)
            initWithAttrs(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode)
            initWithAttrs(context, attrs, defStyleAttr)
    }

    /**
     * This method is used to set the Custom fonts
     */
    private fun initWithAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.inflate_my_custom_text_input_layout, this)

        roorLayout = rootView.findViewById(R.id.roorLayout)
        tvLabel = rootView.findViewById(R.id.tvLabel)
        editText = rootView.findViewById(R.id.editText)
        viewUnderLine = rootView.findViewById(R.id.ViewUnderLine)
        tvError = rootView.findViewById(R.id.tvError)
        tilIcon = rootView.findViewById(R.id.tilIcon)
        ivToggleIcon = rootView.findViewById(R.id.ivToggleIcon)
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyCustomTextInputLayout,
            defStyleAttr,
            0
        )
        val text = a.getString(R.styleable.MyCustomTextInputLayout_tilText)
        hintText = a.getString(R.styleable.MyCustomTextInputLayout_tilHintText)
        val errorText = a.getString(R.styleable.MyCustomTextInputLayout_tilErrorText)
        val inputType = a.getInt(
            R.styleable.MyCustomTextInputLayout_android_inputType,
            EditorInfo.TYPE_TEXT_VARIATION_NORMAL
        )
        val imeOptions = a.getInt(R.styleable.MyCustomTextInputLayout_android_imeOptions, 0)
        val maxLength = a.getInt(R.styleable.MyCustomTextInputLayout_android_maxLength, -1)
        val maxLines = a.getInt(R.styleable.MyCustomTextInputLayout_android_maxLines, -1)
        val textColor = a.getString(R.styleable.MyCustomTextInputLayout_tilTextColor)
        val hintColor = a.getString(R.styleable.MyCustomTextInputLayout_tilHintColor)
        val errorTextColor = a.getString(R.styleable.MyCustomTextInputLayout_tilErrorTextColor)
        labelFocusedColor = a.getString(R.styleable.MyCustomTextInputLayout_tilLabelFocusedColor)
        labelUnFocusedColor =
            a.getString(R.styleable.MyCustomTextInputLayout_tilLabelUnFocusedColor)
        lineFocusedColor = a.getString(R.styleable.MyCustomTextInputLayout_tilLineFocusColor)
        lineUnFocusedColor = a.getString(R.styleable.MyCustomTextInputLayout_tilLineUnFocusColor)
        if (maxLines != -1) {
            editText?.maxLines = maxLines
        }
        if (maxLength != -1) {
            editText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
        val iconType = a.getDrawable(R.styleable.MyCustomTextInputLayout_tilIconType)
        val toggleIcon =
            a.getBoolean(R.styleable.MyCustomTextInputLayout_tilVisibleToggleIcon, false)
        val tilIconn = a.getBoolean(R.styleable.MyCustomTextInputLayout_tilVisibleToggleIcon, false)

        setText(text)
        setHintText(hintText)
        showError(errorText)
        setTextColor(textColor)
        setErrorTextColor(errorTextColor)
        setHintColor(hintColor)
        setInputType(inputType)
        setImeOption(imeOptions)
        setIconType(iconType)
        showPasswordToggleIcon(toggleIcon)
        showTILIcon(tilIconn)
        a.recycle()
    }

    fun showTILIcon(isShow: Boolean?) {
        tilIcon?.visibility = if (isShow == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun showPasswordToggleIcon(toggleIcon: Boolean? = false) {
        ivToggleIcon?.visibility = if (toggleIcon == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setToggleIconClickListener() {
        ivToggleIcon?.setOnClickListener {
            if (editText?.transformationMethod == PasswordTransformationMethod.getInstance()) {
                showPasswordWithToggle()
            } else {
                hidePasswordWithToggle()
            }
        }
    }

    private fun hidePasswordWithToggle() {
        ivToggleIcon?.setImageResource(R.drawable.ic_visibility_off_black_24dp)
        editText?.let {
            it.transformationMethod = PasswordTransformationMethod.getInstance()
            Utils.moveCursorToLast(it)
        }
    }

    private fun showPasswordWithToggle() {
        ivToggleIcon?.setImageResource(R.drawable.ic_visibility_black_24dp)
        editText?.let {
            it.transformationMethod = HideReturnsTransformationMethod.getInstance()
            Utils.moveCursorToLast(it)
        }
    }

    fun setIconType(icon: Drawable?) {
        if (icon != null) {
            tilIcon?.setImageDrawable(icon)
        }
    }

    private fun setImeOption(imeOptions: Int) {
        if (imeOptions != 0) {
            editText?.imeOptions = imeOptions
        }
    }

    private fun setInputType(inputType: Int) {
        if (inputType != -1) {
            editText?.inputType = inputType

        }
    }

    fun setHintColor(hintColor: String?) {
        hintColor?.let {
            editText?.setHintTextColor(Utils.setColors(it))
        }
    }

    fun setLabelColors(labelFocusedColor: String?, labelUnFocusedColor: String?) {
        this.labelFocusedColor = labelFocusedColor
        this.labelUnFocusedColor = labelUnFocusedColor

    }

    fun setLineColors(lineFocusedColor: String?, lineUnFocusedColor: String?) {
        this.lineFocusedColor = lineFocusedColor
        this.lineUnFocusedColor = lineUnFocusedColor

    }

    private fun updateHintColor(labelUnFocusedColor: String?) {
        labelUnFocusedColor?.let {
            tvLabel?.setTextColor(Utils.setColors(it))
        }
    }

    private fun updateLineColor(lineUnFocusedColor: String?) {
        lineUnFocusedColor?.let {
            viewUnderLine?.setBackgroundColor(Utils.setColors(it))
        }
    }

    fun setErrorTextColor(errorTextColor: String?) {
        errorTextColor?.let {
            tvError?.setTextColor(Utils.setColors(it))
        }
    }

    fun setTextColor(textColor: String?) {
        textColor?.let {
            editText?.setTextColor(Utils.setColors(it))
        }
    }

    fun setHintText(hintText: String?) {
        this.hintText = hintText
        editText?.hint = hintText
        tvLabel?.text = hintText
    }

    fun setText(text: String?) {
        editText?.setText(text)
        showHint(false)
    }

    fun init() {
        updateHintColor(labelUnFocusedColor)
        updateLineColor(lineUnFocusedColor)
        setCustomFont("HelveticaNeue-Medium.ttf")
        applyCursorColor()
        setHighLightColor("#bfbfbf")
        setToggleIconClickListener()
        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                showError(null)

            }

        })

        editText?.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            showHint(hasFocus)
            if (hasFocus) {
                updateHintColor(labelFocusedColor)
                updateLineColor(lineFocusedColor)
                editText?.hint = ""
            } else {
                updateHintColor(labelUnFocusedColor)
                updateLineColor(lineUnFocusedColor)
                setHintText(hintText)
                showHint(false)
            }
        }
    }

    private fun showHint(show: Boolean) {
        if (show) {
            tvLabel?.visibility = View.VISIBLE
        } else {
            val value = editText?.text?.toString()
            tvLabel?.visibility = if ((value?.length ?: 0 > 0) || (editText?.isFocused == true)) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    fun showError(error: String?, anim: Boolean = false, needFocus: Boolean = false) {
        tvError?.text = error
        tvError?.visibility = if (error?.length ?: 0 > 0) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
//        if (error != null && anim) {
//            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
//            this.startAnimation(shake)
//        }
        if (needFocus) {
            editText?.requestFocus()
        }
    }

    fun getEditText(): CustomEditText? {
        return editText
    }

    private fun setCustomFont(customFontPath: String) {
        if (isInEditMode && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            //in the Android Studio stf_template preview, with SDK < 22, this throws an exception.  You'll
            // only see your custom font in the preview if you have the SDK set to 22 or above.
            return
        }
        val typeface = Typeface.createFromAsset(context.assets, "fonts/$customFontPath")
        editText?.typeface = typeface
    }

    private fun applyCursorColor() {
        editText?.setCursorColor(context, Color.parseColor(lineFocusedColor))
    }

    private fun EditText.setCursorColor(context: Context, color: Int) {
        val editText = this
        val shapeDrawable = ContextCompat.getDrawable(context, R.drawable.cursor)
        val gradientDrawable: GradientDrawable = shapeDrawable as GradientDrawable
        gradientDrawable.setColor(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textCursorDrawable = shapeDrawable
        } else {
            try {
                // get the cursor resource id
                TextView::class.java.getDeclaredField("mCursorDrawableRes").apply {
                    isAccessible = true
                    val drawableResId: Int = getInt(editText)

                    // get the editor
                    val editorField = TextView::class.java
                        .getDeclaredField("mEditor")
                    editorField.isAccessible = true
                    val editor: Any = editorField.get(editText)

                    // get the drawable and set a color filter
                    val drawable: Drawable? = ContextCompat
                        .getDrawable(editText.context, drawableResId)
                    drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)

                    // set the drawables
                    editor.javaClass.getDeclaredField("mCursorDrawable").apply {
                        isAccessible = true
                        set(editor, arrayOf(drawable, drawable))
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun setHighLightColor(color: String) {
        editText?.highlightColor = Color.parseColor(color)
    }

}
