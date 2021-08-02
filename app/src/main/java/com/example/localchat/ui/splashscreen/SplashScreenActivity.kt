package com.example.localchat.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.localchat.R
import com.example.localchat.BR
import com.example.localchat.base.BaseActivity
import com.example.localchat.base.room.entities.UserEntity
import com.example.localchat.databinding.ActivitySplashScreenBinding
import com.example.localchat.ui.home.chat.ChatHomeActivity
import com.example.localchat.ui.login.LoginActivity

class SplashScreenActivity :
    BaseActivity<ActivitySplashScreenBinding, SplashScreenView, SplashScreenViewModel>(),
    SplashScreenView {
    override fun setViewModelClass(): Class<SplashScreenViewModel> {
        return SplashScreenViewModel::class.java
    }

    override fun getContentView(): Int = R.layout.activity_splash_screen


    override fun getNavigator(): SplashScreenView = this

    override fun getBindingVariable(): Int = BR.splashScreen

    override fun initViews(savedInstanceState: Bundle?) {
        showOrHideStatusBar(false)
        allDummyUsers()
        if (getCurrentLoggedInUserData() == null) {
            changeScreenTo(LoginActivity::class.java, 2500)
        } else {
            changeScreenTo(ChatHomeActivity::class.java, 2500)
        }
    }

    private fun allDummyUsers() {
        mViewModel?.appDataManager?.getDatabaseHelper()?.getUserDataBaseDao()
            ?.getAllUsers(getCurrentLoggedInUserData()?.userId)
            ?.let { users ->
                if (users.size <= 0) {
                    for (i in 0..9) {
                        val userEntity = UserEntity(
                            System.currentTimeMillis().toString(),
                            "Tharun Kumar $i",
                            "888540140$i",
                            "Qwerty"
                        )
                        mViewModel?.appDataManager?.getDatabaseHelper()?.getUserDataBaseDao()
                            ?.insertUser(userEntity)
                    }
                }
            }
    }

    private fun changeScreenTo(mActivity: Class<*>, runningTime: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            this.startActivity(Intent(this@SplashScreenActivity, mActivity))
            finish()
        }, runningTime)
    }

}
