package com.example.localchat.ui.home.newgroup

import com.example.localchat.base.BaseNavigator
import com.example.localchat.base.room.entities.UserEntity


interface NewGroupView : BaseNavigator {
    fun updateData(selectContactList: List<UserEntity?>?)
}