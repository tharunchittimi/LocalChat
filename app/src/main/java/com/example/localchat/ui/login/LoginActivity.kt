package com.example.localchat.ui.login

import android.content.Intent
import android.os.Bundle
import com.example.localchat.BR
import com.example.localchat.R
import com.example.localchat.base.BaseActivity
import com.example.localchat.databinding.ActivityLoginBinding
import com.example.localchat.ui.home.chat.ChatHomeActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginView, LoginViewModel>(), LoginView {

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    override fun setViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getNavigator(): LoginView {
        return this
    }

    override fun getBindingVariable(): Int {
        return BR.loginViewModel
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setUpToolBar()
        setUpTilFields()
        setViewClickListeners()
    }

    private fun setViewClickListeners() {
        mViewDataBinding?.btnSignIn?.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        mViewDataBinding?.let {
            val emailOrMobile =
                it.txtInputSignInEmail.getEditText()?.text?.toString()?.trim()
            val password =
                it.txtInputSignInPassword.getEditText()?.text?.toString()?.trim()

            if (emailOrMobile?.isEmpty() == true) {
                it.txtInputSignInEmail.showError(
                    "Phone Number is required",
                    true,
                    true
                )
            } else if (emailOrMobile?.length ?: 0 < 9) {
                it.txtInputSignInEmail.showError(
                    "Please enter valid Phone Number",
                    true,
                    true
                )
            } else if (password?.length ?: 0 < 1) {
                it.txtInputSignInPassword.showError(
                    "Password is required",
                    true,
                    true
                )
            } else {
                mViewModel?.appDataManager?.getDatabaseHelper()?.getUserDataBaseDao()
                    ?.checkUserByPhoneNumber(emailOrMobile, password).let { userEntity ->
                    if (userEntity != null) {
                        mViewModel?.appDataManager?.setCurrentUserEntity(userEntity)
                        startActivity(Intent(this, ChatHomeActivity::class.java))
                        finish()
                    } else {
                        showMessage("Sorry!, Invalid credentials")
                    }
                }

            }
        }
    }

    private fun setUpTilFields() {
        val headingColor = "#5b5b5b"
        val labelColor = "#5b5b5b"
        val textColor = "#000000"
        mViewDataBinding?.let {
            it.txtInputSignInEmail.apply {
                setHintText("Phone Number")
                setLabelColors(headingColor, labelColor)
                setLineColors(headingColor, headingColor)
                setTextColor(textColor)
                setHintColor(labelColor)
                showTILIcon(false)
                init()
            }

            it.txtInputSignInPassword.apply {
                setHintText("Password")
                setLabelColors(headingColor, labelColor)
                setLineColors(headingColor, headingColor)
                setTextColor(textColor)
                setHintColor(labelColor)
                showTILIcon(false)
                showPasswordToggleIcon(true)
                init()
            }
        }
    }

    private fun setUpToolBar() {
        mViewDataBinding?.toolBarLogin?.let {
            it.imgToolLeft.setImageResource(R.drawable.ic_back)
            it.txtToolTitle.text = "LOCAL CHAT"
            it.imgToolLeft.setOnClickListener {
                onBackPressed()
            }
        }
    }
}