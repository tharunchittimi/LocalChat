package com.example.localchat.base

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.localchat.R
import com.example.localchat.base.room.entities.UserEntity


abstract class BaseActivity<T : ViewDataBinding, N : BaseNavigator, V : BaseViewModel<N>> :
    AppCompatActivity(),
    BaseNavigator {

    private var dialog: Dialog? = null
    var mViewDataBinding: T? = null
    var mViewModel: V? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        initViews(savedInstanceState)
    }


    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getContentView())
        mViewDataBinding?.lifecycleOwner = this
        this.mViewModel = ViewModelProvider(this)[setViewModelClass()]
        mViewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding?.executePendingBindings()
        mViewModel?.navigator = getNavigator()
    }

    /**
     * Return layout id for each screen
     */
    abstract fun getContentView(): Int

    /**
     * Assign view model for appropriate screen
     */

    abstract fun setViewModelClass(): Class<V>


    /**
     * Assign navigator for appropriate screen
     */
    abstract fun getNavigator(): N


    /**
     * Assign binding variable from BR
     */
    abstract fun getBindingVariable(): Int

    abstract fun initViews(savedInstanceState: Bundle?)


    private fun initializeLoadingDialog() {
        if (dialog != null) {
            return
        }
        dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.custom_loading_gif)
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            val lp = window!!.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            window?.attributes = lp
            window?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this@BaseActivity,
                    R.drawable.gradient_for_progress_bar_bg
                )
            )
        }
    }


    fun showMessage(message: String?) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun startLoading() {
        initializeLoadingDialog()
        if (dialog?.isShowing != true) {
            dialog?.show()
        }
    }

    override fun finishLoading() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }


    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        var handled = false
        for (f in fragmentList) {
            if (f is BaseFragment<*, *, *>) {
                handled = f.onBackPressed()

                if (handled) {
                    break
                }
            }
        }

        if (!handled) {
            super.onBackPressed()
        }
    }

    fun showOrHideStatusBar(isStatusBarVisible: Boolean) {
        window.apply {
            if (isStatusBarVisible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.hide(WindowInsets.Type.statusBars())
                }
            } else {
                setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
    }


    fun isNetworkConnected() =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

    fun getCurrentLoggedInUserData(): UserEntity?{
        return mViewModel?.appDataManager?.getCurrentUserEntity()
    }


}