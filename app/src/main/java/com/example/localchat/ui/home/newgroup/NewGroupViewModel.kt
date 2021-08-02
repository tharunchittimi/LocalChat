package com.example.localchat.ui.home.newgroup

import com.example.localchat.base.BaseViewModel


class NewGroupViewModel : BaseViewModel<NewGroupView>() {
    fun addLocalDBContactsList(userId: String?) {
        navigator?.updateData(
            appDataManager.getDatabaseHelper().getUserDataBaseDao()?.getAllUsers(userId)
        )
    }
}