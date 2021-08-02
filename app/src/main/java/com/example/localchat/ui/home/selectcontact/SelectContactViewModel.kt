package com.example.localchat.ui.home.selectcontact

import com.example.localchat.base.BaseViewModel


class SelectContactViewModel : BaseViewModel<SelectContactView>() {
    fun addLocalDBContactsList(userId: String?) {
        navigator?.updateData(
            appDataManager.getDatabaseHelper().getUserDataBaseDao()?.getAllUsers(userId)
        )
    }
}