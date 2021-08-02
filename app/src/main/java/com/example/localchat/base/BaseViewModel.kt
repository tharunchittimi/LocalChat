package com.example.localchat.base

import androidx.lifecycle.ViewModel
import com.example.localchat.data.AppDataManager


open class BaseViewModel<N : BaseNavigator>(val appDataManager: AppDataManager = AppDataManager.getMyInstance()) :
    ViewModel() {

    var navigator: N? = null
}