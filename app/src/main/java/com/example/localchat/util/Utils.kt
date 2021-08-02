package com.example.localchat.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        val requestsDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val responseDateFormat = SimpleDateFormat("yyyy-MM-dd")


        /**
         * Hide the keyboard
         */
        fun hideKeyboard(activity: Activity) {
            val view = activity.findViewById<View>(android.R.id.content)
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        }

        /**
         * Move cursor to last position
         */
        fun moveCursorToLast(editText: EditText) {
            editText.setSelection(editText.text.toString().length)
        }

        fun setColors(stringColor: String?): Int {
            return if (stringColor?.length ?: 0 > 0) {
                Color.parseColor(stringColor ?: "#000000")
            } else {
                Color.parseColor("#000000")
            }
        }

        fun getImagePlaceHolderLoading(context: Context): CircularProgressDrawable {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            return circularProgressDrawable
        }
        private val mRandom = Random()

        fun getRandomHSVColor(): Int {
            return Color.argb(
                255,
                mRandom.nextInt(256),
                mRandom.nextInt(256),
                mRandom.nextInt(256)
            )
        }
    }

}